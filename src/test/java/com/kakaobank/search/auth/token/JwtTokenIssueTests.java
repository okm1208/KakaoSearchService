package com.kakaobank.search.auth.token;
import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.entity.RefreshToken;
import com.kakaobank.search.account.repository.AccountRepository;
import com.kakaobank.search.account.repository.RefreshTokenRepository;
import com.kakaobank.search.auth.model.TokenIssueVo;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.AuthenticationException;
import com.kakaobank.search.common.exception.AuthorityException;
import com.kakaobank.search.common.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.kakaobank.search.auth.service.impl.TokenIssueService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 토큰 발급 테스트
 */
@SpringBootTest(classes = {TokenIssueService.class})
public class JwtTokenIssueTests {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenIssueService tokenIssueService;

    private String userId = "admin";

    private UserDetails userDetails;
    private Account mockAccount;
    private RefreshToken refreshToken;

    @BeforeEach
    public void init(){
        //객체 초기화
        this.mockAccount = new Account();
        this.mockAccount.setUserId(userId);
        this.refreshToken = new RefreshToken();
        this.mockAccount.setRefreshTokens(Arrays.asList(refreshToken));
        this.mockAccount.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_ADMIN));
        this.userDetails =  new AccountUserDetails(mockAccount);
    }


    @Test
    public void 리프레시_토큰_재사용_테스트(){

        //CASE : 유효한 리프레시 토큰이 있다면 재사용

        refreshToken.setRefreshToken("refresh_token");
        refreshToken.setExpiresDt(LocalDateTime.now().plusMinutes(10));

        given(accountRepository.findByUserId(eq(userId))).willReturn(mockAccount);

        TokenIssueVo tokenIssueVo = tokenIssueService.issueRefreshToken(userDetails);
        assertNotNull(tokenIssueVo);
        assertThat(tokenIssueVo.getToken() ,is(refreshToken.getRefreshToken()));

        verify(refreshTokenRepository, never()).save(any());
    }


    @Test
    public void 리프레시_토큰_신규발급_테스트(){

        //CASE1 : 리프레시 토큰이 없는 경우 신규발급
        mockAccount.setRefreshTokens(null);

        given(accountRepository.findByUserId(eq(userId))).willReturn(mockAccount);

        TokenIssueVo tokenIssueVo = tokenIssueService.issueRefreshToken(userDetails);
        assertNotNull(tokenIssueVo);
        assertNotNull(tokenIssueVo.getToken());
        assertTrue(tokenIssueVo.getExpiresDt().isAfter(LocalDateTime.now()));

        verify(refreshTokenRepository, times(1)).save(any());

        //CASE2 : 리프레시 토큰이 있지만 유효 기간이 지난 경우 신규발급
        refreshToken = new RefreshToken();
        refreshToken.setExpiresDt(LocalDateTime.now().minusMinutes(10));
        refreshToken.setRefreshToken("refresh_token");
        mockAccount.setRefreshTokens(Arrays.asList(refreshToken));

        reset(refreshTokenRepository);

        tokenIssueVo = tokenIssueService.issueRefreshToken(userDetails);
        assertNotNull(tokenIssueVo);
        assertNotNull(tokenIssueVo.getToken());
        assertTrue(tokenIssueVo.getExpiresDt().isAfter(LocalDateTime.now()));

        verify(refreshTokenRepository, times(1)).save(any());
    }

    @Test
    public void 리프레시_토큰_재발급_테스트(){

        // CASE1 : 유효하지 않은 토큰
        AuthenticationException thrown = assertThrows(
                AuthenticationException.class,
                () -> tokenIssueService.issueAccessTokenFromRefreshToken("invalid_refresh_token")
        );
        assertThat(thrown.getMessage(), is(ErrorMessageProperties.INVALID_TOKEN));

        // CASE2 : 유효기간이 만료된 토큰
        String expiresRefreshToken = createRefreshToken(LocalDateTime.now().minusMinutes(10));

        thrown = assertThrows(
                AuthenticationException.class,
                () -> tokenIssueService.issueAccessTokenFromRefreshToken(expiresRefreshToken)
        );
        assertThat(thrown.getMessage(), is(ErrorMessageProperties.EXPIRED_ACCESS_TOKEN));

        // CASE3: 사용자의 권한이 없는 경우
        String validRefreshToken = createRefreshToken(LocalDateTime.now().plusHours(1));

        mockAccount.setRoles(null);
        given(accountRepository.findByUserId(eq(userId))).willReturn(mockAccount);

        AuthorityException authoriryThrown = assertThrows(
                AuthorityException.class,
                () -> tokenIssueService.issueAccessTokenFromRefreshToken(validRefreshToken)
        );
        assertThat(authoriryThrown.getMessage(), is(ErrorMessageProperties.EMPTY_AUTHORITIES));

    }

    private String createRefreshToken(LocalDateTime expiresDt){
        return JwtUtils.createRefreshToken(userDetails, expiresDt);
    }
}
