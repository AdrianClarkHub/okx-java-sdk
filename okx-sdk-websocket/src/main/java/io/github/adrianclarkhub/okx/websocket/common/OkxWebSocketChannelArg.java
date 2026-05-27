package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * OKX WebSocket 频道参数。
 *
 * <p>对应订阅请求 args 数组和服务端返回 arg 对象中的公共 channel 字段。</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkxWebSocketChannelArg {

    private String channel;

    /**
     * 创建空频道参数。
     */
    public OkxWebSocketChannelArg() {
    }

    /**
     * 创建频道参数。
     *
     * @param channel 频道名称
     */
    public OkxWebSocketChannelArg(String channel) {
        this.channel = channel;
    }

    /**
     * 获取频道名称。
     *
     * @return 频道名称
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置频道名称。
     *
     * @param channel 频道名称
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
}
