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
        assertEquals("ACTIVE" , retrivedAccount.getStatus());
    }

    @Test
    public void bcrypt(){
        System.out.println(passwordEncoder.encode("admin"));
    }

    @Test
    public void 계정_권한_저장_테스트(){
        Account account = accountRepository.findByUserId("admin");

        System.out.println(account);
//        Account account = new Account();
//        account.setUserId("nick_test");
//        account.setActive(true);
//        account.setStatus(Account.AccountStatus.ACTIVE);
//
//        account.setPassword(passwordEncoder.encode("nick_password"));
//        accountRepository.save(account);
//
//
//        account.setRoles(Arrays.asList(Account.AccountAuthority.ADMIN));


//        AccountRole addRole = new AccountRole();
//        addRole.setUserNo(account.getUserNo());
//        addRole.setAuthority("ADMIN");
//        account.setRoles(Arrays.asList(addRole));


//        Account retrivedAccount = accountRepository.findById(account.getUserNo()).orElseGet(null);
//        assertNotNull(retrivedAccount);
//
//        assertNotNull(retrivedAccount.getRoles());
//        assertEquals(1,retrivedAccount.getRoles().size() );
//
//        retrivedAccount.setRoles(null);
//
//        Account emptyRoleAccount = accountRepository.findById(account.getUserNo()).orElseGet(null);
//        assertNull(emptyRoleAccount.getRoles());
    }
}
