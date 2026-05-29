package io.github.adrianclarkhub.okx.spring.autoconfigure;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClient;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClients;
import io.github.adrianclarkhub.okx.rest.status.StatusClient;
import io.github.adrianclarkhub.okx.rest.support.SupportClient;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OKX SDK Spring Boot 自动配置。
 */
@Configuration
@ConditionalOnClass(OkxConfig.class)
@EnableConfigurationProperties(OkxProperties.class)
public class OkxAutoConfiguration {

    /**
     * 注册 SDK 根配置 Bean。
     *
     * @param properties Spring 配置属性
     * @return SDK 根配置
     */
    @Bean
    @ConditionalOnMissingBean
    public OkxConfig okxConfig(OkxProperties properties) {
        return properties.toOkxConfig();
    }

    /**
     * 注册 REST 底层客户端 Bean。
     *
     * @param okxConfig SDK 根配置
     * @return REST 底层客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public OkxRestClient okxRestClient(OkxConfig okxConfig) {
        return OkxRestClients.create(okxConfig);
    }

    /**
     * 注册 WebSocket 底层客户端 Bean。
     *
     * @param okxConfig SDK 根配置
     * @return WebSocket 底层客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public OkxWebSocketClient okxWebSocketClient(OkxConfig okxConfig) {
        return new OkxWebSocketClient(okxConfig);
    }

    /**
     * 注册系统状态 REST 客户端 Bean。
     *
     * @param okxRestClient REST 底层客户端
     * @return 系统状态客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public StatusClient okxStatusClient(OkxRestClient okxRestClient) {
        return new StatusClient(okxRestClient);
    }

    /**
     * 注册公告 REST 客户端 Bean。
     *
     * @param okxRestClient REST 底层客户端
     * @return 公告客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public SupportClient okxSupportClient(OkxRestClient okxRestClient) {
        return new SupportClient(okxRestClient);
    }
}
