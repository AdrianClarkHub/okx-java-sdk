package io.github.adrianclarkhub.okx.core.auth;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Provides OKX REST and WebSocket timestamps.
 */
public class OkxTimestampProvider {

    private static final DateTimeFormatter REST_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    private final Clock clock;

    public OkxTimestampProvider() {
        this(Clock.systemUTC());
    }

    public OkxTimestampProvider(Clock clock) {
        this.clock = clock == null ? Clock.systemUTC() : clock;
    }

    public String restTimestamp() {
        return REST_FORMATTER.format(Instant.now(clock));
    }

    public String webSocketTimestamp() {
        return String.valueOf(Instant.now(clock).getEpochSecond());
    }
}
