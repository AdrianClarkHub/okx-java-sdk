package io.github.adrianclarkhub.okx.core.enums;

/**
 * 未知枚举值处理策略。
 *
 * <p>当 OKX API 返回 SDK 当前版本未识别的枚举值时，可通过该策略决定使用 UNKNOWN 兜底或抛出异常。</p>
 */
public enum UnknownEnumStrategyEnum {

    /**
     * 使用 UNKNOWN 枚举值兜底。
     */
    USE_UNKNOWN,

    /**
     * 遇到未知枚举值时抛出异常。
     */
    THROW_EXCEPTION
}
