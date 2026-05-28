package io.github.adrianclarkhub.okx.websocket.common;

import java.util.Collections;

public final class OkxWebSocketRequests {

    private OkxWebSocketRequests() {
    }

    public static <T extends OkxWebSocketChannelArg> OkxWebSocketRequest<T> subscribe(String id, T arg) {
        return create(id, OkxWebSocketOperationEnum.SUBSCRIBE, arg);
    }

    public static <T extends OkxWebSocketChannelArg> OkxWebSocketRequest<T> unsubscribe(String id, T arg) {
        return create(id, OkxWebSocketOperationEnum.UNSUBSCRIBE, arg);
    }

    public static <T extends OkxWebSocketChannelArg> OkxWebSocketRequest<T> create(
            String id, OkxWebSocketOperationEnum operation, T arg) {
        OkxWebSocketRequest<T> request = new OkxWebSocketRequest<T>();
        request.setId(id);
        request.setOp(operation.getCode());
        request.setArgs(Collections.singletonList(arg));
        return request;
    }
}
