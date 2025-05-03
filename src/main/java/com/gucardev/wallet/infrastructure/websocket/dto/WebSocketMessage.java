package com.gucardev.wallet.infrastructure.websocket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gucardev.wallet.infrastructure.websocket.enumeration.WebSocketAction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebSocketMessage<T> {
    private int action;
    private String desc;
    private T payload;

    @JsonIgnore
    private WebSocketAction actionType;

    public WebSocketMessage(WebSocketAction actionType, T payload) {
        this.actionType = actionType;
        this.action = actionType.getCode();
        this.desc = actionType.getDesc();
        this.payload = payload;
    }

    public void setAction(int action) {
        this.action = action;
        var actionType = WebSocketAction.fromCode(action);
        this.actionType = actionType;
        this.desc = actionType.getDesc();
    }

    public WebSocketAction getActionType() {
        if (actionType == null) {
            actionType = WebSocketAction.fromCode(action);
        }
        return actionType;
    }
}