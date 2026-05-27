package io.github.adrianclarkhub.okx.core.config;

/**
 * OKX 代理配置。
 *
 * <p>用于配置 HTTP 或 WebSocket 请求使用的网络代理。</p>
 */
public class OkxProxyConfig {

    private boolean enabled;

    private String host;

    private int port;

    private String username;

    private String password;

    /**
     * 创建默认代理配置。
     */
    public OkxProxyConfig() {
        this.enabled = false;
    }

    /**
     * 判断是否启用代理。
     *
     * @return 启用代理返回 true，否则返回 false
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用代理。
     *
     * @param enabled 是否启用代理
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取代理主机。
     *
     * @return 代理主机
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置代理主机。
     *
     * @param host 代理主机
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取代理端口。
     *
     * @return 代理端口
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置代理端口。
     *
     * @param port 代理端口
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 获取代理用户名。
     *
     * @return 代理用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置代理用户名。
     *
     * @param username 代理用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取代理密码。
     *
     * @return 代理密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置代理密码。
     *
     * @param password 代理密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
