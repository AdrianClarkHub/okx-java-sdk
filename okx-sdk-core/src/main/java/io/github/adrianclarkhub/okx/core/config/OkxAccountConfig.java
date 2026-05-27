package io.github.adrianclarkhub.okx.core.config;

/**
 * OKX 账户配置。
 *
 * <p>用于保存访问 OKX API 所需的账户凭证和账户标识。真实凭证只能来自本地配置或环境变量，不能提交到 Git。</p>
 */
public class OkxAccountConfig {

    private String name;

    private String apiKey;

    private String secretKey;

    private String passphrase;

    /**
     * 创建默认账户配置。
     */
    public OkxAccountConfig() {
    }

    /**
     * 获取账户名称。
     *
     * @return 账户名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置账户名称。
     *
     * @param name 账户名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 API Key。
     *
     * @return API Key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * 设置 API Key。
     *
     * @param apiKey API Key
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 获取 Secret Key。
     *
     * @return Secret Key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 设置 Secret Key。
     *
     * @param secretKey Secret Key
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 获取 Passphrase。
     *
     * @return Passphrase
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * 设置 Passphrase。
     *
     * @param passphrase Passphrase
     */
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}
