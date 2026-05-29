package io.github.adrianclarkhub.okx.websocket.common;

import io.github.adrianclarkhub.okx.core.exception.OkxApiException;
import io.github.adrianclarkhub.okx.core.exception.OkxWebSocketCloseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OkxWebSocketProtocolClientTest {

    @Test
    void shouldParseNoticeEvent() {
        OkxWebSocketProtocolClient client = new OkxWebSocketProtocolClient();

        OkxWebSocketNoticeEvent event = client.parseNoticeEvent("{\"event\":\"notice\",\"code\":\"64008\","
                + "\"msg\":\"The connection will soon be closed for a service upgrade. Please reconnect.\","
                + "\"connId\":\"a4d3ae55\"}");

        assertEquals("notice", event.getEvent(), "Notice event should be parsed.");
        assertEquals("64008", event.getCode(), "Notice code should be parsed.");
        assertEquals("a4d3ae55", event.getConnId(), "Connection ID should be parsed.");
    }

    @Test
    void shouldParseChannelConnectionCountEvent() {
        OkxWebSocketProtocolClient client = new OkxWebSocketProtocolClient();

        OkxWebSocketChannelConnectionEvent event = client.parseChannelConnectionEvent("{\"event\":\"channel-conn-count\","
                + "\"channel\":\"orders\",\"connCount\":\"2\",\"connId\":\"abcd1234\"}");

        assertEquals("channel-conn-count", event.getEvent(), "Event should be parsed.");
        assertEquals("orders", event.getChannel(), "Channel should be parsed.");
        assertEquals("2", event.getConnCount(), "Connection count should be parsed.");
    }

    @Test
    void shouldParseChannelConnectionCountErrorEvent() {
        OkxWebSocketProtocolClient client = new OkxWebSocketProtocolClient();

        OkxWebSocketChannelConnectionEvent event = client.parseChannelConnectionEvent(
                "{\"event\":\"channel-conn-count-error\",\"channel\":\"orders\",\"connCount\":\"30\","
                        + "\"connId\":\"a4d3ae55\"}");

        assertEquals("channel-conn-count-error", event.getEvent(), "Event should be parsed.");
        assertEquals("30", event.getConnCount(), "Connection count should be parsed.");
    }

    @Test
    void shouldParseErrorEventAsApiException() {
        OkxWebSocketProtocolClient client = new OkxWebSocketProtocolClient();

        OkxApiException exception = client.parseErrorEvent(
                "{\"event\":\"error\",\"code\":\"60012\",\"msg\":\"Invalid request\"}");

        assertNotNull(exception, "WebSocket error event should become API exception.");
        assertEquals("60012", exception.getRawCode(), "Error code should be preserved.");
        assertEquals("Invalid request", exception.getOkxMessage(), "Error message should be preserved.");
    }

    @Test
    void shouldParseDocumentedCloseFrameAsApiException() {
        OkxWebSocketProtocolClient client = new OkxWebSocketProtocolClient();

        OkxApiException exception = client.parseCloseFrame(4004, "Connection count limit exceeded.");

        assertNotNull(exception, "Documented close frame should become API exception.");
        assertEquals("4004", exception.getRawCode(), "Close frame code should be preserved.");
        assertEquals("Connection count limit exceeded.", exception.getOkxMessage(),
                "Close frame reason should be preserved.");
        assertEquals(OkxWebSocketCloseException.class, exception.getClass(),
                "Close frame should use WebSocket close exception.");
    }

    @Test
    void shouldIgnoreUnknownCloseFrame() {
        OkxWebSocketProtocolClient client = new OkxWebSocketProtocolClient();

        assertNull(client.parseCloseFrame(1000, "Normal closure."),
                "Unknown or normal close frame should not become OKX API exception.");
    }

    @Test
    void shouldIgnoreNonErrorEventWhenParsingError() {
        OkxWebSocketProtocolClient client = new OkxWebSocketProtocolClient();

        assertNull(client.parseErrorEvent("{\"event\":\"subscribe\",\"arg\":{\"channel\":\"status\"}}"),
                "Non-error event should not become exception.");
    }
}
