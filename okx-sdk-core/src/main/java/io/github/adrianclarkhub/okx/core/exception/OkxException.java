package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX Java SDK 自定义异常基类。
 *
 * <p>SDK 内部所有自定义异常都应继承该异常，便于使用者统一捕获 SDK 异常。</p>
 */
public class OkxException extends RuntimeException {

    /**
     * 创建 SDK 异常。
     *
     * @param message 英文异常消息
     */
    public OkxException(String message) {
        super(message);
    }

    /**
     * 创建 SDK 异常。
     *
     * @param message 英文异常消息
     * @param cause 原始异常
     */
    public OkxException(String message, Throwable cause) {
        super(message, cause);
    }
}
