package io.github.adrianclarkhub.okx.rest.support.request;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 公告请求测试。
 *
 * <p>验证 SDK 请求对象到 OKX REST 查询参数的转换。</p>
 */
class AnnouncementsRequestTest {

    @Test
    void shouldReturnEmptyQueryParamsWhenFieldsAreBlank() {
        // Given：未指定公告类型和页码。
        AnnouncementsRequest request = new AnnouncementsRequest(" ", "");

        // When：转换为查询参数。
        Map<String, String> queryParams = request.toQueryParams();

        // Then：空白值不应参与请求。
        assertTrue(queryParams.isEmpty(), "Query params should be empty when fields are blank.");
    }

    @Test
    void shouldIncludeAnnouncementTypeAndPageQueryParams() {
        // Given：指定公告类型和页码。
        AnnouncementsRequest request = new AnnouncementsRequest(" announcements-new-listings ", " 2 ");

        // When：转换为查询参数。
        Map<String, String> queryParams = request.toQueryParams();

        // Then：参数名和值应符合 OKX API 文档。
        assertEquals("announcements-new-listings", queryParams.get("annType"), "Announcement type should be sent.");
        assertEquals("2", queryParams.get("page"), "Page should be sent.");
    }
}
