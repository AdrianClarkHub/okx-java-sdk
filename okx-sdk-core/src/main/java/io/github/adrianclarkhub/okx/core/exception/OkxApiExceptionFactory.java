package io.github.adrianclarkhub.okx.core.exception;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * OKX API 异常工厂。
 *
 * <p>根据 OKX 错误码和 HTTP 状态码创建更具体的 SDK 异常类型。</p>
 */
public final class OkxApiExceptionFactory {

    private static final Set<String> AUTH_CODES = new HashSet<>(Arrays.asList(
            "50100", "50101", "50102", "50103", "50104", "50105"
    ));

    private static final Set<String> RATE_LIMIT_CODES = new HashSet<>(Arrays.asList(
            "50011", "50013", "50040", "50061"
    ));

    private static final Set<String> VALIDATION_CODES = new HashSet<>(Arrays.asList(
            "50000", "50002", "50006", "50014", "50015", "50016", "50024", "50025"
    ));

    private static final Set<String> SERVER_CODES = new HashSet<>(Arrays.asList(
            "50001", "50026"
    ));

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
        String mainCode = parseMainCode(rawCode);
        String message = buildMessage(rawCode, okxMessage, httpStatus, requestPath);

        if (isAuthError(mainCode, httpStatus)) {
            return new OkxAuthException(message, rawCode, okxMessage, httpStatus, requestPath);
        }
        if (isRateLimitError(mainCode, httpStatus)) {
            return new OkxRateLimitException(message, rawCode, okxMessage, httpStatus, requestPath);
        }
        if (isValidationError(mainCode, httpStatus)) {
            return new OkxValidationException(message, rawCode, okxMessage, httpStatus, requestPath);
        }
        if (isServerError(mainCode, httpStatus)) {
            return new OkxServerException(message, rawCode, okxMessage, httpStatus, requestPath);
        }
        return new OkxBusinessException(message, rawCode, okxMessage, httpStatus, requestPath);
    }

    private static boolean isAuthError(String mainCode, Integer httpStatus) {
        return AUTH_CODES.contains(mainCode) || Integer.valueOf(401).equals(httpStatus) || Integer.valueOf(403).equals(httpStatus);
    }

    private static boolean isRateLimitError(String mainCode, Integer httpStatus) {
        return RATE_LIMIT_CODES.contains(mainCode) || Integer.valueOf(429).equals(httpStatus);
    }

    private static boolean isValidationError(String mainCode, Integer httpStatus) {
        return VALIDATION_CODES.contains(mainCode) || Integer.valueOf(400).equals(httpStatus);
    }

    private static boolean isServerError(String mainCode, Integer httpStatus) {
        return SERVER_CODES.contains(mainCode) || isHttpServerError(httpStatus);
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

    private static String buildMessage(String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
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
        }
        return builder.toString();
    }
}
