package com.sunny.chattingmachine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ChatTextMessage {
    private String type;
    private String roomId;
    private String sessionId;
    private String userName;
    private String message;
    private LocalDateTime regDatetime;
}
