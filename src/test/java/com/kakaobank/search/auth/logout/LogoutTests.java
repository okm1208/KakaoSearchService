package com.kakaobank.search.auth.logout;


import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.entity.RefreshToken;
import com.kakaobank.search.account.repository.AccountRepository;
import com.kakaobank.search.account.repository.RefreshTokenRepository;
import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.auth.service.LoginAuthentication;
import com.kakaobank.search.auth.service.LogoutService;
import com.kakaobank.search.auth.service.impl.TokenIssueService;
import com.kakaobank.search.auth.service.impl.WebLoginAuthentication;
import com.kakaobank.search.auth.service.impl.WebLogoutServiceImpl;
import com.kakaobank.search.auth.userdetails.AccountDetailsService;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.common.config.DataSourceConfig;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.AuthenticationException;
import com.kakaobank.search.common.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 로그아웃 테스트
 */
@SpringBootTest(classes = {
        TokenIssueService.class,
        WebLogoutServiceImpl.class
})
public class LogoutTests {

    @Autowired
    private LogoutService logoutService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    private String userId = "admin";

    private UserDetails userDetails;
    private Account mockAccount;
    private RefreshToken refreshToken;

    @BeforeEach
    public void init(){
        //mock 계정 초기화
        this.mockAccount = new Account();
        this.mockAccount.setUserId(userId);
        this.refreshToken = new RefreshToken();
        this.mockAccount.setRefreshTokens(Arrays.asList(refreshToken));
        this.mockAccount.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_ADMIN));
        this.userDetails =  new AccountUserDetails(mockAccount);
    }

    @Test
    public void 로그아웃_테스트(){

        // CASE : 만료된 액세스 토큰
        final String accessToken = createAccessToken(LocalDateTime.now().minusMinutes(10));
        AuthenticationException thrown = assertThrows(
                AuthenticationException.class,
                () ->  logoutService.logout(accessToken)
        );
        assertThat(thrown.getMessage(), is(ErrorMessageProperties.EXPIRED_ACCESS_TOKEN));

        // CASE : 정상
        final String validAccessToken = createAccessToken(LocalDateTime.now().plusHours(1));
        given(accountRepository.findByUserId(eq(userId))).willReturn(mockAccount);

        logoutService.logout(validAccessToken);
        verify(refreshTokenRepository, times(1)).updateExpiresDatetimeByUserId(any() , any());
    }

    private String createAccessToken(LocalDateTime expiresDt){
        return JwtUtils.createAccessToken(userDetails, expiresDt);
    }
}
