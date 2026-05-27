package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX WebSocket 异常。
 *
 * <p>用于表示 WebSocket 连接、订阅、取消订阅、心跳、重连和消息处理相关错误。</p>
 */
public class OkxWebSocketException extends OkxException {

    /**
     * 创建 OKX WebSocket 异常。
     *
     * @param message 英文异常消息
     */
    public OkxWebSocketException(String message) {
        super(message);
    }

    /**
     * 创建 OKX WebSocket 异常。
     *
     * @param message 英文异常消息
     * @param cause 原始异常
     */
    public OkxWebSocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
