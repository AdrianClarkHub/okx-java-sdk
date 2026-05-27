package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX 参数校验异常。
 *
 * <p>用于表示 SDK 本地参数校验失败，或 OKX API 返回参数格式、必填项、参数组合等校验错误。</p>
 */
public class OkxValidationException extends OkxApiException {

    /**
     * 创建 OKX 参数校验异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     */
    public OkxValidationException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
        super(message, rawCode, okxMessage, httpStatus, requestPath);
    }
}
