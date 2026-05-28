package io.github.adrianclarkhub.okx.core.error;

/**
 * OKX 错误码分类。
 *
 * <p>该分类用于 SDK 异常映射，保留 OKX 原始错误码细节的同时提供稳定的异常语义。</p>
 */
public enum OkxErrorClassificationEnum {

    /**
     * 成功响应。
     */
    SUCCESS,

    /**
     * 鉴权、权限或访问控制错误。
     */
    AUTHENTICATION,

    /**
     * 请求频率或账户限速错误。
     */
    RATE_LIMIT,

    /**
     * 参数、请求格式或不支持的接口错误。
     */
    VALIDATION,

    /**
     * OKX 服务端或系统繁忙错误。
     */
    SERVER,

    /**
     * WebSocket 关闭帧。
     */
    WEBSOCKET_CLOSE,

    /**
     * 业务规则错误。
     */
    BUSINESS
}
