package io.github.adrianclarkhub.okx.websocket.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.exception.OkxSerializationException;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketEvent;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketPushMessage;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketRequest;
import io.github.adrianclarkhub.okx.websocket.status.response.StatusChannelData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * StatusChannelClient 单元测试。
 *
 * <p>验证 Status 频道订阅请求构造、JSON 序列化、事件响应解析和推送消息解析。</p>
 */
class StatusChannelClientTest {

    private final StatusChannelClient client = new StatusChannelClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateSubscribeRequestWhenIdProvided() {
        OkxWebSocketRequest<StatusChannelArg> request = client.createSubscribeRequest("1512");

        assertEquals("1512", request.getId(), "Request id should match the provided id.");
        assertEquals("subscribe", request.getOp(), "Operation should be subscribe.");
        assertNotNull(request.getArgs(), "Request args should not be null.");
        assertEquals(1, request.getArgs().size(), "Request should contain one channel arg.");
        assertEquals("status", request.getArgs().get(0).getChannel(), "Channel should be status.");
    }

    @Test
    void shouldCreateUnsubscribeRequestWhenIdProvided() {
        OkxWebSocketRequest<StatusChannelArg> request = client.createUnsubscribeRequest("1513");

        assertEquals("1513", request.getId(), "Request id should match the provided id.");
        assertEquals("unsubscribe", request.getOp(), "Operation should be unsubscribe.");
        assertNotNull(request.getArgs(), "Request args should not be null.");
        assertEquals(1, request.getArgs().size(), "Request should contain one channel arg.");
        assertEquals("status", request.getArgs().get(0).getChannel(), "Channel should be status.");
    }

    @Test
    void shouldSerializeSubscribeJsonWhenIdProvided() throws Exception {
        String json = client.subscribeJson("1512");
        JsonNode root = objectMapper.readTree(json);

        assertEquals("1512", root.get("id").asText(), "Serialized id should match.");
        assertEquals("subscribe", root.get("op").asText(), "Serialized op should be subscribe.");
        assertEquals("status", root.get("args").get(0).get("channel").asText(), "Serialized channel should be status.");
    }

    @Test
    void shouldOmitIdWhenSubscribeRequestHasNoId() throws Exception {
        String json = client.subscribeJson(null);
        JsonNode root = objectMapper.readTree(json);

        assertEquals(false, root.has("id"), "Serialized request should omit null id.");
        assertEquals("subscribe", root.get("op").asText(), "Serialized op should be subscribe.");
        assertEquals("status", root.get("args").get(0).get("channel").asText(), "Serialized channel should be status.");
    }

    @Test
    void shouldParseSubscribeEventWhenJsonValid() {
        String json = "{\"id\":\"1512\",\"event\":\"subscribe\",\"arg\":{\"channel\":\"status\"},\"connId\":\"a4d3ae55\"}";

        OkxWebSocketEvent<StatusChannelArg> event = client.parseEvent(json);

        assertEquals("1512", event.getId(), "Event id should match.");
        assertEquals("subscribe", event.getEvent(), "Event type should match.");
        assertNotNull(event.getArg(), "Event arg should not be null.");
        assertEquals("status", event.getArg().getChannel(), "Event channel should be status.");
        assertEquals("a4d3ae55", event.getConnId(), "Connection id should match.");
    }

    @Test
    void shouldParseErrorEventWhenJsonValid() {
        String json = "{\"id\":\"1512\",\"event\":\"error\",\"code\":\"60012\",\"msg\":\"Invalid request\",\"connId\":\"a4d3ae55\"}";

        OkxWebSocketEvent<StatusChannelArg> event = client.parseEvent(json);

        assertEquals("1512", event.getId(), "Event id should match.");
        assertEquals("error", event.getEvent(), "Event type should be error.");
        assertEquals("60012", event.getCode(), "Error code should match.");
        assertEquals("Invalid request", event.getMsg(), "Error message should match.");
        assertEquals("a4d3ae55", event.getConnId(), "Connection id should match.");
    }

    @Test
    void shouldParsePushMessageWhenJsonValid() {
        String json = "{\"arg\":{\"channel\":\"status\"},\"data\":[{\"begin\":\"1672823400000\",\"end\":\"1672825980000\",\"href\":\"\",\"preOpenBegin\":\"\",\"scheDesc\":\"\",\"serviceType\":\"0\",\"state\":\"completed\",\"system\":\"unified\",\"maintType\":\"1\",\"env\":\"1\",\"title\":\"Trading account WebSocket system upgrade\",\"ts\":\"1672826038470\"}]}";

        OkxWebSocketPushMessage<StatusChannelArg, StatusChannelData> message = client.parsePushMessage(json);

        assertNotNull(message.getArg(), "Push arg should not be null.");
        assertEquals("status", message.getArg().getChannel(), "Push channel should be status.");
        assertNotNull(message.getData(), "Push data should not be null.");
        assertEquals(1, message.getData().size(), "Push data should contain one item.");
        StatusChannelData data = message.getData().get(0);
        assertEquals("1672823400000", data.getBegin(), "Begin should match.");
        assertEquals("1672825980000", data.getEnd(), "End should match.");
        assertEquals("completed", data.getState(), "State should match.");
        assertEquals("0", data.getServiceType(), "Service type should match.");
        assertEquals("unified", data.getSystem(), "System should match.");
        assertEquals("1", data.getMaintType(), "Maintenance type should match.");
        assertEquals("1", data.getEnv(), "Environment should match.");
        assertEquals("Trading account WebSocket system upgrade", data.getTitle(), "Title should match.");
        assertEquals("1672826038470", data.getTs(), "Timestamp should match.");
    }

    @Test
    void shouldThrowSerializationExceptionWhenEventJsonInvalid() {
        assertThrows(OkxSerializationException.class, () -> client.parseEvent("{"), "Invalid event JSON should throw serialization exception.");
    }

    @Test
    void shouldThrowSerializationExceptionWhenPushJsonInvalid() {
        assertThrows(OkxSerializationException.class, () -> client.parsePushMessage("{"), "Invalid push JSON should throw serialization exception.");
    }
}
