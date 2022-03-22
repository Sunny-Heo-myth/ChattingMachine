package com.sunny.chattingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AccountUpdateDto {
    private final Optional<String> accountName;
    private final Optional<String> accountNickName;
    private final Optional<Integer> accountAge;

}
