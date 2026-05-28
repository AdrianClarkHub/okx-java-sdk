package io.github.adrianclarkhub.okx.core.config;

import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import io.github.adrianclarkhub.okx.core.exception.OkxConfigurationException;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        properties.setProperty("okx.endpoints.ws-public-url", "wss://example.okx.com/ws/v5/public");
        properties.setProperty("okx.live.enabled", "true");
        properties.setProperty("okx.http.connect-timeout-millis", "15000");
        properties.setProperty("okx.http.proxy.enabled", "true");
        properties.setProperty("okx.http.proxy.host", "127.0.0.1");
        properties.setProperty("okx.http.proxy.port", "7890");

        OkxConfig config = OkxConfigBinder.fromProperties(properties);

        assertEquals(OkxEnvironmentEnum.DEMO, config.getEnvironment(), "Environment should be bound.");
        assertEquals("main", config.getDefaultAccount(), "Default account should be bound.");
        assertEquals("main-key", config.getAccounts().get("main").getApiKey(), "Account API key should be bound.");
        assertEquals("https://example.okx.com", config.resolveRestBaseUrl(), "REST base URL should be bound.");
        assertEquals("wss://example.okx.com/ws/v5/public", config.resolveWsPublicUrl(),
                "WebSocket public URL should be bound.");
        assertTrue(config.isLiveTestsEnabled(), "Live test flag should be bound.");
        assertEquals(15000, config.getHttp().getConnectTimeoutMillis(), "HTTP timeout should be bound.");
        assertTrue(config.getHttp().getProxy().isEnabled(), "Proxy should be bound.");
        assertEquals("127.0.0.1", config.getHttp().getProxy().getHost(), "Proxy host should be bound.");
        assertEquals(7890, config.getHttp().getProxy().getPort(), "Proxy port should be bound.");
    }

    @Test
    void shouldMapSimulatedFlagToDemoEnvironment() {
        Properties properties = new Properties();
        properties.setProperty("okx.simulated", "true");

        OkxConfig config = OkxConfigBinder.fromProperties(properties);

        assertEquals(OkxEnvironmentEnum.DEMO, config.getEnvironment(), "Simulated flag should map to demo environment.");
    }

    @Test
    void shouldBindBackwardCompatibleEndpointAliases() {
        Properties properties = new Properties();
        properties.setProperty("okx.rest.base-url", "https://alias.okx.com");
        properties.setProperty("okx.websocket.public-url", "wss://alias.okx.com/ws/v5/public");
        properties.setProperty("okx.websocket.private-url", "wss://alias.okx.com/ws/v5/private");
        properties.setProperty("okx.websocket.business-url", "wss://alias.okx.com/ws/v5/business");

        OkxConfig config = OkxConfigBinder.fromProperties(properties);

        assertEquals("https://alias.okx.com", config.resolveRestBaseUrl(),
                "Legacy REST base URL alias should be supported.");
        assertEquals("wss://alias.okx.com/ws/v5/public", config.resolveWsPublicUrl(),
                "WebSocket public URL alias should be supported.");
        assertEquals("wss://alias.okx.com/ws/v5/private", config.getEndpoints().getWsPrivateUrl(),
                "WebSocket private URL alias should be supported.");
        assertEquals("wss://alias.okx.com/ws/v5/business", config.getEndpoints().getWsBusinessUrl(),
                "WebSocket business URL alias should be supported.");
    }

    @Test
    void shouldRejectInvalidIntegerProperty() {
        Properties properties = new Properties();
        properties.setProperty("okx.http.proxy.port", "not-a-number");

        OkxConfigurationException exception = assertThrows(OkxConfigurationException.class,
                () -> OkxConfigBinder.fromProperties(properties),
                "Invalid integer config should fail fast.");

        assertTrue(exception.getMessage().contains("okx.http.proxy.port"),
                "Exception should identify the invalid property.");
    }
}
