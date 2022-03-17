package com.sunny.chattingmachine.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

// todo lombok
@Getter
@Setter
@ToString
public class Room {
    private String roomId;
    private String roomName;
    // todo Enumerate
    private String StateCode;
    private LocalDateTime RegDate;

}
