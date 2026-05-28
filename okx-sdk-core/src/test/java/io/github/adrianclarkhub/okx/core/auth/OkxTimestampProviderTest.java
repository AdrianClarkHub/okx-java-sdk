package io.github.adrianclarkhub.okx.core.auth;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OkxTimestampProviderTest {

    @Test
    void shouldFormatRestTimestampInUtcWithMilliseconds() {
        OkxTimestampProvider provider = new OkxTimestampProvider(
                Clock.fixed(Instant.parse("2020-12-08T09:08:57.715Z"), ZoneOffset.UTC));

        assertEquals("2020-12-08T09:08:57.715Z", provider.restTimestamp(), "REST timestamp should match OKX format.");
    }

    @Test
    void shouldFormatWebSocketTimestampInEpochSeconds() {
        OkxTimestampProvider provider = new OkxTimestampProvider(
                Clock.fixed(Instant.ofEpochSecond(1704876947L), ZoneOffset.UTC));

        assertEquals("1704876947", provider.webSocketTimestamp(), "WebSocket timestamp should use epoch seconds.");
    }
}
