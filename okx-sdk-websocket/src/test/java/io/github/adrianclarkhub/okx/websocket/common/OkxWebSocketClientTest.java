package io.github.adrianclarkhub.okx.websocket.common;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxConfigurationException;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * OKX WebSocket 底层客户端测试。
 */
class OkxWebSocketClientTest {

    @Test
    void shouldCreateHttpClientWithDefaultConfig() {
        HttpClient httpClient = OkxWebSocketClient.createHttpClient(new OkxConfig());

        assertNotNull(httpClient, "WebSocket HTTP client should be created.");
    }

    @Test
    void shouldRejectEnabledProxyWithoutHost() {
        OkxConfig config = new OkxConfig();
        OkxProxyConfig proxy = new OkxProxyConfig();
        proxy.setEnabled(true);
        proxy.setPort(7897);
        config.getHttp().setProxy(proxy);

        assertThrows(OkxConfigurationException.class, () -> OkxWebSocketClient.createHttpClient(config),
                "Enabled proxy requires host.");
    }

    @Test
    void shouldCreateHttpClientWithAuthenticatedProxy() {
        OkxConfig config = new OkxConfig();
        OkxProxyConfig proxy = new OkxProxyConfig();
        proxy.setEnabled(true);
        proxy.setHost("127.0.0.1");
        proxy.setPort(7897);
        proxy.setUsername("proxy-user");
        proxy.setPassword("proxy-password");
        config.getHttp().setProxy(proxy);

        HttpClient httpClient = OkxWebSocketClient.createHttpClient(config);

        assertNotNull(httpClient.authenticator().orElse(null), "WebSocket proxy authentication should be configured.");
    }

    @Test
    void shouldRejectInvalidWebSocketUrl() {
        OkxWebSocketClient client = new OkxWebSocketClient(new OkxConfig());

        assertThrows(OkxConfigurationException.class, () -> client.connect("https://www.okx.com", null),
                "WebSocket URL must use ws or wss scheme.");
    }

    @Test
    void shouldSendAndCloseConnection() {
        RecordingWebSocket webSocket = new RecordingWebSocket();
        OkxWebSocketConnection connection = new OkxWebSocketConnection(webSocket);

        connection.sendText("{\"op\":\"subscribe\"}");
        connection.sendTextPing();
        connection.close("done");

        assertEquals("ping", webSocket.text, "Text ping should be sent to underlying WebSocket.");
        assertEquals(WebSocket.NORMAL_CLOSURE, webSocket.closeStatus, "Connection should close normally.");
        assertEquals("done", webSocket.closeReason, "Close reason should be passed through.");
    }

    private static final class RecordingWebSocket implements WebSocket {

        private String text;

        private int closeStatus;

        private String closeReason;

        @Override
        public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {
            this.text = data == null ? null : data.toString();
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendBinary(ByteBuffer data, boolean last) {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendPing(ByteBuffer message) {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendPong(ByteBuffer message) {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendClose(int statusCode, String reason) {
            this.closeStatus = statusCode;
            this.closeReason = reason;
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public void request(long n) {
        }

        @Override
        public String getSubprotocol() {
            return "";
        }

        @Override
        public boolean isOutputClosed() {
            return false;
        }

        @Override
        public boolean isInputClosed() {
            return false;
        }

        @Override
        public void abort() {
        }
    }
}
