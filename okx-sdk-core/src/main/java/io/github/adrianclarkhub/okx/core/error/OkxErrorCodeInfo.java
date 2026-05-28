package io.github.adrianclarkhub.okx.core.error;

/**
 * OKX 错误码目录项。
 */
public class OkxErrorCodeInfo {

    private final OkxErrorTransportEnum transport;

    private final String category;

    private final String section;

    private final OkxErrorCodeEnum errorCode;

    private final Integer httpStatus;

    private final String messageZhCn;

    private final String messageEnUs;

    private final OkxErrorClassificationEnum classification;

    /**
     * 创建 OKX 错误码目录项。
     *
     * @param transport 来源协议
     * @param category OKX 文档三级分类
     * @param section OKX 文档四级分类
     * @param errorCode 错误码
     * @param httpStatus HTTP 状态码，WebSocket 错误码和关闭帧可为空
     * @param messageZhCn 中文文档错误提示
     * @param classification SDK 错误分类
     */
    public OkxErrorCodeInfo(OkxErrorTransportEnum transport, String category, String section, OkxErrorCodeEnum errorCode,
                            Integer httpStatus, String messageZhCn, OkxErrorClassificationEnum classification) {
        this(transport, category, section, errorCode, httpStatus, messageZhCn, null, classification);
    }

    /**
     * 创建 OKX 错误码目录项。
     *
     * @param transport 来源协议
     * @param category OKX 文档三级分类
     * @param section OKX 文档四级分类
     * @param errorCode 错误码
     * @param httpStatus HTTP 状态码，WebSocket 错误码和关闭帧可为空
     * @param messageZhCn 中文文档错误提示
     * @param messageEnUs 英文错误提示
     * @param classification SDK 错误分类
     */
    public OkxErrorCodeInfo(OkxErrorTransportEnum transport, String category, String section, OkxErrorCodeEnum errorCode,
                            Integer httpStatus, String messageZhCn, String messageEnUs,
                            OkxErrorClassificationEnum classification) {
        this.transport = transport;
        this.category = category;
        this.section = section;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.messageZhCn = messageZhCn;
        this.messageEnUs = messageEnUs == null || messageEnUs.isEmpty()
                ? buildDefaultEnglishMessage(errorCode, classification)
                : messageEnUs;
        this.classification = classification;
    }

    /**
     * 获取来源协议。
     *
     * @return 来源协议
     */
    public OkxErrorTransportEnum getTransport() {
        return transport;
    }

    /**
     * 获取 OKX 文档三级分类。
     *
     * @return 文档三级分类
     */
    public String getCategory() {
        return category;
    }

    /**
     * 获取 OKX 文档四级分类。
     *
     * @return 文档四级分类
     */
    public String getSection() {
        return section;
    }

    /**
     * 获取错误码枚举。
     *
     * @return 错误码枚举
     */
    public OkxErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    public String getCode() {
        return errorCode.getCode();
    }

    /**
     * 获取 HTTP 状态码。
     *
     * @return HTTP 状态码，WebSocket 错误码和关闭帧可为空
     */
    public Integer getHttpStatus() {
        return httpStatus;
    }

    /**
     * 获取中文文档错误提示。
     *
     * @return 中文文档错误提示
     */
    public String getMessageZhCn() {
        return messageZhCn;
    }

    /**
     * 获取英文错误提示。
     *
     * <p>当尚未接入 OKX 英文文档原文时，该字段返回稳定的英文 SDK 提示。</p>
     *
     * @return 英文错误提示
     */
    public String getMessageEnUs() {
        return messageEnUs;
    }

    /**
     * 获取 SDK 错误分类。
     *
     * @return SDK 错误分类
     */
    public OkxErrorClassificationEnum getClassification() {
        return classification;
    }

    /**
     * 判断目录项是否来自 REST API。
     *
     * @return 来自 REST API 时返回 true
     */
    public boolean isRestApi() {
        return OkxErrorTransportEnum.REST_API.equals(transport);
    }

    /**
     * 判断目录项是否来自 WebSocket。
     *
     * @return 来自 WebSocket 时返回 true
     */
    public boolean isWebSocket() {
        return OkxErrorTransportEnum.WEBSOCKET.equals(transport);
    }

    private static String buildDefaultEnglishMessage(OkxErrorCodeEnum errorCode, OkxErrorClassificationEnum classification) {
        String type;
        if (OkxErrorClassificationEnum.AUTHENTICATION.equals(classification)) {
            type = "authentication";
        } else if (OkxErrorClassificationEnum.RATE_LIMIT.equals(classification)) {
            type = "rate limit";
        } else if (OkxErrorClassificationEnum.VALIDATION.equals(classification)) {
            type = "validation";
        } else if (OkxErrorClassificationEnum.SERVER.equals(classification)) {
            type = "server";
        } else if (OkxErrorClassificationEnum.WEBSOCKET_CLOSE.equals(classification)) {
            type = "WebSocket close frame";
        } else if (OkxErrorClassificationEnum.SUCCESS.equals(classification)) {
            type = "success";
        } else {
            type = "business";
        }
        return "Documented OKX " + type + " code " + errorCode.getCode() + ".";
    }
}
