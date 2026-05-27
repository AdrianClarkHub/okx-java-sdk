package io.github.adrianclarkhub.okx.websocket.status.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * OKX WebSocket Status 频道推送数据。
 *
 * <p>对应 status 频道推送消息 data 数组元素。</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusChannelData {

    private String title;

    private String state;

    private String begin;

    private String end;

    private String preOpenBegin;

    private String href;

    private String serviceType;

    private String system;

    private String scheDesc;

    private String maintType;

    private String env;

    private String ts;

    /**
     * 获取系统维护说明标题。
     *
     * @return 系统维护说明标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置系统维护说明标题。
     *
     * @param title 系统维护说明标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取系统维护状态。
     *
     * @return 系统维护状态，OKX 原始值如 scheduled、ongoing、pre_open、completed、canceled
     */
    public String getState() {
        return state;
    }

    /**
     * 设置系统维护状态。
     *
     * @param state 系统维护状态，OKX 原始值如 scheduled、ongoing、pre_open、completed、canceled
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取系统维护开始时间。
     *
     * @return Unix 时间戳的毫秒数字符串
     */
    public String getBegin() {
        return begin;
    }

    /**
     * 设置系统维护开始时间。
     *
     * @param begin Unix 时间戳的毫秒数字符串
     */
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * 获取交易全面开放时间。
     *
     * @return Unix 时间戳的毫秒数字符串
     */
    public String getEnd() {
        return end;
    }

    /**
     * 设置交易全面开放时间。
     *
     * @param end Unix 时间戳的毫秒数字符串
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * 获取预开放开始时间。
     *
     * @return 预开放开始时间
     */
    public String getPreOpenBegin() {
        return preOpenBegin;
    }

    /**
     * 设置预开放开始时间。
     *
     * @param preOpenBegin 预开放开始时间
     */
    public void setPreOpenBegin(String preOpenBegin) {
        this.preOpenBegin = preOpenBegin;
    }

    /**
     * 获取系统维护详情链接。
     *
     * @return 系统维护详情链接
     */
    public String getHref() {
        return href;
    }

    /**
     * 设置系统维护详情链接。
     *
     * @param href 系统维护详情链接
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * 获取服务类型。
     *
     * @return 服务类型，OKX 原始值如 0、5、6、7、8、9、10、11、99
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务类型。
     *
     * @param serviceType 服务类型，OKX 原始值如 0、5、6、7、8、9、10、11、99
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * 获取系统类型。
     *
     * @return 系统类型，OKX 原始值如 unified
     */
    public String getSystem() {
        return system;
    }

    /**
     * 设置系统类型。
     *
     * @param system 系统类型，OKX 原始值如 unified
     */
    public void setSystem(String system) {
        this.system = system;
    }

    /**
     * 获取改期进度说明。
     *
     * @return 改期进度说明
     */
    public String getScheDesc() {
        return scheDesc;
    }

    /**
     * 设置改期进度说明。
     *
     * @param scheDesc 改期进度说明
     */
    public void setScheDesc(String scheDesc) {
        this.scheDesc = scheDesc;
    }

    /**
     * 获取维护类型。
     *
     * @return 维护类型，OKX 原始值 1 表示计划维护，2 表示临时维护，3 表示系统故障
     */
    public String getMaintType() {
        return maintType;
    }

    /**
     * 设置维护类型。
     *
     * @param maintType 维护类型，OKX 原始值 1 表示计划维护，2 表示临时维护，3 表示系统故障
     */
    public void setMaintType(String maintType) {
        this.maintType = maintType;
    }

    /**
     * 获取环境。
     *
     * @return 环境，OKX 原始值 1 表示实盘，2 表示模拟盘
     */
    public String getEnv() {
        return env;
    }

    /**
     * 设置环境。
     *
     * @param env 环境，OKX 原始值 1 表示实盘，2 表示模拟盘
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * 获取事件变更推送时间。
     *
     * @return Unix 时间戳的毫秒数字符串
     */
    public String getTs() {
        return ts;
    }

    /**
     * 设置事件变更推送时间。
     *
     * @param ts Unix 时间戳的毫秒数字符串
     */
    public void setTs(String ts) {
        this.ts = ts;
    }
}
