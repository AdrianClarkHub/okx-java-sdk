package io.github.adrianclarkhub.okx.core.enums;

/**
 * OKX 交易环境枚举。
 *
 * <p>OKX API 中环境字段使用原始值表示：{@code 1} 表示实盘，{@code 2} 表示模拟盘。</p>
 */
public enum OkxEnvironmentEnum implements OkxEnum {

    /**
     * 实盘环境，对应 OKX 原始值 {@code 1}。
     */
    PRODUCTION("1"),

    /**
     * 模拟盘环境，对应 OKX 原始值 {@code 2}。
     */
    DEMO("2"),

    /**
     * 未知环境，用于兼容 OKX 后续新增的环境值。
     */
    UNKNOWN("");

    private final String code;

    OkxEnvironmentEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 根据 OKX 原始环境值解析环境枚举。
     *
     * @param code OKX 原始环境值
     * @return 环境枚举，未知值返回 {@link #UNKNOWN}
     */
    public static OkxEnvironmentEnum fromCode(String code) {
        return fromCode(code, UnknownEnumStrategyEnum.USE_UNKNOWN);
    }

    /**
     * 根据 OKX 原始环境值和未知值处理策略解析环境枚举。
     *
     * @param code OKX 原始环境值
     * @param strategy 未知枚举值处理策略
     * @return 环境枚举
     * @throws IllegalArgumentException 当策略为 {@link UnknownEnumStrategyEnum#THROW_EXCEPTION} 且原始值未知时抛出
     */
    public static OkxEnvironmentEnum fromCode(String code, UnknownEnumStrategyEnum strategy) {
        for (OkxEnvironmentEnum environment : values()) {
            if (!environment.isUnknown() && environment.code.equals(code)) {
                return environment;
            }
        }
        if (UnknownEnumStrategyEnum.THROW_EXCEPTION.equals(strategy)) {
            throw new IllegalArgumentException("Unknown OKX environment code: " + code);
        }
        return UNKNOWN;
    }

    /**
     * 判断当前枚举是否为未知环境。
     *
     * @return 如果是未知环境返回 true，否则返回 false
     */
    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}
