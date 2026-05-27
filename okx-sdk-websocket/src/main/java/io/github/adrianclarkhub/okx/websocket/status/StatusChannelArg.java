package io.github.adrianclarkhub.okx.websocket.status;

import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketChannelArg;

/**
 * OKX WebSocket Status 频道参数。
 *
 * <p>对应频道名 {@code status}。</p>
 */
public class StatusChannelArg extends OkxWebSocketChannelArg {

    /**
     * Status 频道名称。
     */
    public static final String CHANNEL = "status";

    /**
     * 创建 Status 频道参数。
     */
    public StatusChannelArg() {
        super(CHANNEL);
    }
}
