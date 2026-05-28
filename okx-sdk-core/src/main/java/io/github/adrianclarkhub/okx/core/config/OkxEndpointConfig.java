package io.github.adrianclarkhub.okx.core.config;

/**
 * OKX API 端点配置。
 *
 * <p>未显式配置时使用 OKX 官方默认地址。实盘与模拟盘通常共用同一域名，模拟盘通过请求头区分。</p>
 */
public class OkxEndpointConfig {

    /**
     * 默认 REST API 基础地址。
     */
    public static final String DEFAULT_REST_BASE_URL = "https://www.okx.com";

    /**
     * 默认 WebSocket 公共频道地址。
     */
    public static final String DEFAULT_WS_PUBLIC_URL = "wss://ws.okx.com:8443/ws/v5/public";

    /**
     * 默认 WebSocket 私有频道地址。
     */
    public static final String DEFAULT_WS_PRIVATE_URL = "wss://ws.okx.com:8443/ws/v5/private";

    /**
     * 默认 WebSocket 业务频道地址。
     */
    public static final String DEFAULT_WS_BUSINESS_URL = "wss://ws.okx.com:8443/ws/v5/business";

    private String restBaseUrl;

    private String wsPublicUrl;

    private String wsPrivateUrl;

    private String wsBusinessUrl;

    /**
     * 创建默认端点配置。
     */
    public OkxEndpointConfig() {
    }

    /**
     * 获取 REST API 基础地址。
     *
     * @return REST API 基础地址
     */
    public String getRestBaseUrl() {
        return restBaseUrl == null || restBaseUrl.isEmpty() ? DEFAULT_REST_BASE_URL : restBaseUrl;
    }

    /**
     * 设置 REST API 基础地址。
     *
     * @param restBaseUrl REST API 基础地址
     */
    public void setRestBaseUrl(String restBaseUrl) {
        this.restBaseUrl = restBaseUrl;
    }

    /**
     * 获取 WebSocket 公共频道地址。
     *
     * @return WebSocket 公共频道地址
     */
    public String getWsPublicUrl() {
        return wsPublicUrl == null || wsPublicUrl.isEmpty() ? DEFAULT_WS_PUBLIC_URL : wsPublicUrl;
    }

    /**
     * 设置 WebSocket 公共频道地址。
     *
     * @param wsPublicUrl WebSocket 公共频道地址
     */
    public void setWsPublicUrl(String wsPublicUrl) {
        this.wsPublicUrl = wsPublicUrl;
    }

    /**
     * 获取 WebSocket 私有频道地址。
     *
     * @return WebSocket 私有频道地址
     */
    public String getWsPrivateUrl() {
        return wsPrivateUrl == null || wsPrivateUrl.isEmpty() ? DEFAULT_WS_PRIVATE_URL : wsPrivateUrl;
    }

    /**
     * 设置 WebSocket 私有频道地址。
     *
     * @param wsPrivateUrl WebSocket 私有频道地址
     */
    public void setWsPrivateUrl(String wsPrivateUrl) {
        this.wsPrivateUrl = wsPrivateUrl;
    }

    /**
     * 获取 WebSocket 业务频道地址。
     *
     * @return WebSocket 业务频道地址
     */
    public String getWsBusinessUrl() {
        return wsBusinessUrl == null || wsBusinessUrl.isEmpty() ? DEFAULT_WS_BUSINESS_URL : wsBusinessUrl;
    }

    /**
     * 设置 WebSocket 业务频道地址。
     *
     * @param wsBusinessUrl WebSocket 业务频道地址
     */
    public void setWsBusinessUrl(String wsBusinessUrl) {
        this.wsBusinessUrl = wsBusinessUrl;
    }
}
