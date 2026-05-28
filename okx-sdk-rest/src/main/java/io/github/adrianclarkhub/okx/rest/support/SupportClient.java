package io.github.adrianclarkhub.okx.rest.support;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClient;
import io.github.adrianclarkhub.okx.rest.common.OkxRestResponse;
import io.github.adrianclarkhub.okx.rest.common.OkxRestResponses;
import io.github.adrianclarkhub.okx.rest.support.request.AnnouncementsRequest;
import io.github.adrianclarkhub.okx.rest.support.response.AnnouncementTypeResponse;
import io.github.adrianclarkhub.okx.rest.support.response.AnnouncementsResponse;

import java.util.Collections;
import java.util.List;

/**
 * OKX 公告 REST 客户端。
 *
 * <p>用于访问公告和公告类型公共接口。</p>
 */
public class SupportClient {

    private static final String ANNOUNCEMENTS_PATH = "/api/v5/support/announcements";

    private static final String ANNOUNCEMENT_TYPES_PATH = "/api/v5/support/announcement-types";

    private static final TypeReference<OkxRestResponse<AnnouncementsResponse>> ANNOUNCEMENTS_RESPONSE_TYPE =
            new TypeReference<OkxRestResponse<AnnouncementsResponse>>() {
            };

    private static final TypeReference<OkxRestResponse<AnnouncementTypeResponse>> ANNOUNCEMENT_TYPES_RESPONSE_TYPE =
            new TypeReference<OkxRestResponse<AnnouncementTypeResponse>>() {
            };

    private final OkxRestClient restClient;

    /**
     * 创建公告 REST 客户端。
     *
     * @param restClient REST 底层客户端
     */
    public SupportClient(OkxRestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * 获取默认公告列表。
     *
     * @return 公告响应列表
     */
    public List<AnnouncementsResponse> getAnnouncements() {
        return getAnnouncements(new AnnouncementsRequest());
    }

    /**
     * 按条件获取公告列表。
     *
     * @param request 查询请求
     * @return 公告响应列表
     */
    public List<AnnouncementsResponse> getAnnouncements(AnnouncementsRequest request) {
        AnnouncementsRequest actualRequest = request == null ? new AnnouncementsRequest() : request;
        OkxRestResponse<AnnouncementsResponse> response =
                restClient.get(ANNOUNCEMENTS_PATH, actualRequest.toQueryParams(), ANNOUNCEMENTS_RESPONSE_TYPE);
        return OkxRestResponses.dataOrEmpty(response);
    }

    /**
     * 获取公告类型列表。
     *
     * @return 公告类型列表
     */
    public List<AnnouncementTypeResponse> getAnnouncementTypes() {
        OkxRestResponse<AnnouncementTypeResponse> response =
                restClient.get(ANNOUNCEMENT_TYPES_PATH, Collections.emptyMap(), ANNOUNCEMENT_TYPES_RESPONSE_TYPE);
        return OkxRestResponses.dataOrEmpty(response);
    }
}
