package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX 网络异常。
 *
 * <p>用于表示网络连接、DNS、超时、代理连接等 HTTP 传输层问题。</p>
 */
public class OkxNetworkException extends OkxException {

    /**
     * 创建 OKX 网络异常。
     *
     * @param message 英文异常消息
     */
    public OkxNetworkException(String message) {
        super(message);
    }

    /**
     * 创建 OKX 网络异常。
     *
     * @param message 英文异常消息
     * @param cause 原始异常
     */
    public OkxNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
