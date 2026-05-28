package io.github.adrianclarkhub.okx.rest.support.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 公告类型响应。
 *
 * <p>对应 OKX REST API：GET /api/v5/support/announcement-types 的 data 数组元素。</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnouncementTypeResponse {

    private String annType;

    private String annTypeDesc;

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
     * 获取公告类型描述。
     *
     * @return 公告类型描述
     */
    public String getAnnTypeDesc() {
        return annTypeDesc;
    }

    /**
     * 设置公告类型描述。
     *
     * @param annTypeDesc 公告类型描述
     */
    public void setAnnTypeDesc(String annTypeDesc) {
        this.annTypeDesc = annTypeDesc;
    }
}
