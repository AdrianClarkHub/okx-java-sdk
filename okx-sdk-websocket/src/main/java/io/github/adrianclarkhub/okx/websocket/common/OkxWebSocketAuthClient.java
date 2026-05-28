package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.auth.OkxCredentials;
import io.github.adrianclarkhub.okx.core.auth.OkxSigner;
import io.github.adrianclarkhub.okx.core.auth.OkxTimestampProvider;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxSerializationException;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;

import java.util.Collections;

public class OkxWebSocketAuthClient {

    public static final String LOGIN_METHOD = "GET";

    public static final String LOGIN_REQUEST_PATH = "/users/self/verify";

    private final OkxConfig config;

    private final ObjectMapper objectMapper;

    private final OkxTimestampProvider timestampProvider;

    public OkxWebSocketAuthClient(OkxConfig config) {
        this(config, OkxObjectMappers.create(config), new OkxTimestampProvider());
    }

    public OkxWebSocketAuthClient(OkxConfig config, ObjectMapper objectMapper, OkxTimestampProvider timestampProvider) {
        this.config = config;
        this.objectMapper = objectMapper == null ? OkxObjectMappers.create(config) : objectMapper;
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
        try {
            return objectMapper.writeValueAsString(createLoginRequest());
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException("Failed to serialize OKX WebSocket login request.", e);
        }
    }

    public OkxWebSocketLoginEvent parseLoginEvent(String json) {
        try {
            return objectMapper.readValue(json, OkxWebSocketLoginEvent.class);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException("Failed to parse OKX WebSocket login event.", e);
        }
    }
}
