package io.github.adrianclarkhub.okx.core.config;

import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import io.github.adrianclarkhub.okx.core.enums.UnknownEnumStrategyEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OKX SDK 配置模型测试。
 *
 * <p>验证根配置、账户配置、HTTP 配置和代理配置的默认值与属性读写行为。</p>
 */
class OkxConfigTest {

    /**
     * 验证 SDK 根配置默认值。
     */
    @Test
    void shouldCreateDefaultRootConfig() {
        OkxConfig config = new OkxConfig();

        assertEquals(OkxEnvironmentEnum.PRODUCTION, config.getEnvironment(), "Default environment should be production.");
        assertEquals(UnknownEnumStrategyEnum.USE_UNKNOWN, config.getUnknownEnumStrategy(), "Default unknown enum strategy should use UNKNOWN.");
        assertNotNull(config.getHttp(), "Default HTTP config should be created.");
        assertNotNull(config.getAccounts(), "Default account list should be created.");
        assertTrue(config.getAccounts().isEmpty(), "Default account list should be empty.");
    }

    /**
     * 验证 SDK 根配置属性可设置。
     */
    @Test
    void shouldSetRootConfigProperties() {
        OkxConfig config = new OkxConfig();
        OkxHttpConfig http = new OkxHttpConfig();
        List<OkxAccountConfig> accounts = new ArrayList<>();
        accounts.add(new OkxAccountConfig());

        config.setEnvironment(OkxEnvironmentEnum.DEMO);
        config.setUnknownEnumStrategy(UnknownEnumStrategyEnum.THROW_EXCEPTION);
        config.setHttp(http);
        config.setAccounts(accounts);

        assertEquals(OkxEnvironmentEnum.DEMO, config.getEnvironment(), "Configured environment should be preserved.");
        assertEquals(UnknownEnumStrategyEnum.THROW_EXCEPTION, config.getUnknownEnumStrategy(), "Configured unknown enum strategy should be preserved.");
        assertSame(http, config.getHttp(), "Configured HTTP config should be preserved.");
        assertSame(accounts, config.getAccounts(), "Configured account list should be preserved.");
    }

    /**
     * 验证 HTTP 配置默认值。
     */
    @Test
    void shouldCreateDefaultHttpConfig() {
        OkxHttpConfig config = new OkxHttpConfig();

        assertEquals(10000, config.getConnectTimeoutMillis(), "Default connect timeout should be 10000 ms.");
        assertEquals(30000, config.getReadTimeoutMillis(), "Default read timeout should be 30000 ms.");
        assertEquals(30000, config.getWriteTimeoutMillis(), "Default write timeout should be 30000 ms.");
        assertEquals(0, config.getMaxRetries(), "Default max retries should be zero.");
        assertNotNull(config.getProxy(), "Default proxy config should be created.");
        assertFalse(config.getProxy().isEnabled(), "Default proxy should be disabled.");
    }

    /**
     * 验证 HTTP 配置属性可设置。
     */
    @Test
    void shouldSetHttpConfigProperties() {
        OkxHttpConfig config = new OkxHttpConfig();
        OkxProxyConfig proxy = new OkxProxyConfig();

        config.setConnectTimeoutMillis(15000);
        config.setReadTimeoutMillis(45000);
        config.setWriteTimeoutMillis(50000);
        config.setMaxRetries(2);
        config.setProxy(proxy);

        assertEquals(15000, config.getConnectTimeoutMillis(), "Configured connect timeout should be preserved.");
        assertEquals(45000, config.getReadTimeoutMillis(), "Configured read timeout should be preserved.");
        assertEquals(50000, config.getWriteTimeoutMillis(), "Configured write timeout should be preserved.");
        assertEquals(2, config.getMaxRetries(), "Configured max retries should be preserved.");
        assertSame(proxy, config.getProxy(), "Configured proxy should be preserved.");
    }

    /**
     * 验证代理配置属性可设置。
     */
    @Test
    void shouldSetProxyConfigProperties() {
        OkxProxyConfig config = new OkxProxyConfig();

        config.setEnabled(true);
        config.setHost("127.0.0.1");
        config.setPort(7890);
        config.setUsername("proxy-user");
        config.setPassword("proxy-password");

        assertTrue(config.isEnabled(), "Proxy should be enabled.");
        assertEquals("127.0.0.1", config.getHost(), "Configured proxy host should be preserved.");
        assertEquals(7890, config.getPort(), "Configured proxy port should be preserved.");
        assertEquals("proxy-user", config.getUsername(), "Configured proxy username should be preserved.");
        assertEquals("proxy-password", config.getPassword(), "Configured proxy password should be preserved.");
    }

    /**
     * 验证账户配置属性可设置。
     */
    @Test
    void shouldSetAccountConfigProperties() {
        OkxAccountConfig config = new OkxAccountConfig();

        config.setName("main");
        config.setApiKey("api-key");
        config.setSecretKey("secret-key");
        config.setPassphrase("passphrase");

        assertEquals("main", config.getName(), "Configured account name should be preserved.");
        assertEquals("api-key", config.getApiKey(), "Configured API key should be preserved.");
        assertEquals("secret-key", config.getSecretKey(), "Configured secret key should be preserved.");
        assertEquals("passphrase", config.getPassphrase(), "Configured passphrase should be preserved.");
    }
}
