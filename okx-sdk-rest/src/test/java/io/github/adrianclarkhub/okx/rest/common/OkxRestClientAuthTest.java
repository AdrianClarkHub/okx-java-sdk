package io.github.adrianclarkhub.okx.rest.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.auth.OkxAuthHeaders;
import io.github.adrianclarkhub.okx.core.auth.OkxSigner;
import io.github.adrianclarkhub.okx.core.auth.OkxTimestampProvider;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OkxRestClientAuthTest {

    @Test
    void shouldSignPrivateGetRequestWithQueryString() {
        RecordingInterceptor interceptor = new RecordingInterceptor();
        OkxRestClient client = createClient(interceptor, OkxEnvironmentEnum.PRODUCTION);

        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        queryParams.put("ccy", "BTC");

        client.getPrivate("/api/v5/account/balance", queryParams, responseType());

        Request request = interceptor.getRequest();
        assertNotNull(request, "Request should be captured.");
        assertEquals("GET", request.method(), "HTTP method should be GET.");
        assertEquals("/api/v5/account/balance?ccy=BTC", request.url().encodedPath() + "?" + request.url().encodedQuery(),
                "Query string should be part of request path.");
        assertAuthHeaders(request, "GET", "/api/v5/account/balance?ccy=BTC", "");
    }

    @Test
    void shouldSignPrivatePostRequestWithJsonBodyAndDemoHeader() throws Exception {
        RecordingInterceptor interceptor = new RecordingInterceptor();
        OkxRestClient client = createClient(interceptor, OkxEnvironmentEnum.DEMO);

        Map<String, String> body = new LinkedHashMap<String, String>();
        body.put("instId", "BTC-USDT");
        body.put("lever", "5");
        body.put("mgnMode", "isolated");

        client.postPrivate("/api/v5/account/set-leverage", body, responseType());

        Request request = interceptor.getRequest();
        assertNotNull(request, "Request should be captured.");
        String requestBody = readRequestBody(request);
        assertEquals("POST", request.method(), "HTTP method should be POST.");
        assertEquals("{\"instId\":\"BTC-USDT\",\"lever\":\"5\",\"mgnMode\":\"isolated\"}", requestBody,
                "POST body should be JSON.");
        assertEquals("1", request.header(OkxAuthHeaders.SIMULATED_TRADING), "Demo requests should send simulated header.");
        assertAuthHeaders(request, "POST", "/api/v5/account/set-leverage", requestBody);
    }

    private static OkxRestClient createClient(RecordingInterceptor interceptor, OkxEnvironmentEnum environment) {
        OkxConfig config = new OkxConfig();
        config.setApiKey("api-key");
        config.setSecretKey("secret-key");
        config.setPassphrase("passphrase");
        config.setEnvironment(environment);
        config.normalize();
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        OkxTimestampProvider timestampProvider = new OkxTimestampProvider(
                Clock.fixed(Instant.parse("2020-12-08T09:08:57.715Z"), ZoneOffset.UTC));
        return new OkxRestClient(config, httpClient, new ObjectMapper(), "https://www.okx.com", timestampProvider);
    }

    private static void assertAuthHeaders(Request request, String method, String requestPath, String body) {
        String timestamp = "2020-12-08T09:08:57.715Z";
        assertEquals("api-key", request.header(OkxAuthHeaders.ACCESS_KEY), "API key header should be present.");
        assertEquals("passphrase", request.header(OkxAuthHeaders.ACCESS_PASSPHRASE),
                "Passphrase header should be present.");
        assertEquals(timestamp, request.header(OkxAuthHeaders.ACCESS_TIMESTAMP), "Timestamp header should be present.");
        assertEquals(OkxSigner.sign(timestamp, method, requestPath, body, "secret-key"),
                request.header(OkxAuthHeaders.ACCESS_SIGN), "Signature header should match OKX prehash.");
    }

    private static String readRequestBody(Request request) throws IOException {
        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        return buffer.readUtf8();
    }

    private static TypeReference<OkxRestResponse<Object>> responseType() {
        return new TypeReference<OkxRestResponse<Object>>() {
        };
    }

    private static class RecordingInterceptor implements Interceptor {

        private Request request;

        @Override
        public Response intercept(Chain chain) {
            request = chain.request();
            return new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body(ResponseBody.create("{\"code\":\"0\",\"msg\":\"\",\"data\":[]}", null))
                    .build();
        }

        Request getRequest() {
            return request;
        }
    }
}
