package io.github.adrianclarkhub.okx.rest.status;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClient;
import io.github.adrianclarkhub.okx.rest.common.OkxRestResponse;
import io.github.adrianclarkhub.okx.rest.common.OkxRestResponses;
import io.github.adrianclarkhub.okx.rest.status.request.StatusRequest;
import io.github.adrianclarkhub.okx.rest.status.response.StatusResponse;

import java.util.List;

/**
 * OKX 系统状态 REST 客户端。
 *
 * <p>用于访问 GET /api/v5/system/status，查询系统升级和维护事件状态。</p>
 */
public class StatusClient {

    private static final String STATUS_PATH = "/api/v5/system/status";

    private static final TypeReference<OkxRestResponse<StatusResponse>> STATUS_RESPONSE_TYPE =
            new TypeReference<OkxRestResponse<StatusResponse>>() {
            };

    private final OkxRestClient restClient;

    /**
     * 创建系统状态 REST 客户端。
     *
     * @param restClient REST 底层客户端
     */
    public StatusClient(OkxRestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * 获取默认系统升级事件状态。
     *
     * <p>未传 state 时，OKX 默认返回等待中、进行中和预开放的数据。</p>
     *
     * @return 系统升级事件状态列表
     */
    public List<StatusResponse> getStatus() {
        return getStatus(new StatusRequest());
    }

    /**
     * 按条件获取系统升级事件状态。
     *
     * @param request 查询请求
     * @return 系统升级事件状态列表
     */
    public List<StatusResponse> getStatus(StatusRequest request) {
        StatusRequest actualRequest = request == null ? new StatusRequest() : request;
        OkxRestResponse<StatusResponse> response = restClient.get(STATUS_PATH, actualRequest.toQueryParams(), STATUS_RESPONSE_TYPE);
        return OkxRestResponses.dataOrEmpty(response);
    }
}
