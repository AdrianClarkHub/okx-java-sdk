package io.github.adrianclarkhub.okx.websocket.common;

import io.github.adrianclarkhub.okx.core.enums.OkxEnum;

/**
 * OKX WebSocket 操作类型枚举。
 */
public enum OkxWebSocketOperationEnum implements OkxEnum {

    LOGIN("login"),

    /**
     * 订阅频道，对应 OKX 原始值 {@code subscribe}。
     */
    SUBSCRIBE("subscribe"),

    /**
     * 取消订阅频道，对应 OKX 原始值 {@code unsubscribe}。
     */
    UNSUBSCRIBE("unsubscribe");

    private final String code;

    OkxWebSocketOperationEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
