package io.github.adrianclarkhub.okx.websocket.common;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxHttpConfig;
import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import io.github.adrianclarkhub.okx.core.config.OkxWebSocketConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxApiException;
import io.github.adrianclarkhub.okx.core.exception.OkxConfigurationException;
import io.github.adrianclarkhub.okx.core.exception.OkxWebSocketException;
import io.github.adrianclarkhub.okx.core.http.OkxProxySupport;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * OKX WebSocket 底层客户端。
 *
 * <p>负责根据统一配置创建公共、私有、业务 WebSocket 连接，并复用代理和连接超时配置。</p>
 */
public class OkxWebSocketClient {

    private final OkxConfig config;

    private final HttpClient httpClient;

    public OkxWebSocketClient(OkxConfig config) {
        this(config, createHttpClient(config));
    }

    public OkxWebSocketClient(OkxConfig config, HttpClient httpClient) {
        if (config == null) {
            throw new OkxConfigurationException("OKX config must not be null.");
        }
        if (httpClient == null) {
            throw new OkxConfigurationException("OKX WebSocket HTTP client must not be null.");
        }
        config.normalize();
        this.config = config;
        this.httpClient = httpClient;
    }

    /**
     * 连接 OKX 公共频道。
     *
     * @param listener 消息监听器
     * @return 连接 future
     */
    public CompletableFuture<OkxWebSocketConnection> connectPublic(OkxWebSocketListener listener) {
        return connect(config.resolveWsPublicUrl(), listener);
    }

    /**
     * 连接 OKX 私有频道。
     *
     * @param listener 消息监听器
     * @return 连接 future
     */
    public CompletableFuture<OkxWebSocketConnection> connectPrivate(OkxWebSocketListener listener) {
        return connect(config.resolveWsPrivateUrl(), listener);
    }

    /**
     * 连接 OKX 业务频道。
     *
     * @param listener 消息监听器
     * @return 连接 future
     */
    public CompletableFuture<OkxWebSocketConnection> connectBusiness(OkxWebSocketListener listener) {
        return connect(config.resolveWsBusinessUrl(), listener);
    }

    /**
     * 连接指定 WebSocket 地址。
     *
     * @param url WebSocket 地址
     * @param listener 消息监听器
     * @return 连接 future
     */
    public CompletableFuture<OkxWebSocketConnection> connect(String url, OkxWebSocketListener listener) {
        if (url == null || url.trim().isEmpty()) {
            throw new OkxConfigurationException("OKX WebSocket URL must not be empty.");
        }
        URI uri = URI.create(url.trim());
        if (!"wss".equalsIgnoreCase(uri.getScheme()) && !"ws".equalsIgnoreCase(uri.getScheme())) {
            throw new OkxConfigurationException("Invalid OKX WebSocket URL: " + url);
        }
        ListenerAdapter adapter = new ListenerAdapter(listener == null ? new OkxWebSocketListener() {
        } : listener);
        return httpClient.newWebSocketBuilder()
                .connectTimeout(resolveConnectTimeout(config))
                .buildAsync(uri, adapter)
                .thenApply(webSocket -> {
                    OkxWebSocketConnection connection = new OkxWebSocketConnection(webSocket);
                    adapter.setConnection(connection);
                    return connection;
                });
    }

    /**
     * 创建公共频道会话。
     *
     * @param listener 消息监听器
     * @return WebSocket 会话
     */
    public OkxWebSocketSession publicSession(OkxWebSocketListener listener) {
        return createSession(config.resolveWsPublicUrl(), listener);
    }

    /**
     * 创建私有频道会话。
     *
     * @param listener 消息监听器
     * @return WebSocket 会话
     */
    public OkxWebSocketSession privateSession(OkxWebSocketListener listener) {
        return createSession(config.resolveWsPrivateUrl(), listener);
    }

    /**
     * 创建带登录请求的私有频道会话。
     *
     * <p>连接建立后会自动发送 OKX WebSocket login 请求。</p>
     *
     * @param listener 消息监听器
     * @return WebSocket 会话
     */
    public OkxWebSocketSession authenticatedPrivateSession(OkxWebSocketListener listener) {
        return createSession(config.resolveWsPrivateUrl(),
                new LoginOnOpenListener(listener, new OkxWebSocketAuthClient(config)));
    }

