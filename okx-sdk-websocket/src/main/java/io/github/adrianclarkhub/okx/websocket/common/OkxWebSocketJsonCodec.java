package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.exception.OkxSerializationException;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;

public class OkxWebSocketJsonCodec {

    private final ObjectMapper objectMapper;

    public OkxWebSocketJsonCodec() {
        this(OkxObjectMappers.create(null));
    }

    public OkxWebSocketJsonCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper == null ? OkxObjectMappers.create(null) : objectMapper;
    }

    public String toJson(Object value, String errorMessage) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException(errorMessage, e);
        }
    }

    public <T> T fromJson(String json, Class<T> type, String errorMessage) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException(errorMessage, e);
        }
    }

    public <T> T fromJson(String json, TypeReference<T> typeReference, String errorMessage) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException(errorMessage, e);
        }
    }
}
