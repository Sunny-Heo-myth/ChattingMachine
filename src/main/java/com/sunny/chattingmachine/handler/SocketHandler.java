package com.sunny.chattingmachine.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
public class SocketHandler extends TextWebSocketHandler {

    // Mapping for web socket sessions
    HashMap<String, WebSocketSession> sessionHashMap = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // send message
        String stringMessage = message.getPayload();

        for (String key : sessionHashMap.keySet()) {
            WebSocketSession webSocketSession = sessionHashMap.get(key);

            try {
                webSocketSession.sendMessage(new TextMessage(stringMessage));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // socket connection
        super.afterConnectionEstablished(session);
        sessionHashMap.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // socket disconnection
        sessionHashMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }
}
