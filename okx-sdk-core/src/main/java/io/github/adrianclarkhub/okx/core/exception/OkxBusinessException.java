package io.github.adrianclarkhub.okx.core.exception;

import io.github.adrianclarkhub.okx.core.error.OkxErrorCodeInfo;
import io.github.adrianclarkhub.okx.core.error.OkxErrorClassificationEnum;

/**
 * OKX 业务异常。
 *
 * <p>用于表示 HTTP 请求成功但 OKX 业务响应码表示失败的业务场景。</p>
 */
public class OkxBusinessException extends OkxApiException {

    /**
     * 创建 OKX 业务异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     */
    public OkxBusinessException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
        super(message, rawCode, okxMessage, httpStatus, requestPath);
    }

    public OkxBusinessException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath,
                                OkxErrorCodeInfo errorCodeInfo) {
        super(message, rawCode, okxMessage, httpStatus, requestPath, errorCodeInfo);
    }

    public OkxBusinessException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath,
                                OkxErrorCodeInfo errorCodeInfo, OkxErrorClassificationEnum errorClassification) {
        super(message, rawCode, okxMessage, httpStatus, requestPath, errorCodeInfo, errorClassification);
    }
}
