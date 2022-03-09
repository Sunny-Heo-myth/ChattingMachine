package com.sunny.chattingmachine.handler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // socket connection
        super.afterConnectionEstablished(session);
        sessionHashMap.put(session.getId(), session);
        JSONObject obj = new JSONObject();
        obj.put("type", "getId");
        obj.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(obj.toJSONString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // socket disconnection
        sessionHashMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    private static JSONObject JsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }


}
