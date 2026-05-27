package io.github.adrianclarkhub.okx.rest.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClient;
import io.github.adrianclarkhub.okx.rest.common.OkxRestResponse;
import io.github.adrianclarkhub.okx.rest.status.enums.StatusMaintenanceStateEnum;
import io.github.adrianclarkhub.okx.rest.status.request.StatusRequest;
import io.github.adrianclarkhub.okx.rest.status.response.StatusResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 系统状态客户端测试。
 *
 * <p>验证 StatusClient 对 GET /api/v5/system/status 的响应解析和请求参数传递。</p>
 */
class StatusClientTest {

    @Test
    void shouldDeserializeStatusResponseJson() throws Exception {
        // Given：OKX Status API 返回的典型 JSON 响应。
        String json = "{\"code\":\"0\",\"msg\":\"\",\"data\":[{\"begin\":\"1672823400000\",\"end\":\"1672823520000\",\"href\":\"\",\"preOpenBegin\":\"\",\"scheDesc\":\"\",\"serviceType\":\"8\",\"state\":\"completed\",\"maintType\":\"1\",\"env\":\"1\",\"system\":\"unified\",\"title\":\"Trading account system upgrade (in batches of accounts)\"}]}";
        ObjectMapper objectMapper = new ObjectMapper();

        // When：反序列化为 SDK 通用响应对象。
        OkxRestResponse<StatusResponse> response = objectMapper.readValue(json, new TypeReference<OkxRestResponse<StatusResponse>>() {
        });

        // Then：确认关键字段与 OKX 原始响应一致。
        assertEquals("0", response.getCode(), "Response code should be parsed.");
        assertNotNull(response.getData(), "Response data should be parsed.");
        assertEquals(1, response.getData().size(), "Response data size should be parsed.");
        assertEquals("completed", response.getData().get(0).getState(), "Status state should be parsed.");
        assertEquals("8", response.getData().get(0).getServiceType(), "Service type should be parsed.");
        assertEquals("unified", response.getData().get(0).getSystem(), "System should be parsed.");
    }

    @Test
    void shouldCallRestClientWithStatusPathAndStateQuery() {
        // Given：使用可记录请求路径和查询参数的测试 REST 客户端。
        RecordingOkxRestClient restClient = new RecordingOkxRestClient();
        StatusClient statusClient = new StatusClient(restClient);

        // When：按 completed 状态查询系统状态。
        List<StatusResponse> responses = statusClient.getStatus(new StatusRequest(StatusMaintenanceStateEnum.COMPLETED));

        // Then：确认 StatusClient 使用正确路径、参数，并返回响应 data。
        assertEquals("/api/v5/system/status", restClient.path, "Status path should match OKX API path.");
        assertEquals("completed", restClient.queryParams.get("state"), "State query param should be passed.");
        assertEquals(1, responses.size(), "Client should return response data list.");
        assertEquals("completed", responses.get(0).getState(), "Client should return parsed status item.");
    }

    private static class RecordingOkxRestClient extends OkxRestClient {

        private String path;

        private Map<String, String> queryParams;

        RecordingOkxRestClient() {
            super(new OkxConfig(), new OkHttpClient(), new ObjectMapper(), "https://www.okx.com");
        }

        @Override
        public <T> OkxRestResponse<T> get(String path, Map<String, String> queryParams, TypeReference<OkxRestResponse<T>> typeReference) {
            this.path = path;
            this.queryParams = queryParams;
            OkxRestResponse<T> response = new OkxRestResponse<>();
            response.setCode("0");
            response.setMsg("");
            StatusResponse item = new StatusResponse();
            item.setState("completed");
            response.setData(java.util.Collections.singletonList((T) item));
            return response;
        }
    }
}
