package io.github.adrianclarkhub.okx.rest.status.enums;

import io.github.adrianclarkhub.okx.core.enums.OkxEnum;
import io.github.adrianclarkhub.okx.core.enums.UnknownEnumStrategyEnum;

/**
 * 系统维护状态枚举。
 *
 * <p>对应 OKX Status API 的 state 字段：scheduled、ongoing、pre_open、completed、canceled。</p>
 */
public enum StatusMaintenanceStateEnum implements OkxEnum {

    /**
     * 等待中，对应 OKX 原始值 {@code scheduled}。
     */
    SCHEDULED("scheduled"),

    /**
     * 进行中，对应 OKX 原始值 {@code ongoing}。
     */
    ONGOING("ongoing"),

    /**
     * 预开放，对应 OKX 原始值 {@code pre_open}。
     */
    PRE_OPEN("pre_open"),

    /**
     * 已完成，对应 OKX 原始值 {@code completed}。
     */
    COMPLETED("completed"),

    /**
     * 已取消，对应 OKX 原始值 {@code canceled}。
     */
    CANCELED("canceled"),

    /**
     * 未知状态，用于兼容 OKX 后续新增的状态值。
     */
    UNKNOWN("");

    private final String code;

    StatusMaintenanceStateEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    /**
     * 根据 OKX 原始状态值解析枚举。
     *
     * @param code OKX 原始状态值
     * @return 系统维护状态枚举，未知值返回 {@link #UNKNOWN}
     */
    public static StatusMaintenanceStateEnum fromCode(String code) {
        return fromCode(code, UnknownEnumStrategyEnum.USE_UNKNOWN);
    }

    /**
     * 根据 OKX 原始状态值和未知值处理策略解析枚举。
     *
     * @param code OKX 原始状态值
     * @param strategy 未知枚举值处理策略
     * @return 系统维护状态枚举
     * @throws IllegalArgumentException 当策略为 {@link UnknownEnumStrategyEnum#THROW_EXCEPTION} 且原始值未知时抛出
     */
    public static StatusMaintenanceStateEnum fromCode(String code, UnknownEnumStrategyEnum strategy) {
        for (StatusMaintenanceStateEnum state : values()) {
            if (!state.isUnknown() && state.code.equals(code)) {
                return state;
            }
        }
        if (UnknownEnumStrategyEnum.THROW_EXCEPTION.equals(strategy)) {
            throw new IllegalArgumentException("Unknown OKX status maintenance state code: " + code);
        }
        return UNKNOWN;
    }

    /**
     * 判断当前枚举是否为未知状态。
     *
     * @return 如果是未知状态返回 true，否则返回 false
     */
    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}
