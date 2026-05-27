package io.github.adrianclarkhub.okx.rest.status.request;

import io.github.adrianclarkhub.okx.rest.status.enums.StatusMaintenanceStateEnum;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 系统状态请求测试。
 *
 * <p>验证 SDK 语义化请求对象到 OKX REST 查询参数的转换。</p>
 */
class StatusRequestTest {

    @Test
    void shouldReturnEmptyQueryParamsWhenStateIsNull() {
        // Given：未指定 state，符合 OKX 默认查询等待中、进行中和预开放数据的语义。
        StatusRequest request = new StatusRequest();

        // When：转换为查询参数。
        Map<String, String> queryParams = request.toQueryParams();

        // Then：不应发送 state 参数。
        assertTrue(queryParams.isEmpty(), "Query params should be empty when state is null.");
    }

    @Test
    void shouldIncludeStateQueryParamWhenStateIsKnown() {
        // Given：指定 completed 状态。
        StatusRequest request = new StatusRequest(StatusMaintenanceStateEnum.COMPLETED);

        // When：转换为查询参数。
        Map<String, String> queryParams = request.toQueryParams();

        // Then：应发送 OKX 原始 state 值。
        assertEquals("completed", queryParams.get("state"), "State query param should use OKX raw value.");
    }

    @Test
    void shouldSkipStateQueryParamWhenStateIsUnknown() {
        // Given：状态为 UNKNOWN，不能向 OKX 发送空字符串魔法值。
        StatusRequest request = new StatusRequest(StatusMaintenanceStateEnum.UNKNOWN);

        // When：转换为查询参数。
        Map<String, String> queryParams = request.toQueryParams();

        // Then：UNKNOWN 不应参与请求参数。
        assertFalse(queryParams.containsKey("state"), "Unknown state should not be sent as query param.");
    }
}
