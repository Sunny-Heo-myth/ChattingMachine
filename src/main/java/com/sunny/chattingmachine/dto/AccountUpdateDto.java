package com.sunny.chattingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@ToString
public class AccountUpdateDto {
    private final Optional<String> accountName;
    private final Optional<String> accountNickName;
    private final Optional<Integer> accountAge;

    public AccountUpdateDto(Optional<String> accountName, Optional<String> accountNickName, Optional<Integer> accountAge) {
        this.accountName = accountName;
        this.accountNickName = accountNickName;
        this.accountAge = accountAge;
    }
}
