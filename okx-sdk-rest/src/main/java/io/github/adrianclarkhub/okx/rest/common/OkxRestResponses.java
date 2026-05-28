package io.github.adrianclarkhub.okx.rest.common;

import java.util.Collections;
import java.util.List;

public final class OkxRestResponses {

    private OkxRestResponses() {
    }

    public static <T> List<T> dataOrEmpty(OkxRestResponse<T> response) {
        if (response == null || response.getData() == null) {
            return Collections.emptyList();
        }
        return response.getData();
    }
}
