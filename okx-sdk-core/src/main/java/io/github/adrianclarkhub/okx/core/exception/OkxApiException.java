package io.github.adrianclarkhub.okx.core.exception;

/**
 * OKX API 响应异常。
 *
 * <p>当 OKX API 返回非成功业务码时抛出该异常。该异常会保留 OKX 原始错误码、错误子码、HTTP 状态码和请求路径，便于问题排查。</p>
 */
public class OkxApiException extends OkxException {

    private final String rawCode;

    private final String okxCode;

    private final String okxSubCode;

    private final String okxMessage;

    private final Integer httpStatus;

    private final String requestPath;

    /**
     * 创建 OKX API 响应异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     */
    public OkxApiException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath) {
        super(message);
        this.rawCode = rawCode;
        this.okxCode = parseMainCode(rawCode);
        this.okxSubCode = parseSubCode(rawCode);
        this.okxMessage = okxMessage;
        this.httpStatus = httpStatus;
        this.requestPath = requestPath;
    }

    /**
     * 创建 OKX API 响应异常。
     *
     * @param message 英文异常消息
     * @param rawCode OKX 原始错误码
     * @param okxMessage OKX 原始错误消息
     * @param httpStatus HTTP 状态码
     * @param requestPath 请求路径
     * @param cause 原始异常
     */
    public OkxApiException(String message, String rawCode, String okxMessage, Integer httpStatus, String requestPath, Throwable cause) {
        super(message, cause);
        this.rawCode = rawCode;
        this.okxCode = parseMainCode(rawCode);
        this.okxSubCode = parseSubCode(rawCode);
        this.okxMessage = okxMessage;
        this.httpStatus = httpStatus;
        this.requestPath = requestPath;
    }

    /**
     * 获取 OKX 原始错误码。
     *
     * @return OKX 原始错误码
     */
    public String getRawCode() {
        return rawCode;
    }

    /**
     * 获取 OKX 主错误码。
     *
     * @return OKX 主错误码
     */
    public String getOkxCode() {
        return okxCode;
    }

    /**
     * 获取 OKX 错误子码。
     *
     * @return OKX 错误子码，无子码时返回 null
     */
    public String getOkxSubCode() {
        return okxSubCode;
    }

    /**
     * 获取 OKX 原始错误消息。
     *
     * @return OKX 原始错误消息
     */
    public String getOkxMessage() {
        return okxMessage;
    }

    /**
     * 获取 HTTP 状态码。
     *
     * @return HTTP 状态码
     */
    public Integer getHttpStatus() {
        return httpStatus;
    }

    /**
     * 获取请求路径。
     *
     * @return 请求路径
     */
    public String getRequestPath() {
        return requestPath;
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

    private static String parseSubCode(String rawCode) {
        if (rawCode == null || rawCode.isEmpty()) {
            return null;
        }
        int index = rawCode.indexOf('_');
        if (index < 0 || index == rawCode.length() - 1) {
            return null;
        }
        return rawCode.substring(index + 1);
    }
}
