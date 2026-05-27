package io.github.adrianclarkhub.okx.rest.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.exception.OkxApiExceptionFactory;
import io.github.adrianclarkhub.okx.core.exception.OkxNetworkException;
import io.github.adrianclarkhub.okx.core.exception.OkxSerializationException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Map;

/**
 * OKX REST 底层客户端。
 *
 * <p>负责公共 HTTP GET 请求、查询参数拼接、JSON 反序列化和基础错误转换。</p>
 */
public class OkxRestClient {

    private final OkxConfig config;

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final String baseUrl;

    /**
     * 创建 REST 底层客户端。
     *
     * @param config OKX SDK 配置
     * @param httpClient OkHttp 客户端
     * @param objectMapper JSON 对象映射器
     * @param baseUrl REST API 基础地址
     */
    public OkxRestClient(OkxConfig config, OkHttpClient httpClient, ObjectMapper objectMapper, String baseUrl) {
        this.config = config;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
    }

    /**
     * 执行无签名 GET 请求。
     *
     * @param path API 路径
     * @param queryParams 查询参数
     * @param typeReference 响应类型引用
     * @param <T> data 数组元素类型
     * @return REST 通用响应
     */
    public <T> OkxRestResponse<T> get(String path, Map<String, String> queryParams, TypeReference<OkxRestResponse<T>> typeReference) {
        HttpUrl url = buildUrl(path, queryParams);
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = httpClient.newCall(request).execute()) {
            String body = readResponseBody(response);
            OkxRestResponse<T> restResponse = objectMapper.readValue(body, typeReference);
            if (!response.isSuccessful() || restResponse == null || !"0".equals(restResponse.getCode())) {
                String code = restResponse == null ? null : restResponse.getCode();
                String msg = restResponse == null ? body : restResponse.getMsg();
                throw OkxApiExceptionFactory.create(code, msg, response.code(), path);
            }
            return restResponse;
        } catch (IOException e) {
            throw new OkxNetworkException("OKX REST GET request failed: " + path, e);
        } catch (RuntimeException e) {
            if (e instanceof OkxSerializationException) {
                throw e;
            }
            throw e;
        }
    }

    private HttpUrl buildUrl(String path, Map<String, String> queryParams) {
        HttpUrl base = HttpUrl.parse(baseUrl + path);
        if (base == null) {
            throw new IllegalArgumentException("Invalid OKX REST URL: " + baseUrl + path);
        }
        HttpUrl.Builder builder = base.newBuilder();
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                    builder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        return builder.build();
    }

    private String readResponseBody(Response response) {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            throw new OkxSerializationException("OKX REST response body is empty.");
        }
        try {
            return responseBody.string();
        } catch (IOException e) {
            throw new OkxNetworkException("Failed to read OKX REST response body.", e);
        }
    }

    /**
     * 获取 SDK 配置。
     *
     * @return SDK 配置
     */
    public OkxConfig getConfig() {
        return config;
    }
}
