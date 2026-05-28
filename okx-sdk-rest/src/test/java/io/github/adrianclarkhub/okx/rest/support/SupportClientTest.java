package io.github.adrianclarkhub.okx.rest.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClient;
import io.github.adrianclarkhub.okx.rest.common.OkxRestResponse;
import io.github.adrianclarkhub.okx.rest.support.request.AnnouncementsRequest;
import io.github.adrianclarkhub.okx.rest.support.response.AnnouncementTypeResponse;
import io.github.adrianclarkhub.okx.rest.support.response.AnnouncementsResponse;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 公告客户端测试。
 *
 * <p>验证 SupportClient 对公告接口的响应解析和请求参数传递。</p>
 */
class SupportClientTest {

    @Test
    void shouldDeserializeAnnouncementsResponseJson() throws Exception {
        // Given：OKX 公告接口返回的典型 JSON 响应。
        String json = "{\"code\":\"0\",\"msg\":\"\",\"data\":[{\"details\":[{\"annType\":\"announcements-new-listings\",\"title\":\"OKX to list Virtuals Protocol (VIRTUAL) for spot trading\",\"url\":\"https://www.okx.com/help/okx-to-list-virtuals-protocol-virtual-for-spot-trading\",\"pTime\":\"1761620404821\",\"businessPTime\":\"1761620400000\"}],\"totalPage\":\"123\"}]}";
        ObjectMapper objectMapper = new ObjectMapper();

        // When：反序列化为 SDK 通用响应对象。
        OkxRestResponse<AnnouncementsResponse> response =
                objectMapper.readValue(json, new TypeReference<OkxRestResponse<AnnouncementsResponse>>() {
                });

        // Then：确认分页和公告明细字段与 OKX 原始响应一致。
        assertEquals("0", response.getCode(), "Response code should be parsed.");
        assertNotNull(response.getData(), "Response data should be parsed.");
        assertEquals("123", response.getData().get(0).getTotalPage(), "Total page should be parsed.");
        assertEquals("announcements-new-listings", response.getData().get(0).getDetails().get(0).getAnnType(),
                "Announcement type should be parsed.");
        assertEquals("1761620400000", response.getData().get(0).getDetails().get(0).getBusinessPTime(),
                "Business publish time should be parsed.");
    }

    @Test
    void shouldDeserializeAnnouncementTypesResponseJson() throws Exception {
        // Given：OKX 公告类型接口返回的典型 JSON 响应。
        String json = "{\"code\":\"0\",\"msg\":\"\",\"data\":[{\"annType\":\"announcements-new-listings\",\"annTypeDesc\":\"New listings\"}]}";
        ObjectMapper objectMapper = new ObjectMapper();

        // When：反序列化为 SDK 通用响应对象。
        OkxRestResponse<AnnouncementTypeResponse> response =
                objectMapper.readValue(json, new TypeReference<OkxRestResponse<AnnouncementTypeResponse>>() {
                });

        // Then：确认公告类型字段与 OKX 原始响应一致。
        assertEquals("0", response.getCode(), "Response code should be parsed.");
        assertEquals("announcements-new-listings", response.getData().get(0).getAnnType(),
                "Announcement type should be parsed.");
        assertEquals("New listings", response.getData().get(0).getAnnTypeDesc(),
                "Announcement type description should be parsed.");
    }

    @Test
    void shouldCallRestClientWithAnnouncementsPathAndQuery() {
        // Given：使用可记录请求路径和查询参数的测试 REST 客户端。
        RecordingOkxRestClient restClient = new RecordingOkxRestClient();
        SupportClient supportClient = new SupportClient(restClient);

        // When：按公告类型和页码查询公告。
        List<AnnouncementsResponse> responses =
                supportClient.getAnnouncements(new AnnouncementsRequest("announcements-new-listings", "2"));

        // Then：确认 SupportClient 使用正确路径、参数，并返回响应 data。
        assertEquals("/api/v5/support/announcements", restClient.path, "Announcements path should match OKX API path.");
        assertEquals("announcements-new-listings", restClient.queryParams.get("annType"),
                "Announcement type query param should be passed.");
        assertEquals("2", restClient.queryParams.get("page"), "Page query param should be passed.");
        assertEquals(1, responses.size(), "Client should return response data list.");
        assertEquals("123", responses.get(0).getTotalPage(), "Client should return parsed announcement item.");
    }

    @Test
    void shouldCallRestClientWithAnnouncementTypesPath() {
        // Given：使用可记录请求路径和查询参数的测试 REST 客户端。
        RecordingOkxRestClient restClient = new RecordingOkxRestClient();
        SupportClient supportClient = new SupportClient(restClient);

        // When：查询公告类型。
        List<AnnouncementTypeResponse> responses = supportClient.getAnnouncementTypes();

        // Then：确认 SupportClient 使用正确路径，并返回响应 data。
        assertEquals("/api/v5/support/announcement-types", restClient.path,
                "Announcement types path should match OKX API path.");
        assertTrueOrEmpty(restClient.queryParams, "Announcement types query params should be empty.");
        assertEquals(1, responses.size(), "Client should return response data list.");
        assertEquals("announcements-new-listings", responses.get(0).getAnnType(),
                "Client should return parsed announcement type item.");
    }

    private static void assertTrueOrEmpty(Map<String, String> queryParams, String message) {
        assertNotNull(queryParams, "Query params should not be null.");
        assertEquals(Collections.emptyMap(), queryParams, message);
    }

    private static class RecordingOkxRestClient extends OkxRestClient {

        private String path;

        private Map<String, String> queryParams;

        RecordingOkxRestClient() {
            super(new OkxConfig(), new OkHttpClient(), new ObjectMapper());
        }

        @Override
        public <T> OkxRestResponse<T> get(String path, Map<String, String> queryParams,
                                          TypeReference<OkxRestResponse<T>> typeReference) {
            this.path = path;
            this.queryParams = queryParams;
            OkxRestResponse<T> response = new OkxRestResponse<>();
            response.setCode("0");
            response.setMsg("");
            if ("/api/v5/support/announcement-types".equals(path)) {
                AnnouncementTypeResponse item = new AnnouncementTypeResponse();
                item.setAnnType("announcements-new-listings");
                item.setAnnTypeDesc("New listings");
                response.setData(Collections.singletonList((T) item));
                return response;
            }
            AnnouncementsResponse item = new AnnouncementsResponse();
            item.setTotalPage("123");
            response.setData(Collections.singletonList((T) item));
            return response;
        }
    }
}
