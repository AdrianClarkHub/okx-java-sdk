package io.github.adrianclarkhub.okx.core.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;

/**
 * OKX JSON 映射器工厂。
 */
public final class OkxObjectMappers {

    private OkxObjectMappers() {
    }

    /**
     * 根据 SDK 配置创建 ObjectMapper。
     *
     * @param config SDK 根配置
     * @return ObjectMapper
     */
    public static ObjectMapper create(OkxConfig config) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