    /**
     * 创建业务频道会话。
     *
     * @param listener 消息监听器
     * @return WebSocket 会话
     */
    public OkxWebSocketSession businessSession(OkxWebSocketListener listener) {
        return createSession(config.resolveWsBusinessUrl(), listener);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    private OkxWebSocketSession createSession(String url, OkxWebSocketListener listener) {
        OkxWebSocketConfig webSocketConfig = config.getWebSocket() == null
                ? new OkxWebSocketConfig() : config.getWebSocket();
        return new OkxWebSocketSession(this, url, listener,
                webSocketConfig.getHeartbeatIntervalMillis(),
                webSocketConfig.getReconnectDelayMillis(),
                webSocketConfig.getMaxReconnectAttempts());
    }

    static HttpClient createHttpClient(OkxConfig config) {
        if (config == null) {
            throw new OkxConfigurationException("OKX config must not be null.");
        }
        OkxHttpConfig httpConfig = config.getHttp() == null ? new OkxHttpConfig() : config.getHttp();
        validateHttpConfig(httpConfig);
        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(resolveConnectTimeout(config));
        OkxProxyConfig proxy = httpConfig.getProxy();
        if (proxy != null && proxy.isEnabled()) {
            builder.proxy(ProxySelector.of(new InetSocketAddress(proxy.getHost(), proxy.getPort())));
            if (OkxProxySupport.hasAuthentication(proxy)) {
                builder.authenticator(OkxProxySupport.jdkAuthenticator(proxy));
            }
        }
        return builder.build();
    }

    private static Duration resolveConnectTimeout(OkxConfig config) {
        OkxHttpConfig httpConfig = config.getHttp();
        int timeoutMillis = httpConfig == null ? new OkxHttpConfig().getConnectTimeoutMillis()
                : httpConfig.getConnectTimeoutMillis();
        return Duration.ofMillis(timeoutMillis);
    }

    private static void validateHttpConfig(OkxHttpConfig httpConfig) {
        if (httpConfig.getConnectTimeoutMillis() <= 0) {
            throw new OkxConfigurationException("OKX WebSocket connect timeout must be greater than 0.");
        }
        OkxProxySupport.validate(httpConfig.getProxy(), "OKX WebSocket");
    }

    private static final class ListenerAdapter implements WebSocket.Listener {

        private final OkxWebSocketListener delegate;

        private final OkxWebSocketProtocolClient protocolClient = new OkxWebSocketProtocolClient();

        private final StringBuilder messageBuffer = new StringBuilder();

        private volatile OkxWebSocketConnection connection;

        private ListenerAdapter(OkxWebSocketListener delegate) {
            this.delegate = delegate;
        }

        private void setConnection(OkxWebSocketConnection connection) {
            this.connection = connection;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            OkxWebSocketConnection openedConnection = new OkxWebSocketConnection(webSocket);
            this.connection = openedConnection;
            delegate.onOpen(openedConnection);
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            messageBuffer.append(data);
            if (last) {
                try {
                    String message = messageBuffer.toString();
                    if (OkxWebSocketConnection.TEXT_PONG.equals(message)) {
                        delegate.onPong(connection(webSocket));
                    } else {
                        Throwable error = protocolClient.parseErrorEvent(message);
                        if (error != null) {
                            delegate.onError(connection(webSocket), error);
                        } else {
                            delegate.onText(connection(webSocket), message);
                        }
                    }
                } catch (RuntimeException e) {
                    delegate.onError(connection(webSocket), e);
                } finally {
                    messageBuffer.setLength(0);
                }
            }
            webSocket.request(1);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            OkxWebSocketConnection currentConnection = connection(webSocket);
            OkxApiException closeException = protocolClient.parseCloseFrame(statusCode, reason);
            if (closeException != null) {
                delegate.onError(currentConnection, closeException);
            }
            delegate.onClose(currentConnection, statusCode, reason);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            delegate.onError(webSocket == null ? connection : connection(webSocket), error);
        }

        private OkxWebSocketConnection connection(WebSocket webSocket) {
            if (connection == null) {
                if (webSocket == null) {
                    throw new OkxWebSocketException("OKX WebSocket connection is not available.");
                }
                connection = new OkxWebSocketConnection(webSocket);
            }
            return connection;
        }
    }

    private static final class LoginOnOpenListener implements OkxWebSocketListener {

        private final OkxWebSocketListener delegate;

        private final OkxWebSocketAuthClient authClient;

        private LoginOnOpenListener(OkxWebSocketListener delegate, OkxWebSocketAuthClient authClient) {
            this.delegate = delegate == null ? new OkxWebSocketListener() {
            } : delegate;
            this.authClient = authClient;
        }

        @Override
        public void onOpen(OkxWebSocketConnection connection) {
            connection.sendText(authClient.loginJson());
            delegate.onOpen(connection);
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
        }

        @Override
        public void onError(OkxWebSocketConnection connection, Throwable error) {
            delegate.onError(connection, error);
        }
    }
}
