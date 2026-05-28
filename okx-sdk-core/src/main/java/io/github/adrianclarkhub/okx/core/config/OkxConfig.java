package io.github.adrianclarkhub.okx.core.config;

import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import io.github.adrianclarkhub.okx.core.enums.UnknownEnumStrategyEnum;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OKX SDK 根配置。
 *
 * <p>SDK 全局唯一配置模型。无论来自 properties、YAML 还是代码构造，最终都应收敛为该对象。</p>
 */
public class OkxConfig {

    private OkxEnvironmentEnum environment;

    private UnknownEnumStrategyEnum unknownEnumStrategy;

    private String defaultAccount;

    private String apiKey;

    private String secretKey;

    private String passphrase;

    private OkxHttpConfig http;

    private OkxEndpointConfig endpoints;

    private OkxLiveConfig live;

    private Map<String, OkxAccountConfig> accounts;

    /**
     * 创建默认 SDK 配置。
     */
    public OkxConfig() {
        this.environment = OkxEnvironmentEnum.PRODUCTION;
        this.unknownEnumStrategy = UnknownEnumStrategyEnum.USE_UNKNOWN;
        this.http = new OkxHttpConfig();
        this.endpoints = new OkxEndpointConfig();
        this.live = new OkxLiveConfig();
        this.accounts = new LinkedHashMap<>();
    }

    /**
     * 规范化配置。
     *
     * <p>将单账户扁平字段合并进账户映射，并补齐账户名称。</p>
     */
    public void normalize() {
        if (hasRootCredentials() && accounts.isEmpty()) {
            String accountName = defaultAccount == null || defaultAccount.isEmpty() ? "default" : defaultAccount;
            accounts.put(accountName, createAccountFromRoot(accountName));
        }
        for (Map.Entry<String, OkxAccountConfig> entry : accounts.entrySet()) {
            OkxAccountConfig account = entry.getValue();
            if (account.getName() == null || account.getName().isEmpty()) {
                account.setName(entry.getKey());
            }
        }
        if ((defaultAccount == null || defaultAccount.isEmpty()) && accounts.size() == 1) {
            defaultAccount = accounts.keySet().iterator().next();
        }
    }

    /**
     * 获取 OKX 交易环境。
     *
     * @return OKX 交易环境
     */
    public OkxEnvironmentEnum getEnvironment() {
        return environment;
    }

    /**
     * 设置 OKX 交易环境。
     *
     * @param environment OKX 交易环境
     */
    public void setEnvironment(OkxEnvironmentEnum environment) {
        this.environment = environment;
    }

    /**
     * 获取未知枚举值处理策略。
     *
     * @return 未知枚举值处理策略
     */
    public UnknownEnumStrategyEnum getUnknownEnumStrategy() {
        return unknownEnumStrategy;
    }

    /**
     * 设置未知枚举值处理策略。
     *
     * @param unknownEnumStrategy 未知枚举值处理策略
     */
    public void setUnknownEnumStrategy(UnknownEnumStrategyEnum unknownEnumStrategy) {
        this.unknownEnumStrategy = unknownEnumStrategy;
    }

    /**
     * 获取默认账户名称。
     *
     * @return 默认账户名称
     */
    public String getDefaultAccount() {
        return defaultAccount;
    }

    /**
     * 设置默认账户名称。
     *
     * @param defaultAccount 默认账户名称
     */
    public void setDefaultAccount(String defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    /**
     * 获取单账户 API Key。
     *
     * @return API Key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * 设置单账户 API Key。
     *
     * @param apiKey API Key
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 获取单账户 Secret Key。
     *
     * @return Secret Key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 设置单账户 Secret Key。
     *
     * @param secretKey Secret Key
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 获取单账户 Passphrase。
     *
     * @return Passphrase
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * 设置单账户 Passphrase。
     *
     * @param passphrase Passphrase
     */
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    /**
     * 获取 HTTP 客户端配置。
     *
     * @return HTTP 客户端配置
     */
    public OkxHttpConfig getHttp() {
        return http;
    }

    /**
     * 设置 HTTP 客户端配置。
     *
     * @param http HTTP 客户端配置
     */
    public void setHttp(OkxHttpConfig http) {
        this.http = http;
    }

    /**
     * 获取端点配置。
     *
     * @return 端点配置
     */
    public OkxEndpointConfig getEndpoints() {
        return endpoints;
    }

    /**
     * 设置端点配置。
     *
     * @param endpoints 端点配置
     */
    public void setEndpoints(OkxEndpointConfig endpoints) {
        this.endpoints = endpoints;
    }

    /**
     * 获取 Live 测试配置。
     *
     * @return Live 测试配置
     */
    public OkxLiveConfig getLive() {
        return live;
    }

    /**
     * 设置 Live 测试配置。
     *
     * @param live Live 测试配置
     */
    public void setLive(OkxLiveConfig live) {
        this.live = live;
    }

    /**
     * 获取命名账户配置映射。
     *
     * @return 账户配置映射
     */
    public Map<String, OkxAccountConfig> getAccounts() {
        return accounts;
    }

    /**
     * 设置命名账户配置映射。
     *
     * @param accounts 账户配置映射
     */
    public void setAccounts(Map<String, OkxAccountConfig> accounts) {
        this.accounts = accounts == null ? new LinkedHashMap<>() : accounts;
    }

    /**
     * 解析 REST API 基础地址。
     *
     * @return REST API 基础地址
     */
    public String resolveRestBaseUrl() {
        return endpoints.getRestBaseUrl();
    }

    /**
     * 解析 WebSocket 公共频道地址。
     *
     * @return WebSocket 公共频道地址
     */
    public String resolveWsPublicUrl() {
        return endpoints.getWsPublicUrl();
    }

    /**
     * 获取当前生效账户配置。
     *
     * @return 当前账户配置，未配置时返回 null
     */
    public OkxAccountConfig getActiveAccount() {
        if (defaultAccount != null && !defaultAccount.isEmpty()) {
            OkxAccountConfig account = accounts.get(defaultAccount);
            if (account != null) {
                return account;
            }
        }
        if (accounts.size() == 1) {
            return accounts.values().iterator().next();
        }
        if (hasRootCredentials()) {
            String accountName = defaultAccount == null || defaultAccount.isEmpty() ? "default" : defaultAccount;
            return createAccountFromRoot(accountName);
        }
        return null;
    }

    /**
     * 判断是否启用 Live 测试。
     *
     * @return 启用返回 true
     */
    public boolean isLiveTestsEnabled() {
        return live != null && live.isEnabled();
    }

    private boolean hasRootCredentials() {
        return apiKey != null && !apiKey.isEmpty()
                && secretKey != null && !secretKey.isEmpty()
                && passphrase != null && !passphrase.isEmpty();
    }

    private OkxAccountConfig createAccountFromRoot(String accountName) {
        OkxAccountConfig account = new OkxAccountConfig();
        account.setName(accountName);
        account.setApiKey(apiKey);
        account.setSecretKey(secretKey);
        account.setPassphrase(passphrase);
        return account;
    }
}
