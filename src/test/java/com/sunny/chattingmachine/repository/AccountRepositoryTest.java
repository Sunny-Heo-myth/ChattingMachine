package com.sunny.chattingmachine.repository;

import com.sunny.chattingmachine.domain.account.Account;
import com.sunny.chattingmachine.domain.account.AccountRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@WebAppConfiguration
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EntityManager entityManager;

    @AfterEach
    private void afterEach(){
        entityManager.clear();
    }

    @Test
    void accountSaveTest() {
        Account testAccount = Account.builder()
                .accountId("test_id")
                .password("12345")
                .name("test_name")
                .nickName("test_nickName")
                .age(30)
                .accountRole(AccountRole.USER)
                .build();

        Account saveAccount = accountRepository.save(testAccount);

        Account findAccount = accountRepository.findByAccountId(saveAccount.getAccountId())
                .orElseThrow(() -> new RuntimeException("no saved account"));
    }

}