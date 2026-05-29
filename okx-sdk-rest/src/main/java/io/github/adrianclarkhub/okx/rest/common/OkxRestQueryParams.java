package io.github.adrianclarkhub.okx.rest.common;

import io.github.adrianclarkhub.okx.core.enums.OkxEnum;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class OkxRestQueryParams {

    private final Map<String, String> values = new LinkedHashMap<String, String>();

    public OkxRestQueryParams add(String name, String value) {
        if (name != null && value != null && !value.trim().isEmpty()) {
            values.put(name, value.trim());
        }
        return this;
    }

    public OkxRestQueryParams add(String name, OkxEnum value) {
        if (value != null) {
            add(name, value.getCode());
        }
        return this;
    }

    public Map<String, String> toMap() {
        return Collections.unmodifiableMap(new LinkedHashMap<String, String>(values));
    }
}
