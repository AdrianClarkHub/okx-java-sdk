package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;

public class OkxWebSocketProtocolClient {

    private final OkxWebSocketJsonCodec jsonCodec;

    public OkxWebSocketProtocolClient() {
        this(OkxObjectMappers.create(null));
    }

    public OkxWebSocketProtocolClient(ObjectMapper objectMapper) {
        this.jsonCodec = new OkxWebSocketJsonCodec(objectMapper == null ? OkxObjectMappers.create(null) : objectMapper);
    }

    public OkxWebSocketNoticeEvent parseNoticeEvent(String json) {
        return jsonCodec.fromJson(json, OkxWebSocketNoticeEvent.class, "Failed to parse OKX WebSocket notice event.");
    }

    public OkxWebSocketChannelConnectionEvent parseChannelConnectionEvent(String json) {
        return jsonCodec.fromJson(json, OkxWebSocketChannelConnectionEvent.class,
                "Failed to parse OKX WebSocket channel connection event.");
    }
}
