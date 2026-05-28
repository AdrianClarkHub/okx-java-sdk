package io.github.adrianclarkhub.okx.core.error;

/**
 * OKX 错误码来源协议。
 */
public enum OkxErrorTransportEnum {

    /**
     * REST API 错误码。
     */
    REST_API,

    /**
     * WebSocket 错误码或关闭帧状态码。
     */
    WEBSOCKET
}
