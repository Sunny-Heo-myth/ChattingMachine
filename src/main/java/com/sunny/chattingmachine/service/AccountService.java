package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.dto.AccountInfoDto;
import com.sunny.chattingmachine.dto.AccountSignUpDto;
import com.sunny.chattingmachine.dto.AccountUpdateDto;

public interface AccountService {

    /*
    sign up
    update info
    withdrawal
    show info
     */
    void signUp(AccountSignUpDto accountSignUpDto) throws Exception;

    void update(AccountUpdateDto accountUpdateDto) throws Exception;

    void updatePassword(String checkPassword, String toBoPassword) throws Exception;

    void withdraw(String checkPassword) throws Exception;

    AccountInfoDto getInfo(Long accountId) throws Exception;

    AccountInfoDto getMyInfo() throws Exception;
}
