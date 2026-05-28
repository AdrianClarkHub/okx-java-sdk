package io.github.adrianclarkhub.okx.core.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OkxSignerTest {

    @Test
    void shouldSignRestRequestWithQueryPath() {
        String sign = OkxSigner.sign(
                "2020-12-08T09:08:57.715Z",
                "GET",
                "/api/v5/account/balance?ccy=BTC",
                "",
                "22582BD0CFF14C41EDBF1AB98506286D");

        assertEquals("HiZhvSfMtWJA3uUIVXV3a/bSXNPCWvYFXoGCVS8V4zY=", sign, "REST signature should be stable.");
    }

    @Test
    void shouldSignWebSocketLoginRequest() {
        String sign = OkxSigner.sign("1704876947", "GET", "/users/self/verify", "", "secret-key");

        assertEquals("K5oUSHUHtfglg8PhbJI1ZvOIggOo4BcDbCvsB3H7yXY=", sign, "WebSocket login signature should be stable.");
    }
}
