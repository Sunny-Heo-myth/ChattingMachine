package com.sunny.chattingmachine.VO;

import java.time.LocalDate;

public class Room {
    String roomId;
    String roomName;
    String roomManagerAccountId;
    String StateCode;
    LocalDate RegDate;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String chatRoomId) {
        this.roomId = chatRoomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String chatRoomName) {
        this.roomName = chatRoomName;
    }

    public String getRoomManagerAccountId() {
        return roomManagerAccountId;
    }

    public void setRoomManagerAccountId(String roomManagerAccountId) {
        this.roomManagerAccountId = roomManagerAccountId;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public LocalDate getRegDate() {
        return RegDate;
    }

    public void setRegDate(LocalDate regDate) {
        RegDate = regDate;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatRoomId='" + roomId + '\'' +
                ", chatRoomName='" + roomName + '\'' +
                ", roomManagerAccountId='" + roomManagerAccountId + '\'' +
                ", StateCode='" + StateCode + '\'' +
                ", RegDate=" + RegDate +
                '}';
    }
}
