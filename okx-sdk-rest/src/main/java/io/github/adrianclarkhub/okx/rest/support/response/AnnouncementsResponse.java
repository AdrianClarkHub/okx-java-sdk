package io.github.adrianclarkhub.okx.rest.support.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 公告响应。
 *
 * <p>对应 OKX REST API：GET /api/v5/support/announcements 的 data 数组元素。</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnouncementsResponse {

    private String totalPage;

    private List<AnnouncementDetail> details;

    /**
     * 获取总页数。
     *
     * @return 总页数
     */
    public String getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数。
     *
     * @param totalPage 总页数
     */
    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 获取公告列表。
     *
     * @return 公告列表
     */
    public List<AnnouncementDetail> getDetails() {
        return details;
    }

    /**
     * 设置公告列表。
     *
     * @param details 公告列表
     */
    public void setDetails(List<AnnouncementDetail> details) {
        this.details = details;
    }
}
