package io.github.adrianclarkhub.okx.core.exception;

import io.github.adrianclarkhub.okx.core.error.OkxErrorClassificationEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OKX API 异常工厂测试。
 *
 * <p>验证 SDK 能够根据 OKX 错误码和 HTTP 状态码创建更具体的异常类型。</p>
 */
class OkxApiExceptionFactoryTest {

    /**
     * 验证 501xx 鉴权错误会创建鉴权异常。
     */
    @Test
    void shouldCreateAuthExceptionForAuthErrorCode() {
        OkxApiException exception = OkxApiExceptionFactory.create(
                "50102",
                "Timestamp request expired.",
                401,
                "/api/v5/account/balance"
        );

        assertInstanceOf(OkxAuthException.class, exception, "Code 50102 should be classified as authentication error.");
        assertEquals("50102", exception.getOkxCode(), "Authentication error code should be preserved.");
        assertEquals(OkxErrorClassificationEnum.AUTHENTICATION, exception.getErrorClassification(),
                "Catalog classification should be preserved.");
    }

    /**
     * 验证 429 或限流错误码会创建限流异常。
     */
    @Test
    void shouldCreateRateLimitExceptionForRateLimitError() {
        OkxApiException exception = OkxApiExceptionFactory.create(
                "50011",
                "Too many requests.",
                429,
                "/api/v5/account/balance"
        );

        assertInstanceOf(OkxRateLimitException.class, exception, "Code 50011 with HTTP 429 should be classified as rate limit error.");
        assertTrue(exception.getMessage().contains("code=50011"), "Exception message should contain OKX raw code.");
    }

    /**
     * 验证参数错误会创建参数校验异常。
     */
    @Test
    void shouldCreateValidationExceptionForParameterError() {
        OkxApiException exception = OkxApiExceptionFactory.create(
                "50014",
                "Required parameter cannot be empty.",
                400,
                "/api/v5/trade/order"
        );

        assertInstanceOf(OkxValidationException.class, exception, "Code 50014 should be classified as validation error.");
        assertEquals(400, exception.getHttpStatus(), "HTTP status should be preserved.");
    }

    /**
     * 验证服务端错误会创建服务端异常。
     */
    @Test
    void shouldCreateServerExceptionForServerError() {
        OkxApiException exception = OkxApiExceptionFactory.create(
                "50026",
                "System error.",
                500,
                "/api/v5/system/status"
        );

        assertInstanceOf(OkxServerException.class, exception, "Code 50026 should be classified as server error.");
    }

    /**
     * 验证 HTTP 200 但 OKX 业务码失败时会创建业务异常。
     */
    @Test
    void shouldCreateBusinessExceptionForHttpOkButBusinessFailed() {
        OkxApiException exception = OkxApiExceptionFactory.create(
                "51008_1000",
                "Insufficient balance.",
                200,
                "/api/v5/trade/order"
        );

        assertInstanceOf(OkxBusinessException.class, exception, "HTTP 200 with non-zero OKX code should be classified as business error.");
        assertEquals("51008", exception.getOkxCode(), "Main OKX code should be parsed from raw code.");
        assertEquals("1000", exception.getOkxSubCode(), "Sub OKX code should be parsed from raw code.");
        assertEquals("交易类", exception.getErrorCodeInfo().getSection(), "Catalog section should be attached.");
    }

    /**
     * 验证即使 HTTP 状态未传入，目录中的错误分类仍会生效。
     */
    @Test
    void shouldUseCatalogClassificationWhenHttpStatusIsMissing() {
        OkxApiException exception = OkxApiExceptionFactory.create(
                "50000",
                "",
                null,
                "/api/v5/trade/order"
        );

        assertInstanceOf(OkxValidationException.class, exception, "Catalog code 50000 should be classified as validation.");
        assertTrue(exception.getMessage().contains("Body for POST request cannot be empty."),
                "Exception message should use official English catalog metadata when OKX message is empty.");
        assertFalse(exception.getMessage().contains("POST请求的body不能为空"),
                "Exception message should not expose Chinese catalog metadata.");
        assertTrue(exception.getErrorCodeInfo().getMessageZhCn().contains("POST请求的body不能为空"),
                "Chinese catalog metadata should remain available from error code info.");
        assertEquals("Body for POST request cannot be empty.", exception.getErrorCodeInfo().getMessageEnUs(),
                "English catalog message should be available from error code info.");
    }
}
