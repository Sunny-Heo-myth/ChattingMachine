package com.sunny.chattingmachine.controller;

import com.sunny.chattingmachine.dto.*;
import com.sunny.chattingmachine.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@Valid @RequestBody AccountSignUpDto accountSignUpDto) throws Exception {
        accountService.signUp(accountSignUpDto);
    }

    @PutMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public void updateBasicInfo(@Valid @RequestBody AccountUpdateDto accountUpdateDto) throws Exception {
        accountService.update(accountUpdateDto);
    }

    @PutMapping("/account/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody AccountUpdatePwDto accountUpdatePwDto) throws Exception {
        accountService.updatePassword(accountUpdatePwDto.getCheckPassword(), accountUpdatePwDto.getToBePassword());
    }

    @DeleteMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@Valid @RequestBody AccountWithdrawDto accountWithdrawDto) throws Exception {
        accountService.withdraw(accountWithdrawDto.getCheckPassword());
    }

    @GetMapping("/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getInfo(@Valid @PathVariable("accountId") Long id) throws Exception {
        AccountInfoDto infoDto = accountService.getInfo(id);
        return new ResponseEntity(infoDto, HttpStatus.OK);
    }

    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {
        AccountInfoDto infoDto = accountService.getMyInfo();
        return new ResponseEntity(infoDto, HttpStatus.OK);
    }
}
