package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.exception.OkxSerializationException;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;

public class OkxWebSocketProtocolClient {

    private final ObjectMapper objectMapper;

    public OkxWebSocketProtocolClient() {
        this(OkxObjectMappers.create(null));
    }

    public OkxWebSocketProtocolClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper == null ? OkxObjectMappers.create(null) : objectMapper;
    }

    public OkxWebSocketNoticeEvent parseNoticeEvent(String json) {
        try {
            return objectMapper.readValue(json, OkxWebSocketNoticeEvent.class);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException("Failed to parse OKX WebSocket notice event.", e);
        }
    }

    public OkxWebSocketChannelConnectionEvent parseChannelConnectionEvent(String json) {
        try {
            return objectMapper.readValue(json, OkxWebSocketChannelConnectionEvent.class);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException("Failed to parse OKX WebSocket channel connection event.", e);
        }
    }
}
