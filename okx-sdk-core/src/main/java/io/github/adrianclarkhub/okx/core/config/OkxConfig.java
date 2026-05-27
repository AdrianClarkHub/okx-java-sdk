package io.github.adrianclarkhub.okx.core.config;

import io.github.adrianclarkhub.okx.core.enums.OkxEnvironmentEnum;
import io.github.adrianclarkhub.okx.core.enums.UnknownEnumStrategyEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * OKX SDK 根配置。
 *
 * <p>用于保存环境、账户、HTTP 客户端和未知枚举处理策略等 SDK 全局配置。</p>
 */
public class OkxConfig {

    private OkxEnvironmentEnum environment;

    private UnknownEnumStrategyEnum unknownEnumStrategy;

    private OkxHttpConfig http;

    private List<OkxAccountConfig> accounts;

    /**
     * 创建默认 SDK 配置。
     */
    public OkxConfig() {
        this.environment = OkxEnvironmentEnum.PRODUCTION;
        this.unknownEnumStrategy = UnknownEnumStrategyEnum.USE_UNKNOWN;
        this.http = new OkxHttpConfig();
        this.accounts = new ArrayList<>();
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
     * 获取账户配置列表。
     *
     * @return 账户配置列表
     */
    public List<OkxAccountConfig> getAccounts() {
        return accounts;
    }

    /**
     * 设置账户配置列表。
     *
     * @param accounts 账户配置列表
     */
    public void setAccounts(List<OkxAccountConfig> accounts) {
        this.accounts = accounts;
    }
}
