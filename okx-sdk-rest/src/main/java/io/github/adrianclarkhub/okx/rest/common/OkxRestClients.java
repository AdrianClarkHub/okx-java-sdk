package io.github.adrianclarkhub.okx.rest.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.http.OkxHttpClients;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;
import okhttp3.OkHttpClient;

/**
 * OKX REST 客户端工厂。
 *
 * <p>根据统一的 {@link OkxConfig} 创建可复用的 REST 底层客户端。</p>
 */
public final class OkxRestClients {

    private OkxRestClients() {
    }

    /**
     * 根据 SDK 配置创建 REST 底层客户端。
     *
     * @param config SDK 根配置
     * @return REST 底层客户端
     */
    public static OkxRestClient create(OkxConfig config) {
        OkxConfig actualConfig = config == null ? new OkxConfig() : config;
        actualConfig.normalize();
        OkHttpClient httpClient = OkxHttpClients.create(actualConfig.getHttp());
        ObjectMapper objectMapper = OkxObjectMappers.create(actualConfig);
        return new OkxRestClient(actualConfig, httpClient, objectMapper);
    }
}
