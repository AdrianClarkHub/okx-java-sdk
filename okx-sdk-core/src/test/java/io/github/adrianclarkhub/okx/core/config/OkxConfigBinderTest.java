package io.github.adrianclarkhub.okx.core.config;

import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OKX 配置绑定器测试。
 */
class OkxConfigBinderTest {

    @Test
    void shouldBindPropertiesToOkxConfig() {
        Properties properties = new Properties();
        properties.setProperty("okx.environment", "demo");
        properties.setProperty("okx.default-account", "main");
        properties.setProperty("okx.accounts.main.api-key", "main-key");
        properties.setProperty("okx.accounts.main.secret-key", "main-secret");
        properties.setProperty("okx.accounts.main.passphrase", "main-pass");
        properties.setProperty("okx.endpoints.rest-base-url", "https://example.okx.com");
        properties.setProperty("okx.live.enabled", "true");
        properties.setProperty("okx.http.connect-timeout-millis", "15000");

        OkxConfig config = OkxConfigBinder.fromProperties(properties);

        assertEquals(OkxEnvironmentEnum.DEMO, config.getEnvironment(), "Environment should be bound.");
        assertEquals("main", config.getDefaultAccount(), "Default account should be bound.");
        assertEquals("main-key", config.getAccounts().get("main").getApiKey(), "Account API key should be bound.");
        assertEquals("https://example.okx.com", config.resolveRestBaseUrl(), "REST base URL should be bound.");
        assertTrue(config.isLiveTestsEnabled(), "Live test flag should be bound.");
        assertEquals(15000, config.getHttp().getConnectTimeoutMillis(), "HTTP timeout should be bound.");
    }

    @Test
    void shouldMapSimulatedFlagToDemoEnvironment() {
        Properties properties = new Properties();
        properties.setProperty("okx.simulated", "true");

        OkxConfig config = OkxConfigBinder.fromProperties(properties);

        assertEquals(OkxEnvironmentEnum.DEMO, config.getEnvironment(), "Simulated flag should map to demo environment.");
    }
}
