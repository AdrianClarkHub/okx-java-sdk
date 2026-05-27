package io.github.adrianclarkhub.okx.rest.status.enums;

import io.github.adrianclarkhub.okx.core.enums.UnknownEnumStrategyEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 系统维护状态枚举测试。
 *
 * <p>验证 OKX 原始 state 值与 SDK 语义化枚举之间的映射关系。</p>
 */
class StatusMaintenanceStateEnumTest {

    @Test
    void shouldReturnExpectedCodeWhenGetCode() {
        // Given / When / Then：确认 SDK 枚举保留 OKX 原始 state 值。
        assertEquals("scheduled", StatusMaintenanceStateEnum.SCHEDULED.getCode(), "Scheduled state code should match OKX raw value.");
        assertEquals("ongoing", StatusMaintenanceStateEnum.ONGOING.getCode(), "Ongoing state code should match OKX raw value.");
        assertEquals("pre_open", StatusMaintenanceStateEnum.PRE_OPEN.getCode(), "Pre-open state code should match OKX raw value.");
        assertEquals("completed", StatusMaintenanceStateEnum.COMPLETED.getCode(), "Completed state code should match OKX raw value.");
        assertEquals("canceled", StatusMaintenanceStateEnum.CANCELED.getCode(), "Canceled state code should match OKX raw value.");
    }

    @Test
    void shouldResolveKnownStateWhenFromCode() {
        // Given / When / Then：确认所有已知 OKX state 值都能解析为语义化枚举。
        assertEquals(StatusMaintenanceStateEnum.SCHEDULED, StatusMaintenanceStateEnum.fromCode("scheduled"), "Scheduled state should be resolved.");
        assertEquals(StatusMaintenanceStateEnum.ONGOING, StatusMaintenanceStateEnum.fromCode("ongoing"), "Ongoing state should be resolved.");
        assertEquals(StatusMaintenanceStateEnum.PRE_OPEN, StatusMaintenanceStateEnum.fromCode("pre_open"), "Pre-open state should be resolved.");
        assertEquals(StatusMaintenanceStateEnum.COMPLETED, StatusMaintenanceStateEnum.fromCode("completed"), "Completed state should be resolved.");
        assertEquals(StatusMaintenanceStateEnum.CANCELED, StatusMaintenanceStateEnum.fromCode("canceled"), "Canceled state should be resolved.");
    }

    @Test
    void shouldReturnUnknownWhenUnknownCodeUsesDefaultStrategy() {
        // Given / When：传入未来可能新增的 OKX state 值。
        StatusMaintenanceStateEnum state = StatusMaintenanceStateEnum.fromCode("future_state");

        // Then：默认策略应返回 UNKNOWN，避免因 OKX 新增枚举值导致客户端不可用。
        assertEquals(StatusMaintenanceStateEnum.UNKNOWN, state, "Unknown state should resolve to UNKNOWN by default.");
        assertTrue(state.isUnknown(), "Unknown state should report isUnknown true.");
    }

    @Test
    void shouldThrowExceptionWhenUnknownCodeUsesThrowStrategy() {
        // Given / When / Then：严格策略下遇到未知 state 值应抛出异常。
        assertThrows(IllegalArgumentException.class,
                () -> StatusMaintenanceStateEnum.fromCode("future_state", UnknownEnumStrategyEnum.THROW_EXCEPTION),
                "Unknown state should throw when strict strategy is used.");
    }
}
