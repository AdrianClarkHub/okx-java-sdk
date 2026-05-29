package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.error.OkxErrorClassificationEnum;
import io.github.adrianclarkhub.okx.core.error.OkxErrorCodeCatalog;
import io.github.adrianclarkhub.okx.core.error.OkxErrorCodeInfo;
import io.github.adrianclarkhub.okx.core.error.OkxErrorTransportEnum;
import io.github.adrianclarkhub.okx.core.exception.OkxApiException;
import io.github.adrianclarkhub.okx.core.exception.OkxApiExceptionFactory;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;

public class OkxWebSocketProtocolClient {

    private final OkxWebSocketJsonCodec jsonCodec;

    private final ObjectMapper objectMapper;

    public OkxWebSocketProtocolClient() {
        this(OkxObjectMappers.create(null));
    }

    public OkxWebSocketProtocolClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper == null ? OkxObjectMappers.create(null) : objectMapper;
        this.jsonCodec = new OkxWebSocketJsonCodec(this.objectMapper);
    }

    public OkxWebSocketNoticeEvent parseNoticeEvent(String json) {
        return jsonCodec.fromJson(json, OkxWebSocketNoticeEvent.class, "Failed to parse OKX WebSocket notice event.");
    }

    public OkxWebSocketChannelConnectionEvent parseChannelConnectionEvent(String json) {
        return jsonCodec.fromJson(json, OkxWebSocketChannelConnectionEvent.class,
                "Failed to parse OKX WebSocket channel connection event.");
    }

    public OkxApiException parseErrorEvent(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            if (root == null || !root.has("event") || !"error".equals(root.get("event").asText())) {
                return null;
            }
            String code = root.has("code") ? root.get("code").asText() : null;
            String msg = root.has("msg") ? root.get("msg").asText() : null;
            return OkxApiExceptionFactory.create(code, msg, null, "websocket", OkxErrorTransportEnum.WEBSOCKET);
        } catch (Exception e) {
            return null;
        }
    }

    public OkxApiException parseCloseFrame(int statusCode, String reason) {
        if (statusCode <= 0) {
            return null;
        }
        String code = String.valueOf(statusCode);
        OkxErrorCodeInfo closeFrame = OkxErrorCodeCatalog.find(code, OkxErrorTransportEnum.WEBSOCKET).orElse(null);
        if (closeFrame == null || !OkxErrorClassificationEnum.WEBSOCKET_CLOSE.equals(closeFrame.getClassification())) {
            return null;
        }
        return OkxApiExceptionFactory.create(code, reason, null, "websocket-close", OkxErrorTransportEnum.WEBSOCKET);
    }
}
