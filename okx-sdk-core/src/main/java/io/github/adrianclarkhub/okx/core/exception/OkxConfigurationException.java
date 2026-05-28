package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX SDK 配置异常。
 *
 * <p>用于表示 SDK 本地配置缺失、格式错误或组合不合法。</p>
 */
public class OkxConfigurationException extends OkxException {

    /**
     * 创建配置异常。
     *
     * @param message 英文异常消息
     */
    public OkxConfigurationException(String message) {
        super(message);
    }

    /**
     * 创建配置异常。
     *
     * @param message 英文异常消息
     * @param cause 原始异常
     */
    public OkxConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
