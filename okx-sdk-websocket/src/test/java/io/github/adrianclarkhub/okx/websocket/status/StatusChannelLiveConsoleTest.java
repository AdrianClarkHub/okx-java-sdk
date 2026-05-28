package io.github.adrianclarkhub.okx.websocket.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfigLoader;
import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Status WebSocket 频道公网 Live Test。
 *
 * <p>使用统一的 {@link OkxConfigLoader} 加载 {@code okx.*} 配置，默认关闭。</p>
 */
@EnabledIf("liveTestsEnabled")
class StatusChannelLiveConsoleTest {

    private static final String LOCAL_LIVE_CONFIG = "okx-live.local.properties";

    private static final OkxConfig OKX_CONFIG = loadLiveTestConfig();

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);

    private static final Duration SUBSCRIBE_TIMEOUT = Duration.ofSeconds(15);

    private final StatusChannelClient client = new StatusChannelClient();

    @Test
    void shouldSubscribeStatusChannelFromOkxPublicWebSocket() throws Exception {
        StatusWebSocketListener listener = new StatusWebSocketListener();
        WebSocket webSocket;
        try {
            webSocket = newHttpClient()
                    .newWebSocketBuilder()
                    .connectTimeout(CONNECT_TIMEOUT)
                    .buildAsync(URI.create(OKX_CONFIG.resolveWsPublicUrl()), listener)
                    .get(CONNECT_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            Assumptions.abort("OKX live WebSocket unavailable this run: " + e.getMessage());
            return;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assumptions.abort("OKX live WebSocket interrupted this run: " + e.getMessage());
            return;
        }

        try {
            webSocket.sendText(client.subscribeJson("1512"), true)
                    .get(CONNECT_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
            boolean subscribed = listener.awaitSubscribe(SUBSCRIBE_TIMEOUT);
            if (!subscribed && listener.getFailure() != null) {
                Assumptions.abort("OKX live WebSocket failed this run: " + listener.getFailure());
                return;
            }

            assertTrue(subscribed, "Live status channel should acknowledge subscription.");
            assertNotNull(listener.getSubscribeEvent(), "Subscribe event should be captured.");
            assertEquals("subscribe", listener.getSubscribeEvent().get("event").asText(), "Event should be subscribe.");
            assertEquals("status", listener.getSubscribeEvent().get("arg").get("channel").asText(),
                    "Subscribed channel should be status.");

            System.out.println("OKX Live WebSocket: subscribe status");
            System.out.println("WebSocket URL: " + OKX_CONFIG.resolveWsPublicUrl());
            System.out.println("Subscribe event: " + listener.getSubscribeEvent());
        } finally {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "test complete");
        }
    }

    static boolean liveTestsEnabled() {
        return OKX_CONFIG.isLiveTestsEnabled();
    }

    private static OkxConfig loadLiveTestConfig() {
        String explicitConfigFile = System.getProperty(OkxConfigLoader.CONFIG_FILE_PROPERTY);
        if (explicitConfigFile != null && !explicitConfigFile.trim().isEmpty()) {
            return OkxConfigLoader.load();
        }
        String explicitConfigFileEnv = System.getenv(OkxConfigLoader.CONFIG_FILE_ENV);
        if (explicitConfigFileEnv != null && !explicitConfigFileEnv.trim().isEmpty()) {
            return OkxConfigLoader.load();
        }
        return OkxConfigLoader.loadFromClasspath(LOCAL_LIVE_CONFIG);
    }

    private static HttpClient newHttpClient() {
        HttpClient.Builder builder = HttpClient.newBuilder().connectTimeout(CONNECT_TIMEOUT);
        OkxProxyConfig proxy = OKX_CONFIG.getHttp() == null ? null : OKX_CONFIG.getHttp().getProxy();
        if (proxy != null && proxy.isEnabled() && proxy.getHost() != null && !proxy.getHost().isEmpty()
                && proxy.getPort() > 0) {
            builder.proxy(ProxySelector.of(new InetSocketAddress(proxy.getHost(), proxy.getPort())));
        }
        return builder.build();
    }

    private static final class StatusWebSocketListener implements WebSocket.Listener {

        private final ObjectMapper objectMapper = new ObjectMapper();

        private final StringBuilder messageBuffer = new StringBuilder();

        private final CountDownLatch subscribeLatch = new CountDownLatch(1);

        private final AtomicReference<JsonNode> subscribeEvent = new AtomicReference<>();

        private final AtomicReference<Throwable> failure = new AtomicReference<>();

        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            messageBuffer.append(data);
            if (last) {
                handleMessage(messageBuffer.toString());
                messageBuffer.setLength(0);
            }
            webSocket.request(1);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            failure.compareAndSet(null, error);
            subscribeLatch.countDown();
        }

        boolean awaitSubscribe(Duration timeout) throws InterruptedException {
            return subscribeLatch.await(timeout.toMillis(), TimeUnit.MILLISECONDS)
                    && subscribeEvent.get() != null;
        }

        JsonNode getSubscribeEvent() {
            return subscribeEvent.get();
        }

        Throwable getFailure() {
            return failure.get();
        }

        private void handleMessage(String message) {
            try {
                JsonNode root = objectMapper.readTree(message);
                if (root.has("event") && "subscribe".equals(root.get("event").asText())
                        && root.has("arg") && "status".equals(root.get("arg").get("channel").asText())) {
                    subscribeEvent.compareAndSet(null, root);
                    subscribeLatch.countDown();
                } else if (root.has("event") && "error".equals(root.get("event").asText())) {
                    failure.compareAndSet(null, new IllegalStateException(message));
                    subscribeLatch.countDown();
                }
            } catch (Exception e) {
                failure.compareAndSet(null, e);
                subscribeLatch.countDown();
            }
        }
    }
}
