package com.sunny.chattingmachine.handler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class SocketHandler extends TextWebSocketHandler {

    // Mapping for web socket sessions

    // HashMap<String, WebSocketSession> sessionHashMap = new HashMap<>();
    List<HashMap<String, Object>> sessionMapList = new ArrayList<>();

    // send message
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // read TextMessage as a JSONObject
        String stringMessage = message.getPayload();
        JSONObject jsonObject = jsonToObjectParser(stringMessage);
        String jsonRoomId = (String) jsonObject.get("roomId");

        // extract existing session map by using room id
        HashMap<String, Object> tempSessionMap = new HashMap<>();
        if (sessionMapList.size() > 0) {
            for (HashMap<String, Object> sessionMap : sessionMapList) {
                String roomId = (String) sessionMap.get("roomId");
                if (roomId.equals(jsonRoomId)) {
                    tempSessionMap = sessionMap;
                    break;
                }
            }

            // send message for temp map.
            for (String key : tempSessionMap.keySet()) {
                if (key.equals("roomId")) {
                    continue;
                }
                // send messages to client for each value with the key of message in current room map
                WebSocketSession webSocketSession = (WebSocketSession) tempSessionMap.get(key);
                if (webSocketSession != null) {
                    try {
                        webSocketSession.sendMessage(new TextMessage(jsonObject.toJSONString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // socket connection
        super.afterConnectionEstablished(session);
        boolean roomExistence = false;
        String url = session.getUri().toString();
        System.out.println(url);

        // room existence check
        String currentRoomId = url.split("/chatting/")[1];
        int index = sessionMapList.size();
        if (index > 0) {
            for (int i = 0; i < sessionMapList.size(); i++) {
                String roomId = (String) sessionMapList.get(i).get("roomId");
                if (roomId.equals(currentRoomId)) {
                    roomExistence = true;
                    index = i;
                    break;
                }
            }
        }
        // add only a session into map if there is same room
        if (roomExistence) {
            HashMap<String, Object> map = sessionMapList.get(index);
            map.put(session.getId(), session);
        }
        // if there is not add roomId and session into map
        else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("roomId", currentRoomId);
            map.put(session.getId(), session);
            sessionMapList.add(map);
        }

        // send message of issued session id
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "getId");
        jsonObject.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(jsonObject.toJSONString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // socket disconnection
        if (sessionMapList.size() > 0) {
            for (HashMap<String, Object> stringObjectHashMap : sessionMapList) {
                stringObjectHashMap.remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }

    private static JSONObject jsonToObjectParser(String jsonStr) {
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
