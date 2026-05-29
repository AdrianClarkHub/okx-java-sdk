package io.github.adrianclarkhub.okx.core.config;

import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import io.github.adrianclarkhub.okx.core.enums.UnknownEnumStrategyEnum;
import io.github.adrianclarkhub.okx.core.exception.OkxConfigurationException;

import java.util.Properties;

/**
 * OKX 配置绑定器。
 *
 * <p>将 properties 或环境变量键值绑定到 {@link OkxConfig}，供纯 Java 与测试场景复用。</p>
 */
public final class OkxConfigBinder {

    private static final String PREFIX = "okx.";

    private OkxConfigBinder() {
    }

    /**
     * 从 properties 创建配置。
     *
     * @param properties 配置属性
     * @return SDK 根配置
     */
    public static OkxConfig fromProperties(Properties properties) {
        OkxConfig config = new OkxConfig();
        apply(properties, config);
        return config;
    }

    /**
     * 将 properties 合并到已有配置。
     *
     * @param properties 配置属性
     * @param config SDK 根配置
     */
    public static void apply(Properties properties, OkxConfig config) {
        if (properties == null || config == null) {
            return;
        }
        for (String key : properties.stringPropertyNames()) {
            String normalizedKey = normalizeKey(key);
            if (!normalizedKey.startsWith(PREFIX)) {
                continue;
            }
            applyKey(config, normalizedKey.substring(PREFIX.length()), properties.getProperty(key));
        }
        config.normalize();
    }

    /**
     * 绑定环境变量到配置。
     *
     * <p>支持 {@code OKX_API_KEY}、{@code OKX_LIVE_ENABLED} 等常用变量。</p>
     *
     * @param config SDK 根配置
     */
    public static void applyEnvironmentVariables(OkxConfig config) {
        applyEnv(config, "OKX_ENVIRONMENT", "environment");
        applyEnv(config, "OKX_DEFAULT_ACCOUNT", "default-account");
        applyEnv(config, "OKX_API_KEY", "api-key");
        applyEnv(config, "OKX_SECRET_KEY", "secret-key");
        applyEnv(config, "OKX_PASSPHRASE", "passphrase");
        applyEnv(config, "OKX_LIVE_ENABLED", "live.enabled");
        applyEnv(config, "OKX_LIVE_TESTS", "live.enabled");
        applyEnv(config, "OKX_REST_BASE_URL", "endpoints.rest-base-url");
        applyEnv(config, "OKX_WS_PUBLIC_URL", "endpoints.ws-public-url");
        applyEnv(config, "OKX_WS_PRIVATE_URL", "endpoints.ws-private-url");
        applyEnv(config, "OKX_WS_BUSINESS_URL", "endpoints.ws-business-url");
        applyEnv(config, "OKX_WS_HEARTBEAT_INTERVAL_MILLIS", "websocket.heartbeat-interval-millis");
        applyEnv(config, "OKX_WS_RECONNECT_DELAY_MILLIS", "websocket.reconnect-delay-millis");
        applyEnv(config, "OKX_WS_MAX_RECONNECT_ATTEMPTS", "websocket.max-reconnect-attempts");
        applyEnv(config, "OKX_HTTP_CONNECT_TIMEOUT_MILLIS", "http.connect-timeout-millis");
        applyEnv(config, "OKX_HTTP_READ_TIMEOUT_MILLIS", "http.read-timeout-millis");
        applyEnv(config, "OKX_HTTP_WRITE_TIMEOUT_MILLIS", "http.write-timeout-millis");
        applyEnv(config, "OKX_HTTP_MAX_RETRIES", "http.max-retries");
        applyEnv(config, "OKX_PROXY_ENABLED", "http.proxy.enabled");
        applyEnv(config, "OKX_PROXY_HOST", "http.proxy.host");
        applyEnv(config, "OKX_PROXY_PORT", "http.proxy.port");
        applyEnv(config, "OKX_PROXY_USERNAME", "http.proxy.username");
        applyEnv(config, "OKX_PROXY_PASSWORD", "http.proxy.password");
        config.normalize();
    }

