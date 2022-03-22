package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.Account;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class AccountSignUpDto {

    @NotBlank
    @Size(min = 7, max = 25)
    private final String accountId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$")
    private final String accountPassword;

    @NotBlank
    @Size(min = 2)
    @Pattern(regexp = "^[A-Za-z가-힣]+$")
    private final String accountName;

    @NotBlank
    @Size(min = 2)
    private final String accountNickName;

    @NotNull
    @Max(150)
    @Min(0)
    private final Integer accountAge;

    public AccountSignUpDto(String accountId, String accountPassword, String accountName, String accountNickName, Integer accountAge) {
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.accountName = accountName;
        this.accountNickName = accountNickName;
        this.accountAge = accountAge;
    }

    public Account toEntity() {
        return Account.builder()
                .accountId(accountId)
                .password(accountPassword)
                .name(accountName)
                .nickName(accountNickName)
                .age(accountAge)
                .build();
    }
}