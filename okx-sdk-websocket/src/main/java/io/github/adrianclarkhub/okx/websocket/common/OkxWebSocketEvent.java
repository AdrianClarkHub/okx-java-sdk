package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * OKX WebSocket 事件响应。
 *
 * <p>用于表示 subscribe、unsubscribe 和 error 等事件返回。</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkxWebSocketEvent<T extends OkxWebSocketChannelArg> {

    private String id;

    private String event;

    private T arg;

    private String code;

    private String msg;

    private String connId;

    /**
     * 获取消息唯一标识。
     *
     * @return 消息唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置消息唯一标识。
     *
     * @param id 消息唯一标识
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取事件类型。
     *
     * @return 事件类型，OKX 原始值如 subscribe、unsubscribe、error
     */
    public String getEvent() {
        return event;
    }

    /**
     * 设置事件类型。
     *
     * @param event 事件类型，OKX 原始值如 subscribe、unsubscribe、error
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 获取频道参数。
     *
     * @return 频道参数
     */
    public T getArg() {
        return arg;
    }

    /**
     * 设置频道参数。
     *
     * @param arg 频道参数
     */
    public void setArg(T arg) {
        this.arg = arg;
    }

    /**
     * 获取错误码。
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误码。
     *
     * @param code 错误码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取错误消息。
     *
     * @return 错误消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置错误消息。
     *
     * @param msg 错误消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取 WebSocket 连接 ID。
     *
     * @return WebSocket 连接 ID
     */
    public String getConnId() {
        return connId;
    }

    /**
     * 设置 WebSocket 连接 ID。
     *
     * @param connId WebSocket 连接 ID
     */
    public void setConnId(String connId) {
        this.connId = connId;
    }
}
