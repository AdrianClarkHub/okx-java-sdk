package io.github.adrianclarkhub.okx.core.exception;

import io.github.adrianclarkhub.okx.core.error.OkxErrorCodeInfo;
import io.github.adrianclarkhub.okx.core.error.OkxErrorClassificationEnum;

/**
 * OKX 服务端异常。
 *
 * <p>用于表示 OKX 服务暂时不可用、系统错误、HTTP 5xx 等服务端问题。</p>
 */
public class OkxServerException extends OkxApiException {

    /**
     * 创建 OKX 服务端异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     */
    public OkxServerException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
        super(message, rawCode, okxMessage, httpStatus, requestPath);
    }

    public OkxServerException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath,
                              OkxErrorCodeInfo errorCodeInfo) {
        super(message, rawCode, okxMessage, httpStatus, requestPath, errorCodeInfo);
    }

    public OkxServerException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath,
                              OkxErrorCodeInfo errorCodeInfo, OkxErrorClassificationEnum errorClassification) {
        super(message, rawCode, okxMessage, httpStatus, requestPath, errorCodeInfo, errorClassification);
    }
}
