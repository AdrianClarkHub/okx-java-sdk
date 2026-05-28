package io.github.adrianclarkhub.okx.spring.autoconfigure;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClient;
import io.github.adrianclarkhub.okx.rest.status.StatusClient;
import io.github.adrianclarkhub.okx.rest.support.SupportClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * OKX Spring Boot 自动配置测试。
 */
class OkxAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(OkxAutoConfiguration.class));

    @Test
    void shouldRegisterOkxBeansFromProperties() {
        contextRunner
                .withPropertyValues(
                        "okx.environment=production",
                        "okx.endpoints.rest-base-url=https://www.okx.com",
                        "okx.live.enabled=false"
                )
                .run(context -> {
                    assertNotNull(context.getBean(OkxConfig.class), "OkxConfig bean should be registered.");
                    assertNotNull(context.getBean(OkxRestClient.class), "OkxRestClient bean should be registered.");
                    assertNotNull(context.getBean(StatusClient.class), "StatusClient bean should be registered.");
                    assertNotNull(context.getBean(SupportClient.class), "SupportClient bean should be registered.");
                    assertEquals("https://www.okx.com", context.getBean(OkxConfig.class).resolveRestBaseUrl(),
                            "REST base URL should be bound from okx.endpoints.rest-base-url.");
                });
    }
}
