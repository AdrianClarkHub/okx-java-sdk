package io.github.adrianclarkhub.okx.rest.status.request;

import io.github.adrianclarkhub.okx.rest.status.enums.StatusMaintenanceStateEnum;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 获取系统升级事件状态请求。
 *
 * <p>对应 OKX REST API：GET /api/v5/system/status。</p>
 */
public class StatusRequest {

    private StatusMaintenanceStateEnum state;

    /**
     * 创建空请求。
     */
    public StatusRequest() {
    }

    /**
     * 使用系统维护状态创建请求。
     *
     * @param state 系统维护状态
     */
    public StatusRequest(StatusMaintenanceStateEnum state) {
        this.state = state;
    }

    /**
     * 获取系统维护状态。
     *
     * @return 系统维护状态
     */
    public StatusMaintenanceStateEnum getState() {
        return state;
    }

    /**
     * 设置系统维护状态。
     *
     * @param state 系统维护状态
     */
    public void setState(StatusMaintenanceStateEnum state) {
        this.state = state;
    }

    /**
     * 转换为 REST 查询参数。
     *
     * @return 查询参数
     */
    public Map<String, String> toQueryParams() {
        Map<String, String> queryParams = new LinkedHashMap<>();
        if (state != null && !state.isUnknown()) {
            queryParams.put("state", state.getCode());
        }
        return queryParams;
    }
}
