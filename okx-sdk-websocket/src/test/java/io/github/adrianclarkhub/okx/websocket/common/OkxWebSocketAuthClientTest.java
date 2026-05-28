package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.auth.OkxSigner;
import io.github.adrianclarkhub.okx.core.auth.OkxTimestampProvider;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OkxWebSocketAuthClientTest {

    @Test
    void shouldCreateLoginRequestWithSignedCredentials() {
        OkxConfig config = credentialsConfig();
        OkxTimestampProvider timestampProvider = new OkxTimestampProvider(
                Clock.fixed(Instant.ofEpochSecond(1704876947L), ZoneOffset.UTC));
        OkxWebSocketAuthClient client = new OkxWebSocketAuthClient(config, new ObjectMapper(), timestampProvider);

        OkxWebSocketLoginRequest request = client.createLoginRequest();
        OkxWebSocketLoginArg arg = request.getArgs().get(0);

        assertEquals("login", request.getOp(), "Operation should be login.");
        assertEquals("api-key", arg.getApiKey(), "API key should be present.");
        assertEquals("passphrase", arg.getPassphrase(), "Passphrase should be present.");
        assertEquals("1704876947", arg.getTimestamp(), "Timestamp should use epoch seconds.");
        assertEquals(OkxSigner.sign("1704876947", "GET", "/users/self/verify", "", "secret-key"), arg.getSign(),
                "Login signature should match OKX login prehash.");
    }

    @Test
    void shouldSerializeLoginRequestJson() {
        OkxConfig config = credentialsConfig();
        OkxTimestampProvider timestampProvider = new OkxTimestampProvider(
                Clock.fixed(Instant.ofEpochSecond(1704876947L), ZoneOffset.UTC));
        OkxWebSocketAuthClient client = new OkxWebSocketAuthClient(config, new ObjectMapper(), timestampProvider);

        String json = client.loginJson();

        assertEquals("{\"op\":\"login\",\"args\":[{\"apiKey\":\"api-key\",\"passphrase\":\"passphrase\","
                + "\"timestamp\":\"1704876947\",\"sign\":\""
                + OkxSigner.sign("1704876947", "GET", "/users/self/verify", "", "secret-key") + "\"}]}",
                json, "Login JSON should match OKX WebSocket protocol.");
    }

    @Test
    void shouldParseLoginEvent() {
        OkxWebSocketAuthClient client = new OkxWebSocketAuthClient(credentialsConfig());

        OkxWebSocketLoginEvent event = client.parseLoginEvent(
                "{\"event\":\"login\",\"code\":\"0\",\"msg\":\"\",\"connId\":\"a4d3ae55\"}");

        assertEquals("login", event.getEvent(), "Event should be parsed.");
        assertEquals("0", event.getCode(), "Code should be parsed.");
        assertEquals("a4d3ae55", event.getConnId(), "Connection ID should be parsed.");
    }

    private static OkxConfig credentialsConfig() {
        OkxConfig config = new OkxConfig();
        config.setApiKey("api-key");
        config.setSecretKey("secret-key");
        config.setPassphrase("passphrase");
        config.normalize();
        return config;
    }
}