    private static void applyEnv(OkxConfig config, String envName, String configKey) {
        String value = System.getenv(envName);
        if (value != null && !value.trim().isEmpty()) {
            applyKey(config, configKey, value.trim());
        }
    }

    private static String normalizeKey(String key) {
        if (key == null) {
            return "";
        }
        String normalized = key.trim();
        if (normalized.startsWith("\uFEFF")) {
            normalized = normalized.substring(1);
        }
        return normalized;
    }

    private static void applyKey(OkxConfig config, String path, String rawValue) {
        if (path == null || path.isEmpty() || rawValue == null) {
            return;
        }
        String value = rawValue.trim();
        if (value.isEmpty()) {
            return;
        }

        if ("environment".equals(path)) {
            config.setEnvironment(parseEnvironment(value));
            return;
        }
        if ("simulated".equals(path)) {
            if (Boolean.parseBoolean(value)) {
                config.setEnvironment(OkxEnvironmentEnum.DEMO);
            } else {
                config.setEnvironment(OkxEnvironmentEnum.PRODUCTION);
            }
            return;
        }
        if ("unknown-enum-strategy".equals(path)) {
            config.setUnknownEnumStrategy(parseUnknownEnumStrategy(value));
            return;
        }
        if ("default-account".equals(path)) {
            config.setDefaultAccount(value);
            return;
        }
        if ("api-key".equals(path)) {
            config.setApiKey(value);
            return;
        }
        if ("secret-key".equals(path)) {
            config.setSecretKey(value);
            return;
        }
        if ("passphrase".equals(path)) {
            config.setPassphrase(value);
            return;
        }
        if (path.startsWith("accounts.")) {
            applyAccountKey(config, path.substring("accounts.".length()), value);
            return;
        }
        if (path.startsWith("http.")) {
            applyHttpKey(config, path.substring("http.".length()), value);
            return;
        }
        if (path.startsWith("endpoints.")) {
            applyEndpointKey(config, path.substring("endpoints.".length()), value);
            return;
        }
        if (path.startsWith("rest.")) {
            applyRestAliasKey(config, path.substring("rest.".length()), value);
            return;
        }
        if (path.startsWith("websocket.")) {
            applyWebSocketAliasKey(config, path.substring("websocket.".length()), value);
            return;
        }
        if (path.startsWith("live.")) {
            applyLiveKey(config, path.substring("live.".length()), value);
        }
    }

    private static void applyAccountKey(OkxConfig config, String path, String value) {
        int separator = path.indexOf('.');
        if (separator <= 0) {
            return;
        }
        String accountName = path.substring(0, separator);
        String field = path.substring(separator + 1);
        OkxAccountConfig account = config.getAccounts().computeIfAbsent(accountName, key -> new OkxAccountConfig());
        account.setName(accountName);
        if ("api-key".equals(field)) {
            account.setApiKey(value);
        } else if ("secret-key".equals(field)) {
            account.setSecretKey(value);
        } else if ("passphrase".equals(field)) {
            account.setPassphrase(value);
        } else if ("simulated".equals(field)) {
            account.setSimulated(Boolean.valueOf(Boolean.parseBoolean(value)));
        }
    }

    private static void applyHttpKey(OkxConfig config, String path, String value) {
        OkxHttpConfig http = config.getHttp();
        if (http == null) {
            http = new OkxHttpConfig();
            config.setHttp(http);
        }
        if ("connect-timeout-millis".equals(path)) {
            http.setConnectTimeoutMillis(parseInteger("okx.http.connect-timeout-millis", value));
        } else if ("read-timeout-millis".equals(path)) {
            http.setReadTimeoutMillis(parseInteger("okx.http.read-timeout-millis", value));
        } else if ("write-timeout-millis".equals(path)) {
            http.setWriteTimeoutMillis(parseInteger("okx.http.write-timeout-millis", value));
        } else if ("max-retries".equals(path)) {
            http.setMaxRetries(parseInteger("okx.http.max-retries", value));
        } else if (path.startsWith("proxy.")) {
            applyProxyKey(http, path.substring("proxy.".length()), value);
        }
    }

