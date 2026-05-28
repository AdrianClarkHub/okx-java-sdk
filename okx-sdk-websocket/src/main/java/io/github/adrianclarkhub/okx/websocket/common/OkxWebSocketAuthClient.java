package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.auth.OkxCredentials;
import io.github.adrianclarkhub.okx.core.auth.OkxSigner;
import io.github.adrianclarkhub.okx.core.auth.OkxTimestampProvider;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;

import java.util.Collections;

public class OkxWebSocketAuthClient {

    public static final String LOGIN_METHOD = "GET";

    public static final String LOGIN_REQUEST_PATH = "/users/self/verify";

    private final OkxConfig config;

    private final OkxWebSocketJsonCodec jsonCodec;

    private final OkxTimestampProvider timestampProvider;

    public OkxWebSocketAuthClient(OkxConfig config) {
        this(config, OkxObjectMappers.create(config), new OkxTimestampProvider());
    }

    public OkxWebSocketAuthClient(OkxConfig config, ObjectMapper objectMapper, OkxTimestampProvider timestampProvider) {
        this.config = config;
        this.jsonCodec = new OkxWebSocketJsonCodec(objectMapper == null ? OkxObjectMappers.create(config) : objectMapper);
        this.timestampProvider = timestampProvider == null ? new OkxTimestampProvider() : timestampProvider;
    }

    public OkxWebSocketLoginRequest createLoginRequest() {
        OkxCredentials credentials = OkxCredentials.fromConfig(config);
        String timestamp = timestampProvider.webSocketTimestamp();
        String sign = OkxSigner.sign(timestamp, LOGIN_METHOD, LOGIN_REQUEST_PATH, "", credentials.getSecretKey());

        OkxWebSocketLoginArg arg = new OkxWebSocketLoginArg();
        arg.setApiKey(credentials.getApiKey());
        arg.setPassphrase(credentials.getPassphrase());
        arg.setTimestamp(timestamp);
        arg.setSign(sign);

        OkxWebSocketLoginRequest request = new OkxWebSocketLoginRequest();
        request.setOp(OkxWebSocketOperationEnum.LOGIN.getCode());
        request.setArgs(Collections.singletonList(arg));
        return request;
    }

    public String loginJson() {
        return jsonCodec.toJson(createLoginRequest(), "Failed to serialize OKX WebSocket login request.");
    }

    public OkxWebSocketLoginEvent parseLoginEvent(String json) {
        return jsonCodec.fromJson(json, OkxWebSocketLoginEvent.class, "Failed to parse OKX WebSocket login event.");
    }
}
