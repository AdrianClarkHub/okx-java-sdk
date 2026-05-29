package io.github.adrianclarkhub.okx.core.config;

/**
 * OKX WebSocket 客户端配置。
 */
public class OkxWebSocketConfig {

    private long heartbeatIntervalMillis;

    private long reconnectDelayMillis;

    private int maxReconnectAttempts;

    /**
     * 创建默认 WebSocket 配置。
     */
    public OkxWebSocketConfig() {
        this.heartbeatIntervalMillis = 25000L;
        this.reconnectDelayMillis = 5000L;
        this.maxReconnectAttempts = 3;
    }

    public long getHeartbeatIntervalMillis() {
        return heartbeatIntervalMillis;
    }

    public void setHeartbeatIntervalMillis(long heartbeatIntervalMillis) {
        this.heartbeatIntervalMillis = heartbeatIntervalMillis;
    }

    public long getReconnectDelayMillis() {
        return reconnectDelayMillis;
    }

    public void setReconnectDelayMillis(long reconnectDelayMillis) {
        this.reconnectDelayMillis = reconnectDelayMillis;
    }

    public int getMaxReconnectAttempts() {
        return maxReconnectAttempts;
    }

    public void setMaxReconnectAttempts(int maxReconnectAttempts) {
        this.maxReconnectAttempts = maxReconnectAttempts;
    }
}
