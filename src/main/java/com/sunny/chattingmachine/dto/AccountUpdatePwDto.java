package com.sunny.chattingmachine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class AccountUpdatePwDto {

    @NotBlank
    private final String checkPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$")
    private final String toBePassword;

    public AccountUpdatePwDto(String checkPassword, String toBePassword) {
        this.checkPassword = checkPassword;
        this.toBePassword = toBePassword;
    }
}
