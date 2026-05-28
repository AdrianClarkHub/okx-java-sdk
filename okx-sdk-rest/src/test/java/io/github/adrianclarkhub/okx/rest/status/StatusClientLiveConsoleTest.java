package io.github.adrianclarkhub.okx.rest.status;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfigLoader;
import io.github.adrianclarkhub.okx.core.exception.OkxNetworkException;
import io.github.adrianclarkhub.okx.core.exception.OkxRateLimitException;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClients;
import io.github.adrianclarkhub.okx.rest.status.enums.StatusMaintenanceStateEnum;
import io.github.adrianclarkhub.okx.rest.status.request.StatusRequest;
import io.github.adrianclarkhub.okx.rest.status.response.StatusResponse;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 系统状态公网 Live Test。
 *
 * <p>使用统一的 {@link OkxConfigLoader} 加载 {@code okx.*} 配置，默认关闭。</p>
 */
@EnabledIf("liveTestsEnabled")
class StatusClientLiveConsoleTest {

    private static final String LOCAL_LIVE_CONFIG = "okx-live.local.properties";

    private static final OkxConfig OKX_CONFIG = loadLiveTestConfig();

    @Test
    void shouldFetchStatusFromOkxPublicRestApi() {
        StatusClient statusClient = new StatusClient(OkxRestClients.create(OKX_CONFIG));

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

        assertNotNull(responses, "Live status response should not be null.");
        System.out.println("OKX Live API: GET /api/v5/system/status");
        System.out.println("Base URL: " + OKX_CONFIG.resolveRestBaseUrl());
        System.out.println("Status item count: " + responses.size());
        if (!responses.isEmpty()) {
            StatusResponse first = responses.get(0);
            System.out.println("First status state: " + first.getState());
            System.out.println("First status title: " + first.getTitle());
        }
    }

    static boolean liveTestsEnabled() {
        return OKX_CONFIG.isLiveTestsEnabled();
    }

    private static OkxConfig loadLiveTestConfig() {
        String explicitConfigFile = System.getProperty(OkxConfigLoader.CONFIG_FILE_PROPERTY);
        if (explicitConfigFile != null && !explicitConfigFile.trim().isEmpty()) {
            return OkxConfigLoader.load();
        }
        String explicitConfigFileEnv = System.getenv(OkxConfigLoader.CONFIG_FILE_ENV);
        if (explicitConfigFileEnv != null && !explicitConfigFileEnv.trim().isEmpty()) {
            return OkxConfigLoader.load();
        }
        return OkxConfigLoader.loadFromClasspath(LOCAL_LIVE_CONFIG);
    }
}
