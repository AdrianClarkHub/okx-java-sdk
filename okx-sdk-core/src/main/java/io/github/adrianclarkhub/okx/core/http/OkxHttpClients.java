package io.github.adrianclarkhub.okx.core.http;

import io.github.adrianclarkhub.okx.core.config.OkxHttpConfig;
import io.github.adrianclarkhub.okx.core.config.OkxProxyConfig;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * OKX OkHttp 客户端工厂。
 */
public final class OkxHttpClients {

    private OkxHttpClients() {
    }

    /**
     * 根据 HTTP 配置创建 OkHttpClient。
     *
     * @param httpConfig HTTP 配置
     * @return OkHttpClient
     */
    public static OkHttpClient create(OkxHttpConfig httpConfig) {
        OkxHttpConfig actualConfig = httpConfig == null ? new OkxHttpConfig() : httpConfig;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(actualConfig.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(actualConfig.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(actualConfig.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS);

        OkxProxyConfig proxyConfig = actualConfig.getProxy();
        if (proxyConfig != null && proxyConfig.isEnabled()) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort())));
            if (proxyConfig.getUsername() != null && !proxyConfig.getUsername().isEmpty()) {
                builder.proxyAuthenticator(proxyAuthenticator(proxyConfig));
            }
        }
        return builder.build();
    }

    private static Authenticator proxyAuthenticator(OkxProxyConfig proxyConfig) {
        return (route, response) -> {
            String credential = Credentials.basic(proxyConfig.getUsername(), proxyConfig.getPassword() == null ? "" : proxyConfig.getPassword());
            return response.request().newBuilder().header("Proxy-Authorization", credential).build();
        };
    }
}
