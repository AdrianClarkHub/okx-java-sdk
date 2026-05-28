package io.github.adrianclarkhub.okx.websocket.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.http.OkxObjectMappers;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketEvent;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketJsonCodec;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketOperationEnum;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketPushMessage;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketRequest;
import io.github.adrianclarkhub.okx.websocket.common.OkxWebSocketRequests;
import io.github.adrianclarkhub.okx.websocket.status.response.StatusChannelData;

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

    private final OkxWebSocketJsonCodec jsonCodec;

    /**
     * 创建 Status 频道客户端。
     */
    public StatusChannelClient() {
        this(OkxObjectMappers.create(null));
    }

    /**
     * 创建 Status 频道客户端。
     *
     * @param objectMapper JSON 序列化器
     */
    public StatusChannelClient(ObjectMapper objectMapper) {
        this.jsonCodec = new OkxWebSocketJsonCodec(objectMapper);
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
        return jsonCodec.fromJson(json, EVENT_TYPE, "Failed to parse OKX WebSocket status event.");
    }

    /**
     * 解析 Status 频道推送消息。
     *
     * @param json 推送消息 JSON
     * @return Status 频道推送消息
     */
    public OkxWebSocketPushMessage<StatusChannelArg, StatusChannelData> parsePushMessage(String json) {
        return jsonCodec.fromJson(json, PUSH_TYPE, "Failed to parse OKX WebSocket status push message.");
    }

    private OkxWebSocketRequest<StatusChannelArg> createRequest(String id, OkxWebSocketOperationEnum operation) {
        return OkxWebSocketRequests.create(id, operation, new StatusChannelArg());
    }

    private String toJson(OkxWebSocketRequest<StatusChannelArg> request) {
        return jsonCodec.toJson(request, "Failed to serialize OKX WebSocket status request.");
    }
}
