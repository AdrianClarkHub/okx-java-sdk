package io.github.adrianclarkhub.okx.core.exception;

import io.github.adrianclarkhub.okx.core.error.OkxErrorCodeInfo;
import io.github.adrianclarkhub.okx.core.error.OkxErrorClassificationEnum;

/**
 * OKX WebSocket 关闭帧异常。
 *
 * <p>用于表示 OKX WebSocket 连接被服务端以文档化状态码关闭的场景。</p>
 */
public class OkxWebSocketCloseException extends OkxApiException {

    /**
     * 创建 OKX WebSocket 关闭帧异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     * @param errorCodeInfo 错误码目录项
     */
    public OkxWebSocketCloseException(String message, String rawCode, String okxMessage, Integer httpStatus,
                                      String requestPath, OkxErrorCodeInfo errorCodeInfo) {
        super(message, rawCode, okxMessage, httpStatus, requestPath, errorCodeInfo);
    }

    public OkxWebSocketCloseException(String message, String rawCode, String okxMessage, Integer httpStatus,
                                      String requestPath, OkxErrorCodeInfo errorCodeInfo,
                                      OkxErrorClassificationEnum errorClassification) {
        super(message, rawCode, okxMessage, httpStatus, requestPath, errorCodeInfo, errorClassification);
    }
}
