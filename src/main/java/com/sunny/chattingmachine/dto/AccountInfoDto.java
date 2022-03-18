package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.account.Account;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AccountInfoDto {
    private final String accountId;
    private final String accountName;
    private final String accountNickName;
    private final Integer accountAge;

    @Builder
    public AccountInfoDto(Account account) {
        this.accountId = account.getAccountId();
        this.accountName = account.getNickName();
        this.accountNickName = account.getNickName();
        this.accountAge = account.getAge();
    }
}
