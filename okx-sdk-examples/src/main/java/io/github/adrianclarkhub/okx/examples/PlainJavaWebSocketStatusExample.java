package io.github.adrianclarkhub.okx.examples;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfigLoader;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketClient;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketConnection;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketListener;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketSession;
import io.github.adrianclarkhub.okx.websocket.status.StatusChannelClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 普通 Java WebSocket status 频道示例。
 */
public final class PlainJavaWebSocketStatusExample {

    private PlainJavaWebSocketStatusExample() {
    }

    public static void main(String[] args) throws Exception {
        OkxConfig config = OkxConfigLoader.load();
        StatusChannelClient statusChannelClient = new StatusChannelClient();
        CountDownLatch firstMessage = new CountDownLatch(1);

        OkxWebSocketSession session = new OkxWebSocketClient(config).publicSession(new OkxWebSocketListener() {
            @Override
            public void onOpen(OkxWebSocketConnection connection) {
                System.out.println("OKX WebSocket connected.");
            }

            @Override
            public void onText(OkxWebSocketConnection connection, String text) {
                System.out.println("OKX WebSocket message: " + text);
                firstMessage.countDown();
            }

            @Override
            public void onError(OkxWebSocketConnection connection, Throwable error) {
                error.printStackTrace();
                firstMessage.countDown();
            }
        });

        session.start().get(10, TimeUnit.SECONDS);
        session.registerAndSend(statusChannelClient.subscribeJson("status-example"));
        firstMessage.await(30, TimeUnit.SECONDS);
        session.close();
    }
}
