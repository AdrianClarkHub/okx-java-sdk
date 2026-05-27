package io.github.adrianclarkhub.okx.rest.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * OKX REST API 通用响应包装。
 *
 * @param <T> data 数组元素类型
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OkxRestResponse<T> {

    private String code;

    private String msg;

    private List<T> data;

    /**
     * 获取 OKX 响应码。
     *
     * @return OKX 响应码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置 OKX 响应码。
     *
     * @param code OKX 响应码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取 OKX 响应消息。
     *
     * @return OKX 响应消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置 OKX 响应消息。
     *
     * @param msg OKX 响应消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取响应数据。
     *
     * @return 响应数据
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 设置响应数据。
     *
     * @param data 响应数据
     */
    public void setData(List<T> data) {
        this.data = data;
    }
}
