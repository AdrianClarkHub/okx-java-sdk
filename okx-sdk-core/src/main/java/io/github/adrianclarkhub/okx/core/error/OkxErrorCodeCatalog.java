package io.github.adrianclarkhub.okx.core.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * OKX 错误码目录。
 *
 * <p>目录数据来自 OKX API 错误码文档，覆盖 REST API 错误码、WebSocket 错误码和 WebSocket 关闭帧。</p>
 */
public final class OkxErrorCodeCatalog {

    private static final List<OkxErrorCodeInfo> ALL = OkxErrorCodeCatalogData.all();

    private static final Map<String, List<OkxErrorCodeInfo>> BY_CODE = indexByCode(ALL);

    private OkxErrorCodeCatalog() {
    }

    /**
     * 获取全部错误码目录项。
     *
     * @return 不可变目录列表
     */
    public static List<OkxErrorCodeInfo> all() {
        return ALL;
    }

    /**
     * 按错误码查找第一个目录项。
     *
     * <p>如果传入的是带子码格式但目录中不存在完全匹配项，会回退查询主错误码。</p>
     *
     * @param rawCode OKX 原始错误码
     * @return 错误码目录项
     */
    public static Optional<OkxErrorCodeInfo> find(String rawCode) {
        List<OkxErrorCodeInfo> items = findAll(rawCode);
        if (items.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(items.get(0));
    }

    /**
     * 按错误码和来源协议查找第一个目录项。
     *
     * <p>REST API、WebSocket 事件和 WebSocket 关闭帧可能复用相同数字码。调用链已知来源协议时应优先使用该方法。</p>
     *
     * @param rawCode OKX 原始错误码
     * @param transport 来源协议
     * @return 错误码目录项
     */
    public static Optional<OkxErrorCodeInfo> find(String rawCode, OkxErrorTransportEnum transport) {
        List<OkxErrorCodeInfo> items = findAll(rawCode, transport);
        if (items.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(items.get(0));
    }

    /**
     * 按错误码查找所有目录项。
     *
     * <p>部分 OKX 错误码在文档中有多个提示文案，该方法保留全部条目。</p>
     *
     * @param rawCode OKX 原始错误码
     * @return 不可变目录列表
     */
    public static List<OkxErrorCodeInfo> findAll(String rawCode) {
        if (rawCode == null || rawCode.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String normalizedCode = rawCode.trim();
        List<OkxErrorCodeInfo> exact = BY_CODE.get(normalizedCode);
        if (exact != null) {
            return exact;
        }
        String mainCode = parseMainCode(normalizedCode);
        if (!normalizedCode.equals(mainCode)) {
            List<OkxErrorCodeInfo> fallback = BY_CODE.get(mainCode);
            if (fallback != null) {
                return fallback;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 按错误码和来源协议查找所有目录项。
     *
     * @param rawCode OKX 原始错误码
     * @param transport 来源协议
     * @return 不可变目录列表
     */
    public static List<OkxErrorCodeInfo> findAll(String rawCode, OkxErrorTransportEnum transport) {
        List<OkxErrorCodeInfo> items = findAll(rawCode);
        if (transport == null || items.isEmpty()) {
            return items;
        }
        List<OkxErrorCodeInfo> filtered = new ArrayList<>();
        for (OkxErrorCodeInfo item : items) {
            if (transport.equals(item.getTransport())) {
                filtered.add(item);
            }
        }
        return Collections.unmodifiableList(filtered);
    }

    /**
     * 返回目录项数量。
     *
     * @return 目录项数量
     */
    public static int size() {
        return ALL.size();
    }

    private static Map<String, List<OkxErrorCodeInfo>> indexByCode(List<OkxErrorCodeInfo> items) {
        Map<String, List<OkxErrorCodeInfo>> index = new LinkedHashMap<>();
        for (OkxErrorCodeInfo item : items) {
            index.computeIfAbsent(item.getCode(), ignored -> new ArrayList<>()).add(item);
        }
        for (Map.Entry<String, List<OkxErrorCodeInfo>> entry : index.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(index);
    }

    private static String parseMainCode(String rawCode) {
        int index = rawCode.indexOf('_');
        if (index < 0) {
            return rawCode;
        }
        return rawCode.substring(0, index);
    }

}
