package io.github.adrianclarkhub.okx.websocket.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.exception.OkxSerializationException;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketEvent;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketOperationEnum;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketPushMessage;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketRequest;
import io.github.adrianclarkhub.okx.websocket.status.response.StatusChannelData;

import java.util.Collections;

/**
 * OKX WebSocket Status 频道客户端。
 *
 * <p>用于构造 /ws/v5/public 的 status 频道订阅请求，并解析订阅事件和推送消息。</p>
 */
public class StatusChannelClient {

    private static final TypeReference<OkxWebSocketEvent<StatusChannelArg>> EVENT_TYPE =
            new TypeReference<OkxWebSocketEvent<StatusChannelArg>>() {
            };

    private static final TypeReference<OkxWebSocketPushMessage<StatusChannelArg, StatusChannelData>> PUSH_TYPE =
            new TypeReference<OkxWebSocketPushMessage<StatusChannelArg, StatusChannelData>>() {
            };

    private final ObjectMapper objectMapper;

    /**
     * 创建 Status 频道客户端。
     */
    public StatusChannelClient() {
        this(new ObjectMapper());
    }

    /**
     * 创建 Status 频道客户端。
     *
     * @param objectMapper JSON 序列化器
     */
    public StatusChannelClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
    }

    /**
     * 创建订阅请求。
     *
     * @return 订阅请求
     */
    public OkxWebSocketRequest<StatusChannelArg> createSubscribeRequest() {
        return createSubscribeRequest(null);
    }

    /**
     * 创建订阅请求。
     *
     * @param id 消息唯一标识
     * @return 订阅请求
     */
    public OkxWebSocketRequest<StatusChannelArg> createSubscribeRequest(String id) {
        return createRequest(id, OkxWebSocketOperationEnum.SUBSCRIBE);
    }

    /**
     * 创建取消订阅请求。
     *
     * @return 取消订阅请求
     */
    public OkxWebSocketRequest<StatusChannelArg> createUnsubscribeRequest() {
        return createUnsubscribeRequest(null);
    }

    /**
     * 创建取消订阅请求。
     *
     * @param id 消息唯一标识
     * @return 取消订阅请求
     */
    public OkxWebSocketRequest<StatusChannelArg> createUnsubscribeRequest(String id) {
        return createRequest(id, OkxWebSocketOperationEnum.UNSUBSCRIBE);
    }

    /**
     * 将订阅请求序列化为 JSON。
     *
     * @param id 消息唯一标识
     * @return 订阅请求 JSON
     */
    public String subscribeJson(String id) {
        return toJson(createSubscribeRequest(id));
    }

    /**
     * 将取消订阅请求序列化为 JSON。
     *
     * @param id 消息唯一标识
     * @return 取消订阅请求 JSON
     */
    public String unsubscribeJson(String id) {
        return toJson(createUnsubscribeRequest(id));
    }

    /**
     * 解析订阅事件响应。
     *
     * @param json 事件 JSON
     * @return 订阅事件响应
     */
    public OkxWebSocketEvent<StatusChannelArg> parseEvent(String json) {
        try {
            return objectMapper.readValue(json, EVENT_TYPE);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException("Failed to parse OKX WebSocket status event.", e);
        }
    }

    /**
     * 解析 Status 频道推送消息。
     *
     * @param json 推送消息 JSON
     * @return Status 频道推送消息
     */
    public OkxWebSocketPushMessage<StatusChannelArg, StatusChannelData> parsePushMessage(String json) {
        try {
            return objectMapper.readValue(json, PUSH_TYPE);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException("Failed to parse OKX WebSocket status push message.", e);
        }
    }

    private OkxWebSocketRequest<StatusChannelArg> createRequest(String id, OkxWebSocketOperationEnum operation) {
        OkxWebSocketRequest<StatusChannelArg> request = new OkxWebSocketRequest<StatusChannelArg>();
        request.setId(id);
        request.setOp(operation.getCode());
        request.setArgs(Collections.singletonList(new StatusChannelArg()));
        return request;
    }

    private String toJson(OkxWebSocketRequest<StatusChannelArg> request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new OkxSerializationException("Failed to serialize OKX WebSocket status request.", e);
        }
    }
}
