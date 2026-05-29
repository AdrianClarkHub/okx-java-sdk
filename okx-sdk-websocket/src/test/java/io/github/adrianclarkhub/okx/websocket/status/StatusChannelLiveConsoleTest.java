package io.github.adrianclarkhub.okx.websocket.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfigLoader;
import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketClient;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketListener;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
        printLiveConfig();
        StatusWebSocketListener listener = new StatusWebSocketListener();
        OkxWebSocketSession session = new OkxWebSocketClient(OKX_CONFIG).publicSession(listener);
        session.start().get(CONNECT_TIMEOUT.toSeconds(), TimeUnit.SECONDS);

        try {
            session.registerAndSend(client.subscribeJson("1512"));
            boolean subscribed = listener.awaitSubscribe(SUBSCRIBE_TIMEOUT);
            if (!subscribed && listener.getFailure() != null) {
                fail("OKX live WebSocket failed this run: " + listener.getFailure());
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
            session.close();
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

    private static void printLiveConfig() {
        OkxProxyConfig proxy = OKX_CONFIG.getHttp() == null ? null : OKX_CONFIG.getHttp().getProxy();
        System.out.println("OKX live WebSocket URL: " + OKX_CONFIG.resolveWsPublicUrl());
        if (proxy == null || !proxy.isEnabled()) {
            System.out.println("OKX live proxy: disabled");
            return;
        }
        System.out.println("OKX live proxy: " + proxy.getHost() + ":" + proxy.getPort());
    }

    private static final class StatusWebSocketListener implements OkxWebSocketListener {

        private final ObjectMapper objectMapper = new ObjectMapper();

        private final StringBuilder messageBuffer = new StringBuilder();

        private final CountDownLatch subscribeLatch = new CountDownLatch(1);

        private final AtomicReference<JsonNode> subscribeEvent = new AtomicReference<>();

        private final AtomicReference<Throwable> failure = new AtomicReference<>();

        @Override
        public void onText(io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketConnection connection, String text) {
            messageBuffer.append(text);
            handleMessage(messageBuffer.toString());
            messageBuffer.setLength(0);
        }

        @Override
        public void onError(io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketConnection connection, Throwable error) {
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
