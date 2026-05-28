package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OkxWebSocketChannelConnectionEvent {

    private String event;

    private String channel;

    private String connCount;

    private String connId;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getConnCount() {
        return connCount;
    }

    public void setConnCount(String connCount) {
        this.connCount = connCount;
    }

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }
}
