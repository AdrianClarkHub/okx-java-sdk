package io.github.adrianclarkhub.okx.websocket.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OkxWebSocketLoginRequest {

    private String op;

    private List<OkxWebSocketLoginArg> args;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public List<OkxWebSocketLoginArg> getArgs() {
        return args;
    }

    public void setArgs(List<OkxWebSocketLoginArg> args) {
        this.args = args;
    }
}
