package io.github.adrianclarkhub.okx.core.auth;

import io.github.adrianclarkhub.okx.core.exception.OkxSerializationException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * OKX HMAC-SHA256 signer.
 */
public final class OkxSigner {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private OkxSigner() {
    }

    public static String sign(String timestamp, String method, String requestPath, String body, String secretKey) {
        String actualBody = body == null ? "" : body;
        String preHash = timestamp + method.toUpperCase() + requestPath + actualBody;
        return hmacSha256Base64(preHash, secretKey);
    }

    private static String hmacSha256Base64(String value, String secretKey) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] digest = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new OkxSerializationException("Failed to sign OKX request.", e);
        }
    }
}
