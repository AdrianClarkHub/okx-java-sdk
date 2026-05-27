package io.github.adrianclarkhub.okx.core.config;

/**
 * OKX HTTP 客户端配置。
 *
 * <p>用于配置 REST API 请求的超时时间、重试次数和代理信息。</p>
 */
public class OkxHttpConfig {

    private int connectTimeoutMillis;

    private int readTimeoutMillis;

    private int writeTimeoutMillis;

    private int maxRetries;

    private OkxProxyConfig proxy;

    /**
     * 创建默认 HTTP 配置。
     */
    public OkxHttpConfig() {
        this.connectTimeoutMillis = 10000;
        this.readTimeoutMillis = 30000;
        this.writeTimeoutMillis = 30000;
        this.maxRetries = 0;
        this.proxy = new OkxProxyConfig();
    }

    /**
     * 获取连接超时时间，单位毫秒。
     *
     * @return 连接超时时间
     */
    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    /**
     * 设置连接超时时间，单位毫秒。
     *
     * @param connectTimeoutMillis 连接超时时间
     */
    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    /**
     * 获取读取超时时间，单位毫秒。
     *
     * @return 读取超时时间
     */
    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    /**
     * 设置读取超时时间，单位毫秒。
     *
     * @param readTimeoutMillis 读取超时时间
     */
    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    /**
     * 获取写入超时时间，单位毫秒。
     *
     * @return 写入超时时间
     */
    public int getWriteTimeoutMillis() {
        return writeTimeoutMillis;
    }

    /**
     * 设置写入超时时间，单位毫秒。
     *
     * @param writeTimeoutMillis 写入超时时间
     */
    public void setWriteTimeoutMillis(int writeTimeoutMillis) {
        this.writeTimeoutMillis = writeTimeoutMillis;
    }

    /**
     * 获取最大重试次数。
     *
     * @return 最大重试次数
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * 设置最大重试次数。
     *
     * @param maxRetries 最大重试次数
     */
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * 获取代理配置。
     *
     * @return 代理配置
     */
    public OkxProxyConfig getProxy() {
        return proxy;
    }

    /**
     * 设置代理配置。
     *
     * @param proxy 代理配置
     */
    public void setProxy(OkxProxyConfig proxy) {
        this.proxy = proxy;
    }
}
