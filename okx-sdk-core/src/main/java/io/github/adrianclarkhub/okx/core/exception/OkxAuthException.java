package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX 鉴权异常。
 *
 * <p>用于表示 API Key、签名、Passphrase、时间戳、权限等鉴权相关错误。</p>
 */
public class OkxAuthException extends OkxApiException {

    /**
     * 创建 OKX 鉴权异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     */
    public OkxAuthException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
        super(message, rawCode, okxMessage, httpStatus, requestPath);
    }
}
