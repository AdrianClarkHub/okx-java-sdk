package io.github.adrianclarkhub.okx.core.exception;

import io.github.adrianclarkhub.okx.core.error.OkxErrorClassificationEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * OKX API 异常测试。
 *
 * <p>验证 OKX API 异常能够保留错误码、错误子码、HTTP 状态码、请求路径和原始异常。</p>
 */
class OkxApiExceptionTest {

    /**
     * 验证带子码的 OKX 原始错误码可以拆分为主错误码和错误子码。
     */
    @Test
    void shouldParseMainCodeAndSubCodeFromRawCode() {
        OkxApiException exception = new OkxApiException(
                "OKX API request failed.",
                "51008_1000",
                "Insufficient balance.",
                200,
                "/api/v5/trade/order"
        );

        assertEquals("51008_1000", exception.getRawCode(), "Raw OKX code should be preserved.");
        assertEquals("51008", exception.getOkxCode(), "Main OKX code should be parsed from raw code.");
        assertEquals("1000", exception.getOkxSubCode(), "Sub OKX code should be parsed from raw code.");
        assertEquals("Insufficient balance.", exception.getOkxMessage(), "Original OKX message should be preserved.");
        assertEquals(200, exception.getHttpStatus(), "HTTP status should be preserved even when OKX code means failure.");
        assertEquals("/api/v5/trade/order", exception.getRequestPath(), "Request path should be preserved for troubleshooting.");
        assertEquals(OkxErrorClassificationEnum.BUSINESS, exception.getErrorClassification(),
                "Catalog classification should be attached to exception.");
    }

    /**
     * 验证不带子码的 OKX 错误码不会产生错误子码。
     */
    @Test
    void shouldKeepSubCodeNullWhenRawCodeDoesNotContainSubCode() {
        OkxApiException exception = new OkxApiException(
                "OKX API request failed.",
                "50011",
                "Too many requests.",
                429,
                "/api/v5/account/balance"
        );

        assertEquals("50011", exception.getOkxCode(), "Main OKX code should equal raw code when sub code does not exist.");
        assertNull(exception.getOkxSubCode(), "Sub OKX code should be null when raw code does not contain sub code.");
    }

    /**
     * 验证异常能够保留原始 cause，方便排查底层错误。
     */
    @Test
    void shouldPreserveCause() {
        IllegalStateException cause = new IllegalStateException("Original failure.");

        OkxApiException exception = new OkxApiException(
                "OKX API request failed.",
                "50026",
                "System error.",
                500,
                "/api/v5/system/status",
                cause
        );

        assertSame(cause, exception.getCause(), "Cause should be preserved for troubleshooting.");
    }

    /**
     * 验证具体 API 异常类型都继承自统一的 SDK 异常基类。
     */
    @Test
    void shouldKeepApiExceptionHierarchy() {
        OkxApiException exception = new OkxAuthException(
                "OKX authentication failed.",
                "50102",
                "Timestamp request expired.",
                401,
                "/api/v5/account/balance"
        );

        assertInstanceOf(OkxException.class, exception, "API exceptions should be SDK exceptions.");
        assertInstanceOf(OkxApiException.class, exception, "Authentication exceptions should be API exceptions.");
        assertInstanceOf(OkxAuthException.class, exception, "Exception should keep its concrete authentication type.");
    }
}
