package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * OKX WebSocket 推送消息。
 *
 * @param <A> 频道参数类型
 * @param <D> 推送数据元素类型
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkxWebSocketPushMessage<A extends OkxWebSocketChannelArg, D> {

    private A arg;

    private List<D> data;

    /**
     * 获取频道参数。
     *
     * @return 频道参数
     */
    public A getArg() {
        return arg;
    }

    /**
     * 设置频道参数。
     *
     * @param arg 频道参数
     */
    public void setArg(A arg) {
        this.arg = arg;
    }

    /**
     * 获取推送数据。
     *
     * @return 推送数据
     */
    public List<D> getData() {
        return data;
    }

    /**
     * 设置推送数据。
     *
     * @param data 推送数据
     */
    public void setData(List<D> data) {
        this.data = data;
    }
}
