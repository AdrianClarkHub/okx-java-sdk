package io.github.adrianclarkhub.okx.core.http;

import io.github.adrianclarkhub.okx.core.config.OkxHttpConfig;
import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxConfigurationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * OKX HTTP 客户端工厂测试。
 */
class OkxHttpClientsTest {

    @Test
    void shouldCreateClientWithDefaultConfig() {
        assertNotNull(OkxHttpClients.create(null), "Default HTTP client should be created.");
    }

    @Test
    void shouldRejectEnabledProxyWithoutHost() {
        OkxHttpConfig config = new OkxHttpConfig();
        OkxProxyConfig proxy = new OkxProxyConfig();
        proxy.setEnabled(true);
        proxy.setPort(7890);
        config.setProxy(proxy);

        assertThrows(OkxConfigurationException.class, () -> OkxHttpClients.create(config),
                "Enabled proxy requires host.");
    }

    @Test
    void shouldRejectInvalidProxyPort() {
        OkxHttpConfig config = new OkxHttpConfig();
        OkxProxyConfig proxy = new OkxProxyConfig();
        proxy.setEnabled(true);
        proxy.setHost("127.0.0.1");
        proxy.setPort(70000);
        config.setProxy(proxy);

        assertThrows(OkxConfigurationException.class, () -> OkxHttpClients.create(config),
                "Enabled proxy requires valid port.");
    }

    @Test
    void shouldRejectNegativeMaxRetries() {
        OkxHttpConfig config = new OkxHttpConfig();
        config.setMaxRetries(-1);

        assertThrows(OkxConfigurationException.class, () -> OkxHttpClients.create(config),
                "Negative retry count should fail fast.");
    }
}
