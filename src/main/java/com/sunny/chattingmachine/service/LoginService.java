package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.domain.Account;
import com.sunny.chattingmachine.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Compare password of UserDetails and request authentication's credentials from JsonFilter
@Service
public class LoginService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public LoginService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("AccountId not found."));

        return User.builder().username(account.getAccountId())
                .password(account.getPassword())
                .roles(account.getAccountRole().name())
                .build();
    }
}
