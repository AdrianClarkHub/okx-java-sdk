package io.github.adrianclarkhub.okx.core.enums;

/**
 * OKX 枚举基础接口。
 *
 * <p>用于统一表示 SDK 语义化枚举和 OKX API 原始协议值之间的映射关系。</p>
 */
public interface OkxEnum {

    /**
     * 获取 OKX API 使用的原始协议值。
     *
     * @return OKX API 原始协议值
     */
    String getCode();
}
