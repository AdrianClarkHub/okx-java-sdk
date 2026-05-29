package io.github.adrianclarkhub.okx.spring.autoconfigure;

import io.github.adrianclarkhub.okx.core.config.OkxAccountConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxEndpointConfig;
import io.github.adrianclarkhub.okx.core.config.OkxHttpConfig;
import io.github.adrianclarkhub.okx.core.config.OkxLiveConfig;
import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import io.github.adrianclarkhub.okx.core.config.OkxWebSocketConfig;
import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import io.github.adrianclarkhub.okx.core.enums.UnknownEnumStrategyEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Spring Boot OKX 配置属性。
 *
 * <p>绑定 {@code okx.*} 配置并转换为 SDK 唯一的 {@link OkxConfig}。</p>
 */
@ConfigurationProperties(prefix = "okx")
public class OkxProperties {

    private String environment;

    private String unknownEnumStrategy;

    private String defaultAccount;

    private String apiKey;

    private String secretKey;

    private String passphrase;

    private Boolean simulated;

    private Map<String, AccountProperties> accounts = new LinkedHashMap<>();

    private HttpProperties http = new HttpProperties();

    private EndpointProperties endpoints = new EndpointProperties();

    private WebSocketProperties websocket = new WebSocketProperties();

    private LiveProperties live = new LiveProperties();

    /**
     * 转换为 SDK 根配置。
     *
     * @return SDK 根配置
     */
    public OkxConfig toOkxConfig() {
        OkxConfig config = new OkxConfig();
        if (environment != null && !environment.isEmpty()) {
            config.setEnvironment(parseEnvironment(environment));
        } else if (simulated != null) {
            config.setEnvironment(simulated ? OkxEnvironmentEnum.DEMO : OkxEnvironmentEnum.PRODUCTION);
        }
        if (unknownEnumStrategy != null && !unknownEnumStrategy.isEmpty()) {
            config.setUnknownEnumStrategy(parseUnknownEnumStrategy(unknownEnumStrategy));
        }
        config.setDefaultAccount(defaultAccount);
        config.setApiKey(apiKey);
        config.setSecretKey(secretKey);
        config.setPassphrase(passphrase);
        config.setHttp(http.toOkxHttpConfig());
        config.setEndpoints(endpoints.toOkxEndpointConfig());
        config.setWebSocket(websocket.toOkxWebSocketConfig());
        config.setLive(live.toOkxLiveConfig());
        config.setAccounts(toAccountMap());
        config.normalize();
        return config;
    }

    private Map<String, OkxAccountConfig> toAccountMap() {
        Map<String, OkxAccountConfig> accountMap = new LinkedHashMap<>();
        for (Map.Entry<String, AccountProperties> entry : accounts.entrySet()) {
            accountMap.put(entry.getKey(), entry.getValue().toOkxAccountConfig(entry.getKey()));
        }
        return accountMap;
    }

    private static OkxEnvironmentEnum parseEnvironment(String value) {
        if ("demo".equalsIgnoreCase(value) || "simulated".equalsIgnoreCase(value)) {
            return OkxEnvironmentEnum.DEMO;
        }
        if ("production".equalsIgnoreCase(value) || "live".equalsIgnoreCase(value)) {
            return OkxEnvironmentEnum.PRODUCTION;
        }
        return OkxEnvironmentEnum.fromCode(value);
    }

