package io.github.adrianclarkhub.okx.core.auth;

import io.github.adrianclarkhub.okx.core.config.OkxAccountConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxValidationException;

/**
 * OKX API credential holder.
 */
public class OkxCredentials {

    private final String apiKey;

    private final String secretKey;

    private final String passphrase;

    public OkxCredentials(String apiKey, String secretKey, String passphrase) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;
    }

    public static OkxCredentials fromConfig(OkxConfig config) {
        if (config == null) {
            throw validationException("OKX config is required for private API requests.");
        }
        config.normalize();
        OkxAccountConfig account = config.getActiveAccount();
        if (account == null) {
            throw validationException("OKX account credentials are required for private API requests.");
        }
        OkxCredentials credentials = new OkxCredentials(account.getApiKey(), account.getSecretKey(), account.getPassphrase());
        credentials.validate();
        return credentials;
    }

    public void validate() {
        if (isBlank(apiKey)) {
            throw validationException("OKX API key is required.");
        }
        if (isBlank(secretKey)) {
            throw validationException("OKX secret key is required.");
        }
        if (isBlank(passphrase)) {
            throw validationException("OKX passphrase is required.");
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static OkxValidationException validationException(String message) {
        return new OkxValidationException(message, null, message, null, null);
    }
}
