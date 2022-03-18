package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.config.SecurityUtil;
import com.sunny.chattingmachine.domain.account.Account;
import com.sunny.chattingmachine.dto.AccountInfoDto;
import com.sunny.chattingmachine.dto.AccountSignUpDto;
import com.sunny.chattingmachine.dto.AccountUpdateDto;
import com.sunny.chattingmachine.exception.OverlappedIdException;
import com.sunny.chattingmachine.repository.AccountRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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

        if (accountRepository.findByAccountId(accountSignUpDto.getAccountId()).isPresent()) {
            throw new OverlappedIdException(accountSignUpDto.getAccountId());
        }
        accountRepository.save(account);
    }

    @Override
    public void update(AccountUpdateDto accountUpdateDto) throws Exception {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new UsernameNotFoundException("could not update - AccountId not found."));

        accountUpdateDto.getAccountName().ifPresent(account::updateName);
        accountUpdateDto.getAccountNickName().ifPresent(account::updateNickName);
        accountUpdateDto.getAccountAge().ifPresent(account::updateAge);
    }

    @Override
    public void updatePassword(String checkPassword, String toBoPassword) throws Exception {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new Exception("could not updatePassword : Account not exist."));
    }

    @Override
    public void withdraw(String checkPassword) throws Exception {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new UsernameNotFoundException("could not withdraw : AccountId not found."));

        if (!account.matchPassword(passwordEncoder, checkPassword)) {
            throw new Exception("could not withdraw : wrong password.");
        }

        accountRepository.delete(account);
    }

    @Override
    public AccountInfoDto getInfo(Long account_pk) throws Exception {
        Account account = accountRepository.findById(account_pk)
                .orElseThrow(() -> new UsernameNotFoundException("could not getInfo : AccountId not found."));
        return new AccountInfoDto(account);
    }

    @Override
    public AccountInfoDto getMyInfo() throws Exception {
        Account account = accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new UsernameNotFoundException("could not getMyInfo : AccountId not found."));
        return new AccountInfoDto(account);
    }

}
