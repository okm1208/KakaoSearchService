package com.kakaobank.search.auth.login;


import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.repository.AccountRepository;
import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.auth.service.LoginAuthentication;
import com.kakaobank.search.auth.service.impl.WebLoginAuthentication;
import com.kakaobank.search.auth.userdetails.AccountDetailsService;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 로그인 실패 예외 처리 테스트
 */
@SpringBootTest(classes = {
        WebLoginAuthentication.class,
        BCryptPasswordEncoder.class,
        AccountDetailsService.class
})
public class LoginAuthenticateTests {

    @Autowired
    private LoginAuthentication loginAuthentication;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AccountRepository accountRepository;

    private LoginRequestVo loginRequestVo;
    private String validEncodedPassword;
    @BeforeEach
    public void init(){
        loginRequestVo = new LoginRequestVo();
        loginRequestVo.setUserId("admin");
        loginRequestVo.setPassword("admin");

        validEncodedPassword = passwordEncoder.encode("admin");
    }
    @Test
    public void 존재하지_않는_사용자_테스트(){

        given(accountRepository.findByUserId(eq(loginRequestVo.getUserId()))).willReturn(null);

        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> loginAuthentication.authenticate(loginRequestVo)
        );

        assertThat(thrown.getMessage(), is(ErrorMessageProperties.ACCOUNT_NOT_FOUND));
    }

    @Test
    public void 패스워드_불일치_테스트(){
        Account mockAccount = new Account();

        //CASE : 패스워드 불일치
        mockAccount.setPassword("invalid_password");

        given(accountRepository.findByUserId(eq(loginRequestVo.getUserId()))).willReturn(mockAccount);

        BadCredentialsException thrown = assertThrows(
                BadCredentialsException.class,
                () -> loginAuthentication.authenticate(loginRequestVo)
        );

        assertThat(thrown.getMessage(), is(ErrorMessageProperties.MISMATCH_PASSWORD));

        //CASE : 계정 비활성화 상태
        mockAccount.setPassword(validEncodedPassword);
        mockAccount.setActive(false);

        thrown = assertThrows(
                BadCredentialsException.class,
                () -> loginAuthentication.authenticate(loginRequestVo)
        );

        assertThat(thrown.getMessage(), is(ErrorMessageProperties.INVALID_ACCOUNT));
    }




}
