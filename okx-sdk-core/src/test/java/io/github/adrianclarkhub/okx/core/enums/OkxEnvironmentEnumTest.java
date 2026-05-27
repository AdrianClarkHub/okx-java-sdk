package io.github.adrianclarkhub.okx.core.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OKX 交易环境枚举测试。
 *
 * <p>验证 SDK 语义化环境枚举和 OKX API 原始环境值之间的映射关系。</p>
 */
class OkxEnvironmentEnumTest {

    /**
     * 验证枚举能够返回 OKX API 要求的原始环境值。
     */
    @Test
    void shouldReturnOriginalOkxCode() {
        assertEquals("1", OkxEnvironmentEnum.PRODUCTION.getCode(), "Production environment code should be 1.");
        assertEquals("2", OkxEnvironmentEnum.DEMO.getCode(), "Demo environment code should be 2.");
    }

    /**
     * 验证已知 OKX 原始环境值可以正确转换为 SDK 枚举。
     */
    @Test
    void shouldResolveKnownCode() {
        assertEquals(
                OkxEnvironmentEnum.PRODUCTION,
                OkxEnvironmentEnum.fromCode("1"),
                "Code 1 should be resolved as production environment."
        );
        assertEquals(
                OkxEnvironmentEnum.DEMO,
                OkxEnvironmentEnum.fromCode("2"),
                "Code 2 should be resolved as demo environment."
        );
    }

    /**
     * 验证默认未知值策略下，SDK 使用 UNKNOWN 兜底而不是直接失败。
     */
    @Test
    void shouldUseUnknownWhenCodeIsUnknownByDefault() {
        OkxEnvironmentEnum environment = OkxEnvironmentEnum.fromCode("3");

        assertEquals(
                OkxEnvironmentEnum.UNKNOWN,
                environment,
                "Unknown environment code should be resolved as UNKNOWN by default."
        );
        assertTrue(environment.isUnknown(), "Resolved environment should be marked as unknown.");
    }

    /**
     * 验证严格未知值策略下，SDK 会抛出异常，帮助调用方及时发现不兼容值。
     */
    @Test
    void shouldThrowExceptionWhenStrategyIsThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OkxEnvironmentEnum.fromCode("3", UnknownEnumStrategyEnum.THROW_EXCEPTION),
                "Unknown environment code should throw exception when strict strategy is enabled."
        );

        assertEquals(
                "Unknown OKX environment code: 3",
                exception.getMessage(),
                "Exception message should keep the original unknown OKX code."
        );
    }
}
