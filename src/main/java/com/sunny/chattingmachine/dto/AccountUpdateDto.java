package com.sunny.chattingmachine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@ToString
public class AccountUpdateDto {
    Optional<String> accountName;
    Optional<String> accountNickName;
    Optional<Integer> accountAge;

}
