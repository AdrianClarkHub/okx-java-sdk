package io.github.adrianclarkhub.okx.core.error;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OKX 错误码目录测试。
 *
 * <p>验证 14 错误码文档生成的 SDK 目录可以完整加载和查询。</p>
 */
class OkxErrorCodeCatalogTest {

    @Test
    void shouldLoadAllDocumentedErrorCodes() {
        assertEquals(1105, OkxErrorCodeCatalog.size(), "Catalog should contain all documented error code rows.");
    }

    @Test
    void shouldFindRestErrorCodeWithMetadata() {
        Optional<OkxErrorCodeInfo> errorCodeInfo = OkxErrorCodeCatalog.find("50014");

        assertTrue(errorCodeInfo.isPresent(), "Error code 50014 should exist.");
        assertEquals(OkxErrorCodeEnum.CODE_50014, errorCodeInfo.get().getErrorCode(),
                "Error code enum should be attached.");
        assertEquals(OkxErrorTransportEnum.REST_API, errorCodeInfo.get().getTransport(), "Transport should be REST API.");
        assertEquals("公共", errorCodeInfo.get().getCategory(), "Category should match document heading.");
        assertEquals("通用类", errorCodeInfo.get().getSection(), "Section should match document heading.");
        assertEquals(Integer.valueOf(400), errorCodeInfo.get().getHttpStatus(), "HTTP status should be parsed.");
        assertEquals(OkxErrorClassificationEnum.VALIDATION, errorCodeInfo.get().getClassification(),
                "Required parameter error should be classified as validation.");
        assertEquals("Parameter {param0} can not be empty.", errorCodeInfo.get().getMessageEnUs(),
                "English SDK message should be available.");
    }

    @Test
    void shouldFindExactSubCodeBeforeMainCodeFallback() {
        Optional<OkxErrorCodeInfo> errorCodeInfo = OkxErrorCodeCatalog.find("51008_1000");

        assertTrue(errorCodeInfo.isPresent(), "Exact sub code should exist.");
        assertEquals(OkxErrorCodeEnum.CODE_51008_1000, errorCodeInfo.get().getErrorCode(),
                "Exact sub code enum should be returned.");
        assertEquals("51008_1000", errorCodeInfo.get().getCode(), "Exact sub code should be returned.");
        assertEquals("交易类", errorCodeInfo.get().getSection(), "Sub code section should match document heading.");
        assertTrue(errorCodeInfo.get().getMessageZhCn().contains("可用余额不足"),
                "Sub code message should come from documented entry.");
        assertEquals("Order failed. Insufficient {param0} balance in account", errorCodeInfo.get().getMessageEnUs(),
                "English SDK message should be available for sub code.");
    }

    @Test
    void shouldFallbackToMainCodeWhenUnknownSubCodeIsRequested() {
        Optional<OkxErrorCodeInfo> errorCodeInfo = OkxErrorCodeCatalog.find("51000_9999");

        assertTrue(errorCodeInfo.isPresent(), "Unknown sub code should fallback to main code.");
        assertEquals(OkxErrorCodeEnum.CODE_51000, errorCodeInfo.get().getErrorCode(),
                "Main code enum should be returned as fallback.");
        assertEquals("51000", errorCodeInfo.get().getCode(), "Main code should be returned as fallback.");
    }

    @Test
    void shouldBuildEnglishFallbackWhenOfficialEnglishMessageIsMissing() {
        Optional<OkxErrorCodeInfo> errorCodeInfo = OkxErrorCodeCatalog.find("59320");

        assertTrue(errorCodeInfo.isPresent(), "Error code 59320 should exist.");
        assertEquals("Documented OKX business code 59320.", errorCodeInfo.get().getMessageEnUs(),
                "SDK should provide an English fallback when the English document has no matching row.");
    }

    @Test
    void shouldIncludeEnglishOnlyDocumentedRows() {
        Optional<OkxErrorCodeInfo> errorCodeInfo = OkxErrorCodeCatalog.find("58407");

        assertTrue(errorCodeInfo.isPresent(), "English-only error code 58407 should exist.");
        assertEquals(OkxErrorCodeEnum.CODE_58407, errorCodeInfo.get().getErrorCode(),
                "English-only error code enum should be generated.");
        assertEquals("API withdrawal isn't allowed for this payment method. Withdraw using OKX app or web",
                errorCodeInfo.get().getMessageEnUs(), "English-only documented message should be preserved.");
    }

    @Test
    void shouldPreserveDuplicateDocumentedMessages() {
        List<OkxErrorCodeInfo> entries = OkxErrorCodeCatalog.findAll("51024");

        assertTrue(entries.size() > 1, "Duplicated OKX code entries should be preserved.");
    }

    @Test
    void shouldFindWebSocketErrorAndCloseFrame() {
        Optional<OkxErrorCodeInfo> webSocketError = OkxErrorCodeCatalog.find("60007");
        Optional<OkxErrorCodeInfo> closeFrame = OkxErrorCodeCatalog.find("4004");

        assertTrue(webSocketError.isPresent(), "WebSocket error code 60007 should exist.");
        assertEquals(OkxErrorTransportEnum.WEBSOCKET, webSocketError.get().getTransport(),
                "Transport should be WebSocket.");
        assertEquals(OkxErrorClassificationEnum.AUTHENTICATION, webSocketError.get().getClassification(),
                "Invalid signature should be classified as authentication.");
        assertTrue(closeFrame.isPresent(), "WebSocket close frame 4004 should exist.");
        assertEquals(OkxErrorClassificationEnum.WEBSOCKET_CLOSE, closeFrame.get().getClassification(),
                "Close frame should use close frame classification.");
    }

    @Test
    void shouldFindErrorCodeEnumByRawCode() {
        assertEquals(OkxErrorCodeEnum.CODE_50014, OkxErrorCodeEnum.fromCode("50014").orElse(null),
                "Known REST code should resolve to enum.");
        assertEquals(OkxErrorCodeEnum.CODE_51008_1000, OkxErrorCodeEnum.fromCode("51008_1000").orElse(null),
                "Known REST sub code should resolve to enum.");
        assertEquals(OkxErrorCodeEnum.CODE_60007, OkxErrorCodeEnum.fromCode("60007").orElse(null),
                "Known WebSocket code should resolve to enum.");
        assertFalse(OkxErrorCodeEnum.fromCode("999999").isPresent(), "Unknown code should not resolve to enum.");
    }

    @Test
    void shouldReturnEmptyWhenCodeIsUnknown() {
        assertFalse(OkxErrorCodeCatalog.find("999999").isPresent(), "Unknown code should not exist.");
        assertTrue(OkxErrorCodeCatalog.findAll("999999").isEmpty(), "Unknown code list should be empty.");
    }
}
