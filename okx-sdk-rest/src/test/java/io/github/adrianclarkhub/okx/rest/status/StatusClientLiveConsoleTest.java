package io.github.adrianclarkhub.okx.rest.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxNetworkException;
import io.github.adrianclarkhub.okx.core.exception.OkxRateLimitException;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClient;
import io.github.adrianclarkhub.okx.rest.status.enums.StatusMaintenanceStateEnum;
import io.github.adrianclarkhub.okx.rest.status.request.StatusRequest;
import io.github.adrianclarkhub.okx.rest.status.response.StatusResponse;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 系统状态公网 Live Test。
 *
 * <p>该测试会真实访问 OKX 公网 REST API，默认关闭。需要通过本地配置文件或环境变量显式启用。</p>
 */
@EnabledIf("liveTestsEnabled")
class StatusClientLiveConsoleTest {

    private static final String DEFAULT_BASE_URL = "https://www.okx.com";

    private static final String LOCAL_PROPERTIES = "okx-live.local.properties";

    private static final Properties LIVE_PROPERTIES = loadLiveProperties();

    @Test
    void shouldFetchStatusFromOkxPublicRestApi() {
        // Given：构造访问 OKX 公网的匿名 REST 客户端。
        String baseUrl = getConfigValue("OKX_REST_BASE_URL", "okx.rest.base-url", DEFAULT_BASE_URL);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        StatusClient statusClient = new StatusClient(new OkxRestClient(new OkxConfig(), httpClient, new ObjectMapper(), baseUrl));

        // When：真实请求系统维护状态接口。
        List<StatusResponse> responses;
        try {
            responses = statusClient.getStatus(new StatusRequest(StatusMaintenanceStateEnum.COMPLETED));
        } catch (OkxRateLimitException e) {
            Assumptions.abort("OKX live API rate limited this run: " + e.getMessage());
            return;
        } catch (OkxNetworkException e) {
            Assumptions.abort("OKX live API network unavailable this run: " + e.getMessage());
            return;
        }

        // Then：确认公网响应可被 SDK 正常接收和解析。
        assertNotNull(responses, "Live status response should not be null.");
        System.out.println("OKX Live API: GET /api/v5/system/status");
        System.out.println("Base URL: " + baseUrl);
        System.out.println("Status item count: " + responses.size());
        if (!responses.isEmpty()) {
            StatusResponse first = responses.get(0);
            System.out.println("First status state: " + first.getState());
            System.out.println("First status title: " + first.getTitle());
        }
    }

    static boolean liveTestsEnabled() {
        return Boolean.parseBoolean(getConfigValue("OKX_LIVE_TESTS", "okx.live.enabled", "false"));
    }

    private static String getConfigValue(String environmentName, String propertyName, String defaultValue) {
        String value = System.getenv(environmentName);
        if (value == null || value.trim().isEmpty()) {
            value = getLiveProperty(propertyName);
        }
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    private static String getLiveProperty(String propertyName) {
        String value = LIVE_PROPERTIES.getProperty(propertyName);
        if (value == null) {
            value = LIVE_PROPERTIES.getProperty("\uFEFF" + propertyName);
        }
        return value;
    }

    private static Properties loadLiveProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = StatusClientLiveConsoleTest.class.getClassLoader().getResourceAsStream(LOCAL_PROPERTIES)) {
            if (inputStream != null) {
                properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            }
            Path[] sourcePropertiesCandidates = new Path[] {
                    Paths.get("src", "test", "resources", LOCAL_PROPERTIES),
                    Paths.get("okx-sdk-rest", "src", "test", "resources", LOCAL_PROPERTIES)
            };
            for (Path sourceProperties : sourcePropertiesCandidates) {
                if (!Files.isRegularFile(sourceProperties)) {
                    continue;
                }
                try (InputStream sourceInputStream = Files.newInputStream(sourceProperties)) {
                    properties.load(new InputStreamReader(sourceInputStream, StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load OKX live test properties.", e);
        }
        return properties;
    }
}
