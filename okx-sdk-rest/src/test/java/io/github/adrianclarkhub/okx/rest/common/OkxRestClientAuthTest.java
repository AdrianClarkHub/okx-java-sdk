package io.github.adrianclarkhub.okx.rest.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.auth.OkxAuthHeaders;
import io.github.adrianclarkhub.okx.core.auth.OkxSigner;
import io.github.adrianclarkhub.okx.core.auth.OkxTimestampProvider;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import io.github.adrianclarkhub.okx.core.exception.OkxConfigurationException;
import io.github.adrianclarkhub.okx.core.exception.OkxNetworkException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OkxRestClientAuthTest {

    @Test
    void shouldSignPrivateGetRequestWithQueryString() {
        RecordingInterceptor interceptor = new RecordingInterceptor();
        OkxRestClient client = createClient(interceptor, createConfig(OkxEnvironmentEnum.PRODUCTION));

        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        queryParams.put("ccy", "BTC");

        client.getPrivate("/api/v5/account/balance", queryParams, responseType());

        Request request = interceptor.getRequest();
        assertNotNull(request, "Request should be captured.");
        assertEquals("GET", request.method(), "HTTP method should be GET.");
        assertNull(request.header("Content-Type"), "GET request should not send JSON Content-Type.");
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

    @Test
    void shouldUseAccountLevelSimulatedFlagForPrivateRequest() throws Exception {
        RecordingInterceptor interceptor = new RecordingInterceptor();
        OkxConfig config = createConfig(OkxEnvironmentEnum.PRODUCTION);
        config.getActiveAccount().setSimulated(true);
        OkxRestClient client = createClient(interceptor, config);

        client.postPrivate("/api/v5/account/set-leverage", "{}", responseType());

        assertEquals("1", interceptor.getRequest().header(OkxAuthHeaders.SIMULATED_TRADING),
                "Account-level simulated flag should enable simulated trading header.");
    }

    @Test
    void shouldRejectInvalidRestPathAsSdkConfigurationException() {
        OkxRestClient client = createClient(new RecordingInterceptor(), OkxEnvironmentEnum.PRODUCTION);

        assertThrows(OkxConfigurationException.class,
                () -> client.get("://bad-path", null, responseType()),
                "Invalid REST path should use SDK configuration exception.");
    }

    @Test
    void shouldRetryGetNetworkFailureAccordingToConfig() {
        FailingThenSuccessfulInterceptor interceptor = new FailingThenSuccessfulInterceptor();
        OkxConfig config = createConfig(OkxEnvironmentEnum.PRODUCTION);
        config.getHttp().setMaxRetries(1);
        OkxRestClient client = createClient(interceptor, config);

        client.get("/api/v5/system/status", null, responseType());

        assertEquals(2, interceptor.getCalls(), "GET should retry once after network failure.");
    }

    @Test
    void shouldNotRetryPostNetworkFailure() {
        AlwaysFailingInterceptor interceptor = new AlwaysFailingInterceptor();
        OkxConfig config = createConfig(OkxEnvironmentEnum.PRODUCTION);
        config.getHttp().setMaxRetries(3);
        OkxRestClient client = createClient(interceptor, config);

        assertThrows(OkxNetworkException.class,
                () -> client.post("/api/v5/account/set-leverage", "{}", responseType()),
                "POST should not be retried automatically.");

        assertEquals(1, interceptor.getCalls(), "POST should fail after the first network error.");
    }

    @Test
    void shouldReportNonJsonHttpErrorAsNetworkException() {
        NonJsonErrorInterceptor interceptor = new NonJsonErrorInterceptor();
        OkxRestClient client = createClient(interceptor, OkxEnvironmentEnum.PRODUCTION);

        OkxNetworkException exception = assertThrows(OkxNetworkException.class,
                () -> client.get("/api/v5/system/status", null, responseType()),
                "Non JSON gateway errors should keep HTTP status context.");

        assertTrue(exception.getMessage().contains("HTTP 502"),
                "Exception message should include HTTP status.");
    }

    private static OkxRestClient createClient(Interceptor interceptor, OkxEnvironmentEnum environment) {
        return createClient(interceptor, createConfig(environment));
    }

    private static OkxRestClient createClient(Interceptor interceptor, OkxConfig config) {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        OkxTimestampProvider timestampProvider = new OkxTimestampProvider(
                Clock.fixed(Instant.parse("2020-12-08T09:08:57.715Z"), ZoneOffset.UTC));
        return new OkxRestClient(config, httpClient, new ObjectMapper(), "https://www.okx.com", timestampProvider);
    }

    private static OkxConfig createConfig(OkxEnvironmentEnum environment) {
        OkxConfig config = new OkxConfig();
        config.setApiKey("api-key");
        config.setSecretKey("secret-key");
        config.setPassphrase("passphrase");
        config.setEnvironment(environment);
        config.normalize();
        return config;
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

    private static class FailingThenSuccessfulInterceptor implements Interceptor {

        private int calls;

        @Override
        public Response intercept(Chain chain) throws IOException {
            calls++;
            if (calls == 1) {
                throw new IOException("temporary network failure");
            }
            return success(chain.request());
        }

        int getCalls() {
            return calls;
        }
    }

    private static class AlwaysFailingInterceptor implements Interceptor {

        private int calls;

        @Override
        public Response intercept(Chain chain) throws IOException {
            calls++;
            throw new IOException("network failure");
        }

        int getCalls() {
            return calls;
        }
    }

    private static class NonJsonErrorInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) {
            return new Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(502)
                    .message("Bad Gateway")
                    .body(ResponseBody.create("<html>bad gateway</html>", MediaType.parse("text/html")))
                    .build();
        }
    }

    private static Response success(Request request) {
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create("{\"code\":\"0\",\"msg\":\"\",\"data\":[]}", null))
                .build();
    }
}
