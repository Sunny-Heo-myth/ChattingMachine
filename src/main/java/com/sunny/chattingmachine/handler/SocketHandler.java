package com.sunny.chattingmachine.handler;

import com.google.gson.Gson;
import com.sunny.chattingmachine.domain.ChatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class SocketHandler extends TextWebSocketHandler {

    private final Gson gson;

    @Autowired
    public SocketHandler(Gson gson) {
        this.gson = gson;
    }

    // data structure of each room is managed as map in server.
    List<HashMap<String, Object>> sessionMapList = new ArrayList<>();
//    private static final String FILE_UPLOAD_PATH = "/Users/sunny/Downloads/chattingMachine";
//    private static int fileUploadIndex = 0;
//    private static String fileUploadSession = "";

    // broadcast message
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // read TextMessage as a JSONObject & get roomId, type
        String stringMessage = message.getPayload();
        ChatTextMessage chatTextMessage = gson.fromJson(stringMessage, ChatTextMessage.class);
        // future room map of sessions to broadcast for client of the room
        HashMap<String, Object> tempSessionMap = new HashMap<>();

        // extract existing session map by using room id
        if (sessionMapList.size() > 0) {
            for (int i = 0; i < sessionMapList.size(); i++) {
                String roomId = (String) sessionMapList.get(i).get("roomId");
                // find room map to find sessions match with room id.
                if (roomId.equals(chatTextMessage.getRoomId())) {
                    tempSessionMap = sessionMapList.get(i);
//                    fileUploadIndex = i;
//                    fileUploadSession = chatTextMessage.getSessionId();
                    break;
                }
            }
            // send only if it is not file upload type
            if (!StringUtils.equals(chatTextMessage.getType(), "fileUpload")) {
                for (String key : tempSessionMap.keySet()) {
                    if (key.equals("roomId")) {
                        continue;
                    }
                    // get every session in map with key
                    WebSocketSession webSocketSession = (WebSocketSession) tempSessionMap.get(key);
                    if (webSocketSession != null) {
                        try {
                            //broadcast message as json
                            webSocketSession.sendMessage(new TextMessage(chatTextMessage.getMessage()));
                        }
                        catch (Exception e) {
                            log.error("broadcast binary fail : {}", webSocketSession.getId(), e);
                        }
                    }
                }
            }
        }
    }

//    @Override
//    public void handleBinaryMessage(WebSocketSession session, BinaryMessage binaryMessage) {
//        ByteBuffer byteBuffer = binaryMessage.getPayload();
//        String fileName = "chattingServer.jpg " + fileUploadIndex;
//        File directory = new File(FILE_UPLOAD_PATH);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        File file = new File(FILE_UPLOAD_PATH, fileName);
//        FileOutputStream outputStream = null;
//        FileChannel outChannel = null;
//
//        try {
//            byteBuffer.flip();
//            outputStream = new FileOutputStream(file, true);
//            outChannel = outputStream.getChannel();
//            byteBuffer.compact();
//            outChannel.write(byteBuffer);
//        }
//        catch (Exception e) {
//            log.error("binary outPutStream instantiate fail : {}", file, e);
//        }
//        finally {
//            // todo try with resource (do not have to write close)
//            try {
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//                if (outChannel != null) {
//                    outChannel.close();
//                }
//            }
//            catch (IOException e) {
//                log.error("binary outPutStream close fail : {}", file, e);
//            }
//        }
//
//        byteBuffer.position(0);
//        // to the room
//        HashMap<String, Object> tempSessionMap = sessionMapList.get(fileUploadIndex);
//        for (String key : tempSessionMap.keySet()) {
//            if (key.equals("roomId")) {
//                continue;
//            }
//            // broadcast binary message
//            WebSocketSession webSocketSession = (WebSocketSession) tempSessionMap.get(key);
//            try {
//                webSocketSession.sendMessage(new BinaryMessage(byteBuffer));
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // socket connection
        super.afterConnectionEstablished(session);
        boolean roomExistence = false;
        String url = Objects.requireNonNull(session.getUri()).toString();

        String currentRoomId = url.split("/chatting/")[1];
        int index = sessionMapList.size();
        if (sessionMapList.size() > 0) {
            for (int i = 0; i < sessionMapList.size(); i++) {
                String roomId = (String) sessionMapList.get(i).get("roomId");
                if (roomId.equals(currentRoomId)) {
                    roomExistence = true;
                    index = i;
                    break;
                }
            }
        }
        // if there is same room
        if (roomExistence) {
            // get the room map
            HashMap<String, Object> map = sessionMapList.get(index);
            // add session id and session into the map
            map.put(session.getId(), session);
        }
        else {
            HashMap<String, Object> map = new HashMap<>();
            // add "roomId" and room id into the map
            map.put("roomId", currentRoomId);
            // add session id and session into the map
            map.put(session.getId(), session);
            sessionMapList.add(map);
        }
        ChatTextMessage chatTextMessage =
                new ChatTextMessage("getId", null, session.getId(), null, null, LocalDateTime.now());
        String jsonMessage = gson.toJson(chatTextMessage);
        session.sendMessage(new TextMessage(jsonMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // delete all sessions with the client's session id for each room.
        if (sessionMapList.size() > 0) {
            for (HashMap<String, Object> sessionMap : sessionMapList) {
                sessionMap.remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }
}
