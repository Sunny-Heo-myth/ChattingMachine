package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.config.SecurityUtil;
import com.sunny.chattingmachine.domain.Account;
import com.sunny.chattingmachine.dto.AccountInfoDto;
import com.sunny.chattingmachine.dto.AccountSignUpDto;
import com.sunny.chattingmachine.dto.AccountUpdateDto;
import com.sunny.chattingmachine.exception.AccountException;
import com.sunny.chattingmachine.exception.AccountExceptionType;
import com.sunny.chattingmachine.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signUp(AccountSignUpDto accountSignUpDto) {
        Account account = accountSignUpDto.toEntity();
        account.addUserAuthority();
        account.encodePassword(passwordEncoder);

        // accountId already exists.
        if (accountRepository.findByAccountId(accountSignUpDto.getAccountId()).isPresent()) {
            throw new AccountException(AccountExceptionType.ACCOUNTID_ALREADY_EXIST);
        }
        accountRepository.save(account);
    }

    @Override
    public void update(AccountUpdateDto accountUpdateDto) throws Exception {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND));

        accountUpdateDto.getAccountName().ifPresent(account::updateName);
        accountUpdateDto.getAccountNickName().ifPresent(account::updateNickName);
        accountUpdateDto.getAccountAge().ifPresent(account::updateAge);
    }

    @Override
    public void updatePassword(String asIsPassword, String toBoPassword) {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND));

        if (account.matchPassword(passwordEncoder, asIsPassword)) {
            throw new AccountException(AccountExceptionType.WRONG_PASSWORD);
        }
    }

    @Override
    public void withdraw(String checkPassword) {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND));

        if (account.matchPassword(passwordEncoder, checkPassword)) {
            throw new AccountException(AccountExceptionType.WRONG_PASSWORD);
        }

        accountRepository.delete(account);
    }

    @Override
    public AccountInfoDto getInfo(Long account_pk) {
        Account account = accountRepository.findById(account_pk)
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND));
        return new AccountInfoDto(account);
    }

    @Override
    public AccountInfoDto getMyInfo() {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND));
        return new AccountInfoDto(account);
    }

}
