package io.github.adrianclarkhub.okx.websocket.common;

import io.github.adrianclarkhub.okx.core.exception.OkxWebSocketException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OKX WebSocket 会话。
 *
 * <p>在底层连接之上提供文本心跳、断线重连和订阅消息恢复能力。</p>
 */
public class OkxWebSocketSession implements OkxWebSocketListener, AutoCloseable {

    private static final long DEFAULT_HEARTBEAT_INTERVAL_MILLIS = 25000L;

    private static final long DEFAULT_RECONNECT_DELAY_MILLIS = 5000L;

    private static final int DEFAULT_MAX_RECONNECT_ATTEMPTS = 3;

    private final OkxWebSocketClient client;

    private final String url;

    private final OkxWebSocketListener delegate;

    private final long heartbeatIntervalMillis;

    private final long reconnectDelayMillis;

    private final int maxReconnectAttempts;

    private final ScheduledExecutorService scheduler;

    private final List<String> replayMessages = new CopyOnWriteArrayList<String>();

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final AtomicBoolean reconnectScheduled = new AtomicBoolean(false);

    private final AtomicInteger reconnectAttempts = new AtomicInteger(0);

    private volatile OkxWebSocketConnection connection;

    private volatile ScheduledFuture<?> heartbeatTask;

    public OkxWebSocketSession(OkxWebSocketClient client, String url, OkxWebSocketListener listener) {
        this(client, url, listener, DEFAULT_HEARTBEAT_INTERVAL_MILLIS, DEFAULT_RECONNECT_DELAY_MILLIS,
                DEFAULT_MAX_RECONNECT_ATTEMPTS);
    }

    public OkxWebSocketSession(OkxWebSocketClient client, String url, OkxWebSocketListener listener,
                               long heartbeatIntervalMillis, long reconnectDelayMillis, int maxReconnectAttempts) {
        if (client == null) {
            throw new OkxWebSocketException("OKX WebSocket client must not be null.");
        }
        if (url == null || url.trim().isEmpty()) {
            throw new OkxWebSocketException("OKX WebSocket session URL must not be empty.");
        }
        if (heartbeatIntervalMillis <= 0) {
            throw new OkxWebSocketException("OKX WebSocket heartbeat interval must be greater than 0.");
        }
        if (reconnectDelayMillis < 0) {
            throw new OkxWebSocketException("OKX WebSocket reconnect delay must not be negative.");
        }
        if (maxReconnectAttempts < 0) {
            throw new OkxWebSocketException("OKX WebSocket max reconnect attempts must not be negative.");
        }
        this.client = client;
        this.url = url.trim();
        this.delegate = listener == null ? new OkxWebSocketListener() {
        } : listener;
        this.heartbeatIntervalMillis = heartbeatIntervalMillis;
        this.reconnectDelayMillis = reconnectDelayMillis;
        this.maxReconnectAttempts = maxReconnectAttempts;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new SessionThreadFactory());
    }

    /**
     * 启动会话连接。
     */
    public CompletableFuture<OkxWebSocketConnection> start() {
        closed.set(false);
        CompletableFuture<OkxWebSocketConnection> future = connect();
        startHeartbeat();
        return future;
    }

    /**
     * 发送消息。
     *
     * @param message 文本消息
     */
    public void send(String message) {
        OkxWebSocketConnection currentConnection = connection;
        if (currentConnection == null) {
            throw new OkxWebSocketException("OKX WebSocket session is not connected.");
        }
        currentConnection.sendText(message);
    }

    /**
     * 注册可恢复消息并发送。
     *
     * <p>订阅类消息建议使用该方法，断线重连后会自动重发。</p>
     *
     * @param message 文本消息
     */
    public void registerAndSend(String message) {
        registerReplayMessage(message);
        send(message);
    }

    /**
     * 注册重连后需要重放的消息。
     *
     * @param message 文本消息
     */
    public void registerReplayMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new OkxWebSocketException("OKX WebSocket replay message must not be empty.");
        }
        if (!replayMessages.contains(message)) {
            replayMessages.add(message);
        }
    }

    /**
     * 移除重连重放消息。
     *
     * @param message 文本消息
     */
    public void unregisterReplayMessage(String message) {
        replayMessages.remove(message);
    }

    @Override
    public void onOpen(OkxWebSocketConnection connection) {
        this.connection = connection;
        reconnectAttempts.set(0);
        reconnectScheduled.set(false);
        delegate.onOpen(connection);
        replayRegisteredMessages();
    }

    @Override
    public void onText(OkxWebSocketConnection connection, String text) {
        delegate.onText(connection, text);
    }

    @Override
    public void onPong(OkxWebSocketConnection connection) {
        delegate.onPong(connection);
    }

    @Override
    public void onClose(OkxWebSocketConnection connection, int statusCode, String reason) {
        delegate.onClose(connection, statusCode, reason);
        scheduleReconnect();
    }

    @Override
    public void onError(OkxWebSocketConnection connection, Throwable error) {
        delegate.onError(connection, error);
        scheduleReconnect();
    }

    @Override
    public void close() {
        closed.set(true);
        stopHeartbeat();
        OkxWebSocketConnection currentConnection = connection;
        if (currentConnection != null) {
            currentConnection.close("session closed");
        }
        scheduler.shutdownNow();
    }

    private CompletableFuture<OkxWebSocketConnection> connect() {
        return client.connect(url, this).exceptionally(error -> {
            delegate.onError(null, error);
            scheduleReconnect();
            return null;
        });
    }

    private void startHeartbeat() {
        stopHeartbeat();
        heartbeatTask = scheduler.scheduleAtFixedRate(() -> {
            OkxWebSocketConnection currentConnection = connection;
            if (!closed.get() && currentConnection != null) {
                currentConnection.sendTextPing();
            }
        }, heartbeatIntervalMillis, heartbeatIntervalMillis, TimeUnit.MILLISECONDS);
    }

    private void stopHeartbeat() {
        ScheduledFuture<?> currentTask = heartbeatTask;
        if (currentTask != null) {
            currentTask.cancel(true);
        }
    }

    private void scheduleReconnect() {
        if (closed.get()) {
            return;
        }
        int attempt = reconnectAttempts.incrementAndGet();
        if (attempt > maxReconnectAttempts) {
            delegate.onError(connection, new OkxWebSocketException("OKX WebSocket reconnect attempts exceeded."));
            return;
        }
        if (!reconnectScheduled.compareAndSet(false, true)) {
            return;
        }
        scheduler.schedule(() -> {
            reconnectScheduled.set(false);
            if (!closed.get()) {
                connect();
            }
        }, reconnectDelayMillis, TimeUnit.MILLISECONDS);
    }

    private void replayRegisteredMessages() {
        OkxWebSocketConnection currentConnection = connection;
        if (currentConnection == null) {
            return;
        }
        for (String replayMessage : replayMessages) {
            currentConnection.sendText(replayMessage);
        }
    }

    private static final class SessionThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "okx-websocket-session");
            thread.setDaemon(true);
            return thread;
        }
    }
}
