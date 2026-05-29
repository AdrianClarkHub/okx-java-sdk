package io.github.adrianclarkhub.okx.websocket.common;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OKX WebSocket 会话测试。
 */
class OkxWebSocketSessionTest {

    @Test
    void shouldConnectAndSendReplayMessage() {
        RecordingWebSocketClient client = new RecordingWebSocketClient();
        OkxWebSocketSession session = new OkxWebSocketSession(client, "wss://example.okx.com/ws", null,
                1000L, 0L, 1);

        session.start();
        session.registerAndSend("{\"op\":\"subscribe\"}");

        assertEquals(1, client.connectCalls, "Session should connect once.");
        assertEquals("{\"op\":\"subscribe\"}", client.webSocket.sentTexts.get(0),
                "Registered message should be sent immediately.");

        session.close();
    }

    @Test
    void shouldReplayRegisteredMessagesAfterReconnect() throws Exception {
        RecordingWebSocketClient client = new RecordingWebSocketClient();
        OkxWebSocketSession session = new OkxWebSocketSession(client, "wss://example.okx.com/ws", null,
                1000L, 1L, 1);

        session.start();
        session.registerAndSend("{\"op\":\"subscribe\"}");
        client.listener.onClose(client.connection, WebSocket.NORMAL_CLOSURE, "closed");
        Thread.sleep(80L);

        assertEquals(2, client.connectCalls, "Session should reconnect once.");
        assertEquals(2, client.webSocket.sentTexts.size(), "Registered message should be replayed after reconnect.");
        assertEquals("{\"op\":\"subscribe\"}", client.webSocket.sentTexts.get(1),
                "Replay message should match original subscription.");

        session.close();
    }

    @Test
    void shouldNotReconnectAfterClose() throws Exception {
        RecordingWebSocketClient client = new RecordingWebSocketClient();
        OkxWebSocketSession session = new OkxWebSocketSession(client, "wss://example.okx.com/ws", null,
                1000L, 1L, 3);

        session.start();
        session.close();
        client.listener.onClose(client.connection, WebSocket.NORMAL_CLOSURE, "closed");
        Thread.sleep(80L);

        assertEquals(1, client.connectCalls, "Closed session should not reconnect.");
        assertTrue(client.webSocket.closed, "Underlying WebSocket should be closed.");
    }

    @Test
    void shouldCreateSessionsFromClient() {
        OkxConfig config = new OkxConfig();
        config.setApiKey("api-key");
        config.setSecretKey("secret-key");
        config.setPassphrase("passphrase");
        OkxWebSocketClient client = new OkxWebSocketClient(config, HttpClient.newHttpClient());

        assertFalse(client.publicSession(null) == null, "Public session should be created.");
        assertFalse(client.privateSession(null) == null, "Private session should be created.");
        assertFalse(client.authenticatedPrivateSession(null) == null, "Authenticated private session should be created.");
        assertFalse(client.businessSession(null) == null, "Business session should be created.");
    }

    @Test
    void shouldSendLoginWhenAuthenticatedPrivateSessionOpens() {
        RecordingWebSocketClient client = new RecordingWebSocketClient();
        OkxWebSocketSession session = client.authenticatedPrivateSession(null);

        session.start();

        assertTrue(client.webSocket.sentTexts.get(0).contains("\"op\":\"login\""),
                "Authenticated private session should send login request on open.");

        session.close();
    }

    @Test
    void shouldLoginBeforeReplayingPrivateSubscriptionsAfterReconnect() throws Exception {
        RecordingWebSocketClient client = new RecordingWebSocketClient();
        OkxWebSocketListener loginListener = new OkxWebSocketListener() {
            @Override
            public void onOpen(OkxWebSocketConnection connection) {
                connection.sendText("{\"op\":\"login\"}");
            }
        };
        OkxWebSocketSession session = new OkxWebSocketSession(client, "wss://example.okx.com/ws", loginListener,
                1000L, 1L, 1);

        session.start();
        session.registerAndSend("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"orders\"}]}");
        client.webSocket.sentTexts.clear();
        client.listener.onClose(client.connection, WebSocket.NORMAL_CLOSURE, "closed");
        Thread.sleep(80L);

        assertTrue(client.webSocket.sentTexts.get(0).contains("\"op\":\"login\""),
                "Private reconnect should login before replaying subscriptions.");
        assertEquals("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"orders\"}]}",
                client.webSocket.sentTexts.get(1), "Private subscription should be replayed after login.");

        session.close();
    }

    private static final class RecordingWebSocketClient extends OkxWebSocketClient {

        private int connectCalls;

        private OkxWebSocketListener listener;

        private final RecordingWebSocket webSocket = new RecordingWebSocket();

        private final OkxWebSocketConnection connection = new OkxWebSocketConnection(webSocket);

        private RecordingWebSocketClient() {
            super(credentialsConfig(), HttpClient.newHttpClient());
        }

        @Override
        public CompletableFuture<OkxWebSocketConnection> connect(String url, OkxWebSocketListener listener) {
            connectCalls++;
            this.listener = listener;
            listener.onOpen(connection);
            return CompletableFuture.completedFuture(connection);
        }
    }

    private static OkxConfig credentialsConfig() {
        OkxConfig config = new OkxConfig();
        config.setApiKey("api-key");
        config.setSecretKey("secret-key");
        config.setPassphrase("passphrase");
        config.normalize();
        return config;
    }

    private static final class RecordingWebSocket implements WebSocket {

        private final List<String> sentTexts = new ArrayList<String>();

        private boolean closed;

        @Override
        public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {
            sentTexts.add(data == null ? null : data.toString());
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
            closed = true;
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
            return closed;
        }

        @Override
        public boolean isInputClosed() {
            return closed;
        }

        @Override
        public void abort() {
            closed = true;
        }
    }
}
