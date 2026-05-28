package io.github.adrianclarkhub.okx.core.auth;

/**
 * OKX REST authentication header names.
 */
public final class OkxAuthHeaders {

    public static final String ACCESS_KEY = "OK-ACCESS-KEY";

    public static final String ACCESS_SIGN = "OK-ACCESS-SIGN";

    public static final String ACCESS_TIMESTAMP = "OK-ACCESS-TIMESTAMP";

    public static final String ACCESS_PASSPHRASE = "OK-ACCESS-PASSPHRASE";

    public static final String SIMULATED_TRADING = "x-simulated-trading";

    private OkxAuthHeaders() {
    }
}
