package io.github.adrianclarkhub.okx.websocket.common;

import io.github.adrianclarkhub.okx.core.exception.OkxWebSocketException;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

/**
 * OKX WebSocket 连接包装。
 */
public class OkxWebSocketConnection {

    public static final String TEXT_PING = "ping";

    public static final String TEXT_PONG = "pong";

    private final WebSocket webSocket;

    public OkxWebSocketConnection(WebSocket webSocket) {
        if (webSocket == null) {
            throw new OkxWebSocketException("WebSocket must not be null.");
        }
        this.webSocket = webSocket;
    }

    /**
     * 发送文本消息。
     *
     * @param text 文本消息
     * @return 发送完成 future
     */
    public CompletableFuture<WebSocket> sendText(String text) {
        return webSocket.sendText(text, true);
    }

    /**
     * 发送 OKX 文本 ping 心跳。
     *
     * @return 发送完成 future
     */
    public CompletableFuture<WebSocket> sendTextPing() {
        return sendText(TEXT_PING);
    }

    /**
     * 发送 ping 帧。
     *
     * @param text ping 内容
     * @return 发送完成 future
     */
    public CompletableFuture<WebSocket> sendPing(String text) {
        return webSocket.sendPing(java.nio.ByteBuffer.wrap(text == null ? new byte[0] : text.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }

    /**
     * 关闭连接。
     *
     * @param reason 关闭原因
     * @return 关闭完成 future
     */
    public CompletableFuture<WebSocket> close(String reason) {
        return webSocket.sendClose(WebSocket.NORMAL_CLOSURE, reason == null ? "" : reason);
    }

    /**
     * 请求更多消息。
     *
     * @param n 消息数量
     */
    public void request(long n) {
        webSocket.request(n);
    }

    /**
     * 获取 JDK 原始 WebSocket。
     *
     * @return 原始 WebSocket
     */
    public WebSocket unwrap() {
        return webSocket;
    }
}
