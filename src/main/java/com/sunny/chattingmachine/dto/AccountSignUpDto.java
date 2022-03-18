package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.account.Account;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AccountSignUpDto {

    @NotBlank
    @Size(min = 7, max = 25)
    private String accountId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$")
    private String accountPassword;

    @NotBlank
    @Size(min = 2)
    @Pattern(regexp = "^[A-Za-z가-힣]+$")
    private String accountName;

    @NotBlank
    @Size(min = 2)
    private String accountNickName;

    @NotNull
    @Max(150)
    @Min(0)
    private Integer accountAge;

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