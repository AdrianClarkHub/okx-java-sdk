package io.github.adrianclarkhub.okx.rest.support.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 公告详情。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnouncementDetail {

    private String title;

    private String annType;

    private String businessPTime;

    private String pTime;

    private String url;

    /**
     * 获取公告标题。
     *
     * @return 公告标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置公告标题。
     *
     * @param title 公告标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取公告类型。
     *
     * @return 公告类型
     */
    public String getAnnType() {
        return annType;
    }

    /**
     * 设置公告类型。
     *
     * @param annType 公告类型
     */
    public void setAnnType(String annType) {
        this.annType = annType;
    }

    /**
     * 获取公告页面展示时间。
     *
     * @return Unix 时间戳的毫秒数字符串
     */
    public String getBusinessPTime() {
        return businessPTime;
    }

    /**
     * 设置公告页面展示时间。
     *
     * @param businessPTime Unix 时间戳的毫秒数字符串
     */
    public void setBusinessPTime(String businessPTime) {
        this.businessPTime = businessPTime;
    }

    /**
     * 获取公告首次实际发布时间。
     *
     * @return Unix 时间戳的毫秒数字符串
     */
    public String getPTime() {
        return pTime;
    }

    /**
     * 设置公告首次实际发布时间。
     *
     * @param pTime Unix 时间戳的毫秒数字符串
     */
    public void setPTime(String pTime) {
        this.pTime = pTime;
    }

    /**
     * 获取公告链接。
     *
     * @return 公告链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置公告链接。
     *
     * @param url 公告链接
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
