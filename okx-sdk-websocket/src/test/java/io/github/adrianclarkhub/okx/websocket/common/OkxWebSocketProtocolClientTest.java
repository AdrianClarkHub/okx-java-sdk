package io.github.adrianclarkhub.okx.websocket.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
