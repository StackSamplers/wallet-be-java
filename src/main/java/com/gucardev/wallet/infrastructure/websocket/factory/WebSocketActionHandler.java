package com.gucardev.wallet.infrastructure.websocket.factory;

import com.gucardev.wallet.infrastructure.websocket.dto.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketActionHandler {
    void handle(WebSocketSession session, WebSocketMessage<?> message) throws Exception;
}