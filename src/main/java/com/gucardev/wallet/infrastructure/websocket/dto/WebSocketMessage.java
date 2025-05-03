package com.gucardev.wallet.infrastructure.websocket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gucardev.wallet.infrastructure.websocket.enumeration.WebSocketAction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebSocketMessage<T> {
    private int code;
    private String desc;
    private T payload;

    @JsonIgnore
    private WebSocketAction actionType;

    public WebSocketMessage(WebSocketAction actionType, T payload) {
        this.actionType = actionType;
        this.code = actionType.getCode();
        this.desc = actionType.getDesc();
        this.payload = payload;
    }

    public void setCode(int code) {
        this.code = code;
        var actionType = WebSocketAction.fromCode(code);
        this.actionType = actionType;
        this.desc = actionType.getDesc();
    }

    public WebSocketAction getActionType() {
        if (actionType == null) {
            actionType = WebSocketAction.fromCode(code);
        }
        return actionType;
    }
}