    private static void applyProxyKey(OkxHttpConfig http, String path, String value) {
        OkxProxyConfig proxy = http.getProxy();
        if (proxy == null) {
            proxy = new OkxProxyConfig();
            http.setProxy(proxy);
        }
        if ("enabled".equals(path)) {
            proxy.setEnabled(Boolean.parseBoolean(value));
        } else if ("host".equals(path)) {
            proxy.setHost(value);
        } else if ("port".equals(path)) {
            proxy.setPort(parseInteger("okx.http.proxy.port", value));
        } else if ("username".equals(path)) {
            proxy.setUsername(value);
        } else if ("password".equals(path)) {
            proxy.setPassword(value);
        }
    }

    private static void applyEndpointKey(OkxConfig config, String path, String value) {
        OkxEndpointConfig endpoints = config.getEndpoints();
        if (endpoints == null) {
            endpoints = new OkxEndpointConfig();
            config.setEndpoints(endpoints);
        }
        if ("rest-base-url".equals(path)) {
            endpoints.setRestBaseUrl(value);
        } else if ("ws-public-url".equals(path)) {
            endpoints.setWsPublicUrl(value);
        } else if ("ws-private-url".equals(path)) {
            endpoints.setWsPrivateUrl(value);
        } else if ("ws-business-url".equals(path)) {
            endpoints.setWsBusinessUrl(value);
        }
    }

    private static void applyRestAliasKey(OkxConfig config, String path, String value) {
        if ("base-url".equals(path)) {
            applyEndpointKey(config, "rest-base-url", value);
        }
    }

    private static void applyWebSocketAliasKey(OkxConfig config, String path, String value) {
        if ("public-url".equals(path) || "ws-public-url".equals(path)) {
            applyEndpointKey(config, "ws-public-url", value);
        } else if ("private-url".equals(path) || "ws-private-url".equals(path)) {
            applyEndpointKey(config, "ws-private-url", value);
        } else if ("business-url".equals(path) || "ws-business-url".equals(path)) {
            applyEndpointKey(config, "ws-business-url", value);
        } else {
            applyWebSocketKey(config, path, value);
        }
    }

    private static void applyWebSocketKey(OkxConfig config, String path, String value) {
        OkxWebSocketConfig webSocket = config.getWebSocket();
        if (webSocket == null) {
            webSocket = new OkxWebSocketConfig();
            config.setWebSocket(webSocket);
        }
        if ("heartbeat-interval-millis".equals(path)) {
            webSocket.setHeartbeatIntervalMillis(parseLong("okx.websocket.heartbeat-interval-millis", value));
        } else if ("reconnect-delay-millis".equals(path)) {
            webSocket.setReconnectDelayMillis(parseLong("okx.websocket.reconnect-delay-millis", value));
        } else if ("max-reconnect-attempts".equals(path)) {
            webSocket.setMaxReconnectAttempts(parseInteger("okx.websocket.max-reconnect-attempts", value));
        }
    }

    private static void applyLiveKey(OkxConfig config, String path, String value) {
        OkxLiveConfig live = config.getLive();
        if (live == null) {
            live = new OkxLiveConfig();
            config.setLive(live);
        }
        if ("enabled".equals(path)) {
            live.setEnabled(Boolean.parseBoolean(value));
        }
    }

    private static OkxEnvironmentEnum parseEnvironment(String value) {
        if ("demo".equalsIgnoreCase(value) || "simulated".equalsIgnoreCase(value) || "2".equals(value)) {
            return OkxEnvironmentEnum.DEMO;
        }
        if ("production".equalsIgnoreCase(value) || "live".equalsIgnoreCase(value) || "1".equals(value)) {
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

    private static int parseInteger(String propertyName, String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new OkxConfigurationException("Invalid integer value for " + propertyName + ": " + value, e);
        }
    }

    private static long parseLong(String propertyName, String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new OkxConfigurationException("Invalid long value for " + propertyName + ": " + value, e);
        }
    }
}