    private static UnknownEnumStrategyEnum parseUnknownEnumStrategy(String value) {
        if ("throw".equalsIgnoreCase(value) || "throw_exception".equalsIgnoreCase(value)) {
            return UnknownEnumStrategyEnum.THROW_EXCEPTION;
        }
        return UnknownEnumStrategyEnum.USE_UNKNOWN;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getUnknownEnumStrategy() {
        return unknownEnumStrategy;
    }

    public void setUnknownEnumStrategy(String unknownEnumStrategy) {
        this.unknownEnumStrategy = unknownEnumStrategy;
    }

    public String getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(String defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public Boolean getSimulated() {
        return simulated;
    }

    public void setSimulated(Boolean simulated) {
        this.simulated = simulated;
    }

    public Map<String, AccountProperties> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, AccountProperties> accounts) {
        this.accounts = accounts;
    }

    public HttpProperties getHttp() {
        return http;
    }

    public void setHttp(HttpProperties http) {
        this.http = http;
    }

    public EndpointProperties getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(EndpointProperties endpoints) {
        this.endpoints = endpoints;
    }

    public WebSocketProperties getWebsocket() {
        return websocket;
    }

    public void setWebsocket(WebSocketProperties websocket) {
        this.websocket = websocket;
    }

    public LiveProperties getLive() {
        return live;
    }

    public void setLive(LiveProperties live) {
        this.live = live;
    }

    /**
     * 账户配置属性。
     */
    public static class AccountProperties {

        private String apiKey;

        private String secretKey;

        private String passphrase;

        private Boolean simulated;

        public OkxAccountConfig toOkxAccountConfig(String accountName) {
            OkxAccountConfig account = new OkxAccountConfig();
            account.setName(accountName);
            account.setApiKey(apiKey);
            account.setSecretKey(secretKey);
            account.setPassphrase(passphrase);
            return account;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getPassphrase() {
            return passphrase;
        }

        public void setPassphrase(String passphrase) {
            this.passphrase = passphrase;
        }

        public Boolean getSimulated() {
            return simulated;
        }

        public void setSimulated(Boolean simulated) {
            this.simulated = simulated;
        }
    }

    /**
     * HTTP 配置属性。
     */
    public static class HttpProperties {

        private int connectTimeoutMillis = 10000;

        private int readTimeoutMillis = 30000;

        private int writeTimeoutMillis = 30000;

        private int maxRetries;

        private ProxyProperties proxy = new ProxyProperties();

        public OkxHttpConfig toOkxHttpConfig() {
            OkxHttpConfig httpConfig = new OkxHttpConfig();
            httpConfig.setConnectTimeoutMillis(connectTimeoutMillis);
            httpConfig.setReadTimeoutMillis(readTimeoutMillis);
            httpConfig.setWriteTimeoutMillis(writeTimeoutMillis);
            httpConfig.setMaxRetries(maxRetries);
            httpConfig.setProxy(proxy.toOkxProxyConfig());
            return httpConfig;
        }

        public int getConnectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        public void setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
        }

        public int getReadTimeoutMillis() {
            return readTimeoutMillis;
        }

        public void setReadTimeoutMillis(int readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
        }

        public int getWriteTimeoutMillis() {
            return writeTimeoutMillis;
        }

        public void setWriteTimeoutMillis(int writeTimeoutMillis) {
            this.writeTimeoutMillis = writeTimeoutMillis;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        public ProxyProperties getProxy() {
            return proxy;
        }

        public void setProxy(ProxyProperties proxy) {
            this.proxy = proxy;
        }
    }

    /**
     * 代理配置属性。
     */
    public static class ProxyProperties {

        private boolean enabled;

        private String host;

        private int port;

        private String username;

        private String password;

        public OkxProxyConfig toOkxProxyConfig() {
            OkxProxyConfig proxyConfig = new OkxProxyConfig();
            proxyConfig.setEnabled(enabled);
            proxyConfig.setHost(host);
            proxyConfig.setPort(port);
            proxyConfig.setUsername(username);
            proxyConfig.setPassword(password);
            return proxyConfig;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 端点配置属性。
     */
    public static class EndpointProperties {

        private String restBaseUrl;

        private String wsPublicUrl;

        private String wsPrivateUrl;

        private String wsBusinessUrl;

        public OkxEndpointConfig toOkxEndpointConfig() {
            OkxEndpointConfig endpointConfig = new OkxEndpointConfig();
            endpointConfig.setRestBaseUrl(restBaseUrl);
            endpointConfig.setWsPublicUrl(wsPublicUrl);
            endpointConfig.setWsPrivateUrl(wsPrivateUrl);
            endpointConfig.setWsBusinessUrl(wsBusinessUrl);
            return endpointConfig;
        }

        public String getRestBaseUrl() {
            return restBaseUrl;
        }

        public void setRestBaseUrl(String restBaseUrl) {
            this.restBaseUrl = restBaseUrl;
        }

        public String getWsPublicUrl() {
            return wsPublicUrl;
        }

        public void setWsPublicUrl(String wsPublicUrl) {
            this.wsPublicUrl = wsPublicUrl;
        }

        public String getWsPrivateUrl() {
            return wsPrivateUrl;
        }

        public void setWsPrivateUrl(String wsPrivateUrl) {
            this.wsPrivateUrl = wsPrivateUrl;
        }

        public String getWsBusinessUrl() {
            return wsBusinessUrl;
        }

        public void setWsBusinessUrl(String wsBusinessUrl) {
            this.wsBusinessUrl = wsBusinessUrl;
        }
    }

    /**
     * WebSocket 客户端配置属性。
     */
    public static class WebSocketProperties {

        private long heartbeatIntervalMillis = 25000L;

        private long reconnectDelayMillis = 5000L;

        private int maxReconnectAttempts = 3;

        public OkxWebSocketConfig toOkxWebSocketConfig() {
            OkxWebSocketConfig webSocketConfig = new OkxWebSocketConfig();
            webSocketConfig.setHeartbeatIntervalMillis(heartbeatIntervalMillis);
            webSocketConfig.setReconnectDelayMillis(reconnectDelayMillis);
            webSocketConfig.setMaxReconnectAttempts(maxReconnectAttempts);
            return webSocketConfig;
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

    /**
     * Live 测试配置属性。
     */
    public static class LiveProperties {

        private boolean enabled;

        public OkxLiveConfig toOkxLiveConfig() {
            OkxLiveConfig liveConfig = new OkxLiveConfig();
            liveConfig.setEnabled(enabled);
            return liveConfig;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
