package io.github.adrianclarkhub.okx.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * OKX SDK 基础异常测试。
 *
 * <p>验证非 API 响应类异常能够保留英文异常消息、原始 cause 和统一异常继承关系。</p>
 */
class OkxExceptionTest {

    /**
     * 验证 SDK 基础异常能够保留英文异常消息。
     */
    @Test
    void shouldPreserveExceptionMessage() {
        OkxException exception = new OkxException("SDK configuration is invalid.");

        assertEquals("SDK configuration is invalid.", exception.getMessage(), "Exception message should be preserved.");
    }

    /**
     * 验证网络异常能够保留底层网络错误。
     */
    @Test
    void shouldPreserveCauseInNetworkException() {
        RuntimeException cause = new RuntimeException("Connection timeout.");

        OkxNetworkException exception = new OkxNetworkException("OKX network request failed.", cause);

        assertSame(cause, exception.getCause(), "Network exception should preserve original cause.");
        assertInstanceOf(OkxException.class, exception, "Network exception should inherit SDK exception.");
    }

    /**
     * 验证序列化异常能够保留底层 JSON 处理错误。
     */
    @Test
    void shouldPreserveCauseInSerializationException() {
        RuntimeException cause = new RuntimeException("JSON parsing failed.");

        OkxSerializationException exception = new OkxSerializationException("OKX response serialization failed.", cause);

        assertSame(cause, exception.getCause(), "Serialization exception should preserve original cause.");
        assertInstanceOf(OkxException.class, exception, "Serialization exception should inherit SDK exception.");
    }

    /**
     * 验证 WebSocket 异常能够保留底层连接错误。
     */
    @Test
    void shouldPreserveCauseInWebSocketException() {
        RuntimeException cause = new RuntimeException("WebSocket connection closed.");

        OkxWebSocketException exception = new OkxWebSocketException("OKX WebSocket connection failed.", cause);

        assertSame(cause, exception.getCause(), "WebSocket exception should preserve original cause.");
        assertInstanceOf(OkxException.class, exception, "WebSocket exception should inherit SDK exception.");
    }
}
