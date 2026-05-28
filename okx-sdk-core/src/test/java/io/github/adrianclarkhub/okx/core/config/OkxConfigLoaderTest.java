package io.github.adrianclarkhub.okx.core.config;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * OKX 配置加载器测试。
 */
class OkxConfigLoaderTest {

    @Test
    void shouldLoadConfigFromExplicitPath() throws IOException {
        Path configFile = Files.createTempFile("okx-sdk-test-", ".properties");
        Files.write(configFile, java.util.Arrays.asList(
                "okx.environment=demo",
                "okx.default-account=main",
                "okx.accounts.main.api-key=test-key",
                "okx.accounts.main.secret-key=test-secret",
                "okx.accounts.main.passphrase=test-pass",
                "okx.endpoints.rest-base-url=https://example.okx.com"
        ));

        OkxConfig config = OkxConfigLoader.loadFromPath(configFile);

        assertNotNull(config.getHttp(), "HTTP config should be loaded.");
        assertEquals("https://example.okx.com", config.resolveRestBaseUrl(), "Explicit REST base URL should be loaded.");
        assertEquals("main", config.getDefaultAccount(), "Default account should be loaded.");
    }

    @Test
    void shouldLoadDefaultConfigWhenNoLocalFileExists() {
        OkxConfig config = OkxConfigLoader.load();

        assertNotNull(config.getHttp(), "Default config should always provide HTTP settings.");
        assertEquals(OkxEndpointConfig.DEFAULT_REST_BASE_URL, config.resolveRestBaseUrl(), "Default REST base URL should be used.");
    }

    @Test
    void shouldNotImplicitlyLoadWorkingDirectoryConfigFile() throws IOException {
        Path workingDirectoryConfig = Path.of("okx.properties");
        boolean existedBefore = Files.exists(workingDirectoryConfig);
        byte[] originalContent = existedBefore ? Files.readAllBytes(workingDirectoryConfig) : null;

        try {
            Files.write(workingDirectoryConfig, java.util.Arrays.asList(
                    "okx.live.enabled=true",
                    "okx.endpoints.rest-base-url=https://local-file.example.com"
            ));

            OkxConfig config = OkxConfigLoader.load();

            assertFalse(config.isLiveTestsEnabled(), "Default loader should not implicitly read working directory config.");
            assertEquals(OkxEndpointConfig.DEFAULT_REST_BASE_URL, config.resolveRestBaseUrl(), "Default REST base URL should be preserved.");
        } finally {
            if (existedBefore) {
                Files.write(workingDirectoryConfig, originalContent);
            } else {
                Files.deleteIfExists(workingDirectoryConfig);
            }
        }
    }
}
