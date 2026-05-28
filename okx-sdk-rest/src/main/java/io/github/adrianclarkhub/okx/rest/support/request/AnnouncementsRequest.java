package io.github.adrianclarkhub.okx.rest.support.request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 获取公告请求。
 *
 * <p>对应 OKX REST API：GET /api/v5/support/announcements。</p>
 */
public class AnnouncementsRequest {

    private String annType;

    private String page;

    /**
     * 创建空请求。
     */
    public AnnouncementsRequest() {
    }

    /**
     * 使用公告类型和页码创建请求。
     *
     * @param annType 公告类型
     * @param page 页码
     */
    public AnnouncementsRequest(String annType, String page) {
        this.annType = annType;
        this.page = page;
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
     * 获取页码。
     *
     * @return 页码
     */
    public String getPage() {
        return page;
    }

    /**
     * 设置页码。
     *
     * @param page 页码
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * 转换为 REST 查询参数。
     *
     * @return 查询参数
     */
    public Map<String, String> toQueryParams() {
        Map<String, String> queryParams = new LinkedHashMap<>();
        if (annType != null && !annType.trim().isEmpty()) {
            queryParams.put("annType", annType.trim());
        }
        if (page != null && !page.trim().isEmpty()) {
            queryParams.put("page", page.trim());
        }
        return queryParams;
    }
}
