package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX 请求限流异常。
 *
 * <p>用于表示请求频率过高、系统繁忙、操作频繁等需要降频或稍后重试的错误。</p>
 */
public class OkxRateLimitException extends OkxApiException {

    /**
     * 创建 OKX 请求限流异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     */
    public OkxRateLimitException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
        super(message, rawCode, okxMessage, httpStatus, requestPath);
    }
}
