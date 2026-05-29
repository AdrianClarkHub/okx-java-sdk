package io.github.adrianclarkhub.okx.core.http;

import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxConfigurationException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * OKX 代理配置工具。
 */
public final class OkxProxySupport {

    private OkxProxySupport() {
    }

    /**
     * 校验代理配置。
     *
     * @param proxyConfig 代理配置
     * @param owner 配置使用方
     */
    public static void validate(OkxProxyConfig proxyConfig, String owner) {
        if (proxyConfig == null || !proxyConfig.isEnabled()) {
            return;
        }
        String prefix = owner == null || owner.isEmpty() ? "OKX" : owner;
        if (proxyConfig.getHost() == null || proxyConfig.getHost().trim().isEmpty()) {
            throw new OkxConfigurationException(prefix + " proxy host is required when proxy is enabled.");
        }
        if (proxyConfig.getPort() <= 0 || proxyConfig.getPort() > 65535) {
            throw new OkxConfigurationException(prefix + " proxy port must be between 1 and 65535 when proxy is enabled.");
        }
    }

    /**
     * 判断代理是否启用了认证。
     *
     * @param proxyConfig 代理配置
     * @return 启用代理认证时返回 true
     */
    public static boolean hasAuthentication(OkxProxyConfig proxyConfig) {
        return proxyConfig != null
                && proxyConfig.getUsername() != null
                && !proxyConfig.getUsername().trim().isEmpty();
    }

    /**
     * 创建 JDK HTTP 客户端使用的代理认证器。
     *
     * @param proxyConfig 代理配置
     * @return 代理认证器
     */
    public static Authenticator jdkAuthenticator(OkxProxyConfig proxyConfig) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String password = proxyConfig.getPassword() == null ? "" : proxyConfig.getPassword();
                return new PasswordAuthentication(proxyConfig.getUsername(), password.toCharArray());
            }
        };
    }
}
