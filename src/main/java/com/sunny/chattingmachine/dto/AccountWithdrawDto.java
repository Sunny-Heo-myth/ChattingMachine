package com.sunny.chattingmachine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class AccountWithdrawDto {

    @NotBlank
    String checkPassword;
}
