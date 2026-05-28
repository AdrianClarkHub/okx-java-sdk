package io.github.adrianclarkhub.okx.core.config;

/**
 * OKX SDK Live 测试配置。
 *
 * <p>用于控制是否访问 OKX 公网真实 API，默认关闭。生产业务代码不应依赖该配置。</p>
 */
public class OkxLiveConfig {

    private boolean enabled;

    /**
     * 创建默认 Live 测试配置。
     */
    public OkxLiveConfig() {
        this.enabled = false;
    }

    /**
     * 判断是否启用 Live 测试。
     *
     * @return 启用返回 true，否则返回 false
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用 Live 测试。
     *
     * @param enabled 是否启用 Live 测试
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
