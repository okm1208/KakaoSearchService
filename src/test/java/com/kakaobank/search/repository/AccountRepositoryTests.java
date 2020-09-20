package com.kakaobank.search.repository;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.entity.AccountRole;
import com.kakaobank.search.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 계정 저장 및 조회 테스트
 */
@DataJpaTest
@Transactional
@Import(value = {BCryptPasswordEncoder.class})
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 계정_저장_및_조회_테스트(){
        Account account = new Account();
        account.setUserId("nick_test");
        account.setActive(true);
        account.setStatus(Account.AccountStatus.ACTIVE);

        account.setPassword(passwordEncoder.encode("nick_password"));
        accountRepository.save(account);

        Account retrivedAccount = accountRepository.findById(account.getUserNo()).orElseGet(null);
        assertNotNull(retrivedAccount);

        assertEquals(true, retrivedAccount.isActive());
        assertTrue(passwordEncoder.matches("nick_password",account.getPassword()));
        assertEquals("ACTIVE" , retrivedAccount.getStatus().name());
    }
}
