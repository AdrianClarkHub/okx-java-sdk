package io.github.adrianclarkhub.okx.websocket.common;

/**
 * OKX WebSocket 消息监听器。
 */
public interface OkxWebSocketListener {

    /**
     * 连接打开后回调。
     *
     * @param connection WebSocket 连接
     */
    default void onOpen(OkxWebSocketConnection connection) {
    }

    /**
     * 收到完整文本消息后回调。
     *
     * @param connection WebSocket 连接
     * @param text 文本消息
     */
    default void onText(OkxWebSocketConnection connection, String text) {
    }

    /**
     * 收到 OKX 文本 pong 心跳后回调。
     *
     * @param connection WebSocket 连接
     */
    default void onPong(OkxWebSocketConnection connection) {
    }

    /**
     * 连接正常关闭后回调。
     *
     * @param connection WebSocket 连接
     * @param statusCode 关闭状态码
     * @param reason 关闭原因
     */
    default void onClose(OkxWebSocketConnection connection, int statusCode, String reason) {
    }

    /**
     * 连接或消息处理出错后回调。
     *
     * @param connection WebSocket 连接，连接建立前可能为 null
     * @param error 异常
     */
    default void onError(OkxWebSocketConnection connection, Throwable error) {
    }
}
