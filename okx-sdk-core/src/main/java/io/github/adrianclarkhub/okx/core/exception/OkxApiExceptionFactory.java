package io.github.adrianclarkhub.okx.core.exception;

import io.github.adrianclarkhub.okx.core.error.OkxErrorClassificationEnum;
import io.github.adrianclarkhub.okx.core.error.OkxErrorCodeCatalog;
import io.github.adrianclarkhub.okx.core.error.OkxErrorCodeInfo;
import io.github.adrianclarkhub.okx.core.error.OkxErrorTransportEnum;

import java.util.Optional;

/**
 * OKX API 异常工厂。
 *
 * <p>根据 OKX 错误码和 HTTP 状态码创建更具体的 SDK 异常类型。</p>
 */
public final class OkxApiExceptionFactory {

    private OkxApiExceptionFactory() {
    }

    /**
     * 创建 OKX API 异常。
     *
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     * @return 具体的 OKX API 异常
     */
    public static OkxApiException create(String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
        return create(rawCode, okxMessage, httpStatus, requestPath, null);
    }

    /**
     * 创建 OKX API 异常。
     *
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     * @param transport 来源协议
     * @return 具体的 OKX API 异常
     */
    public static OkxApiException create(String rawCode, String okxMessage, Integer httpStatus, String requestPath,
                                         OkxErrorTransportEnum transport) {
        String mainCode = parseMainCode(rawCode);
        Optional<OkxErrorCodeInfo> errorCodeInfo = transport == null
                ? OkxErrorCodeCatalog.find(rawCode)
                : OkxErrorCodeCatalog.find(rawCode, transport);
        OkxErrorClassificationEnum classification = errorCodeInfo
                .map(OkxErrorCodeInfo::getClassification)
                .orElseGet(() -> fallbackClassification(mainCode, httpStatus));
        String messageEnUs = errorCodeInfo.map(OkxErrorCodeInfo::getMessageEnUs).orElse(null);
        String message = buildMessage(rawCode, okxMessage, messageEnUs, httpStatus, requestPath);
        OkxErrorCodeInfo info = errorCodeInfo.orElse(null);

        if (OkxErrorClassificationEnum.AUTHENTICATION.equals(classification)) {
            return new OkxAuthException(message, rawCode, okxMessage, httpStatus, requestPath, info, classification);
        }
        if (OkxErrorClassificationEnum.RATE_LIMIT.equals(classification)) {
            return new OkxRateLimitException(message, rawCode, okxMessage, httpStatus, requestPath, info, classification);
        }
        if (OkxErrorClassificationEnum.VALIDATION.equals(classification)) {
            return new OkxValidationException(message, rawCode, okxMessage, httpStatus, requestPath, info, classification);
        }
        if (OkxErrorClassificationEnum.SERVER.equals(classification)) {
            return new OkxServerException(message, rawCode, okxMessage, httpStatus, requestPath, info, classification);
        }
        if (OkxErrorClassificationEnum.WEBSOCKET_CLOSE.equals(classification)) {
            return new OkxWebSocketCloseException(message, rawCode, okxMessage, httpStatus, requestPath, info, classification);
        }
        return new OkxBusinessException(message, rawCode, okxMessage, httpStatus, requestPath, info, classification);
    }

    private static OkxErrorClassificationEnum fallbackClassification(String mainCode, Integer httpStatus) {
        if (Integer.valueOf(401).equals(httpStatus) || Integer.valueOf(403).equals(httpStatus)) {
            return OkxErrorClassificationEnum.AUTHENTICATION;
        }
        if (Integer.valueOf(429).equals(httpStatus)) {
            return OkxErrorClassificationEnum.RATE_LIMIT;
        }
        if (Integer.valueOf(400).equals(httpStatus) || Integer.valueOf(405).equals(httpStatus) || Integer.valueOf(410).equals(httpStatus)) {
            return OkxErrorClassificationEnum.VALIDATION;
        }
        if ("50001".equals(mainCode) || "50026".equals(mainCode) || isHttpServerError(httpStatus)) {
            return OkxErrorClassificationEnum.SERVER;
        }
        return OkxErrorClassificationEnum.BUSINESS;
    }

    private static boolean isHttpServerError(Integer httpStatus) {
        return httpStatus != null && httpStatus >= 500 && httpStatus <= 599;
    }

    private static String parseMainCode(String rawCode) {
        if (rawCode == null || rawCode.isEmpty()) {
            return rawCode;
        }
        int index = rawCode.indexOf('_');
        if (index < 0) {
            return rawCode;
        }
        return rawCode.substring(0, index);
    }

    private static String buildMessage(String rawCode, String okxMessage, String messageEnUs, Integer httpStatus, String requestPath) {
        StringBuilder builder = new StringBuilder("OKX API request failed");
        if (rawCode != null && !rawCode.isEmpty()) {
            builder.append(". code=").append(rawCode);
        }
        if (httpStatus != null) {
            builder.append(", httpStatus=").append(httpStatus);
        }
        if (requestPath != null && !requestPath.isEmpty()) {
            builder.append(", path=").append(requestPath);
        }
        if (okxMessage != null && !okxMessage.isEmpty()) {
            builder.append(", message=").append(okxMessage);
        } else if (messageEnUs != null && !messageEnUs.isEmpty()) {
            builder.append(", message=").append(messageEnUs);
        }
        return builder.toString();
    }
}
