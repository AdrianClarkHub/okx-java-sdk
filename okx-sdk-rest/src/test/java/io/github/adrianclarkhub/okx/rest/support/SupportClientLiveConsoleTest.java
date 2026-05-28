package io.github.adrianclarkhub.okx.rest.support;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfigLoader;
import io.github.adrianclarkhub.okx.core.exception.OkxNetworkException;
import io.github.adrianclarkhub.okx.core.exception.OkxRateLimitException;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClients;
import io.github.adrianclarkhub.okx.rest.support.response.AnnouncementTypeResponse;
import io.github.adrianclarkhub.okx.rest.support.response.AnnouncementsResponse;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 公告公网 Live Test。
 *
 * <p>使用统一的 {@link OkxConfigLoader} 加载 {@code okx.*} 配置，默认关闭。</p>
 */
@EnabledIf("liveTestsEnabled")
class SupportClientLiveConsoleTest {

    private static final String LOCAL_LIVE_CONFIG = "okx-live.local.properties";

    private static final OkxConfig OKX_CONFIG = loadLiveTestConfig();

    @Test
    void shouldFetchAnnouncementsFromOkxPublicRestApi() {
        SupportClient supportClient = new SupportClient(OkxRestClients.create(OKX_CONFIG));

        List<AnnouncementsResponse> responses;
        try {
            responses = supportClient.getAnnouncements();
        } catch (OkxRateLimitException e) {
            Assumptions.abort("OKX live API rate limited this run: " + e.getMessage());
            return;
        } catch (OkxNetworkException e) {
            Assumptions.abort("OKX live API network unavailable this run: " + e.getMessage());
            return;
        }

        assertNotNull(responses, "Live announcements response should not be null.");
        assertFalse(responses.isEmpty(), "Live announcements response should contain at least one page item.");
        assertNotNull(responses.get(0).getDetails(), "Live announcements details should not be null.");
        System.out.println("OKX Live API: GET /api/v5/support/announcements");
        System.out.println("Base URL: " + OKX_CONFIG.resolveRestBaseUrl());
        System.out.println("Announcements page item count: " + responses.size());
        System.out.println("Announcements total page: " + responses.get(0).getTotalPage());
        System.out.println("Announcements detail count: " + responses.get(0).getDetails().size());
        if (!responses.get(0).getDetails().isEmpty()) {
            System.out.println("First announcement type: " + responses.get(0).getDetails().get(0).getAnnType());
            System.out.println("First announcement title: " + responses.get(0).getDetails().get(0).getTitle());
        }
    }

    @Test
    void shouldFetchAnnouncementTypesFromOkxPublicRestApi() {
        SupportClient supportClient = new SupportClient(OkxRestClients.create(OKX_CONFIG));

        List<AnnouncementTypeResponse> responses;
        try {
            responses = supportClient.getAnnouncementTypes();
        } catch (OkxRateLimitException e) {
            Assumptions.abort("OKX live API rate limited this run: " + e.getMessage());
            return;
        } catch (OkxNetworkException e) {
            Assumptions.abort("OKX live API network unavailable this run: " + e.getMessage());
            return;
        }

        assertNotNull(responses, "Live announcement types response should not be null.");
        assertFalse(responses.isEmpty(), "Live announcement types response should contain at least one item.");
        System.out.println("OKX Live API: GET /api/v5/support/announcement-types");
        System.out.println("Base URL: " + OKX_CONFIG.resolveRestBaseUrl());
        System.out.println("Announcement type count: " + responses.size());
        System.out.println("First announcement type: " + responses.get(0).getAnnType());
        System.out.println("First announcement type description: " + responses.get(0).getAnnTypeDesc());
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
