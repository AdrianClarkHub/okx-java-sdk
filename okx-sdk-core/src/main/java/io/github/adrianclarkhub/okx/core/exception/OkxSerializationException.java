package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX 序列化异常。
 *
 * <p>用于表示 JSON 序列化、反序列化或数据格式转换失败。</p>
 */
public class OkxSerializationException extends OkxException {

    /**
     * 创建 OKX 序列化异常。
     *
     * @param message 英文异常消息
     */
    public OkxSerializationException(String message) {
        super(message);
    }

    /**
     * 创建 OKX 序列化异常。
     *
     * @param message 英文异常消息
     * @param cause 原始异常
     */
    public OkxSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
