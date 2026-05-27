package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * OKX WebSocket 操作请求。
 *
 * @param <T> 频道参数类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OkxWebSocketRequest<T extends OkxWebSocketChannelArg> {

    private String id;

    private String op;

    private List<T> args;

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
     * @param id 消息唯一标识，长度应为 1 到 32 位
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取操作类型。
     *
     * @return 操作类型，OKX 原始值如 subscribe、unsubscribe
     */
    public String getOp() {
        return op;
    }

    /**
     * 设置操作类型。
     *
     * @param op 操作类型，OKX 原始值如 subscribe、unsubscribe
     */
    public void setOp(String op) {
        this.op = op;
    }

    /**
     * 获取频道参数列表。
     *
     * @return 频道参数列表
     */
    public List<T> getArgs() {
        return args;
    }

    /**
     * 设置频道参数列表。
     *
     * @param args 频道参数列表
     */
    public void setArgs(List<T> args) {
        this.args = args;
    }
}
