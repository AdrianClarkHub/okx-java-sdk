package io.github.adrianclarkhub.okx.examples;

import io.github.adrianclarkhub.okx.core.config.OkxConfig;
import io.github.adrianclarkhub.okx.core.config.OkxConfigLoader;
import io.github.adrianclarkhub.okx.rest.common.OkxRestClients;
import io.github.adrianclarkhub.okx.rest.status.StatusClient;
import io.github.adrianclarkhub.okx.rest.status.response.StatusResponse;

import java.util.List;

/**
 * 普通 Java REST 示例。
 */
public final class PlainJavaStatusExample {

    private PlainJavaStatusExample() {
    }

    public static void main(String[] args) {
        OkxConfig config = OkxConfigLoader.load();
        StatusClient statusClient = new StatusClient(OkxRestClients.create(config));

        List<StatusResponse> statusList = statusClient.getStatus();
        System.out.println("OKX status item count: " + statusList.size());
        if (!statusList.isEmpty()) {
            System.out.println("First status title: " + statusList.get(0).getTitle());
        }
    }
}
