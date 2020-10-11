package com.kakaobank.search.auth.permission;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.entity.RefreshToken;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.model.CommonResponse;
import com.kakaobank.search.common.model.ErrorResponseEntity;
import com.kakaobank.search.common.utils.JwtUtils;
import com.kakaobank.search.external.service.impl.KakaoPlaceKeywordSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-20
 *  description : 인가 처리 예외 통합 테스트
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationExceptionApiTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private KakaoPlaceKeywordSearchService kakaoPlaceKeywordSearchService;
    private String getSearchPlaceApiUrl;

    private String userId = "admin";

    private UserDetails userDetails;
    private Account mockAccount;
    private RefreshToken refreshToken;

    @BeforeEach
    public void init(){
        this.getSearchPlaceApiUrl =  "http://localhost:" + port + "/place/kakao/search";
        //mock 계정 초기화
        this.mockAccount = new Account();
        this.mockAccount.setUserId(userId);
        this.refreshToken = new RefreshToken();
        this.mockAccount.setRefreshTokens(Arrays.asList(refreshToken));
        this.mockAccount.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_ADMIN));
        this.userDetails =  new AccountUserDetails(mockAccount);
    }
    @Test
    public void 토큰_ISNULL_필터_테스트()  {
        ResponseEntity<ErrorResponseEntity> response = testRestTemplate.
                getForEntity(UriComponentsBuilder.fromHttpUrl(getSearchPlaceApiUrl).toUriString(),
                        ErrorResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
        assertThat(response.getBody().getType(),is("NotAuthorized"));
        assertThat(response.getBody().getMessage(),is(ErrorMessageProperties.REQUIRED_TOKEN));
    }

    @Test
    public void 유효하지_않은_토큰_테스트 (){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "invalid_token");

        ResponseEntity<ErrorResponseEntity> response = testRestTemplate.
                exchange(UriComponentsBuilder.fromHttpUrl(getSearchPlaceApiUrl).toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        ErrorResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody().getType(),is("NotAuthenticated"));
        assertThat(response.getBody().getMessage(),is(ErrorMessageProperties.INVALID_TOKEN));
    }

    @Test
    public void 만료된_토큰_테스트(){
        String accessToken = JwtUtils.createAccessToken(userDetails, LocalDateTime.now().minusHours(1));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);

        ResponseEntity<ErrorResponseEntity> response = testRestTemplate.
                exchange(UriComponentsBuilder.fromHttpUrl(getSearchPlaceApiUrl).toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        ErrorResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody().getType(),is("NotAuthenticated"));
        assertThat(response.getBody().getMessage(),is(ErrorMessageProperties.EXPIRED_ACCESS_TOKEN));
    }

    @Test
    public void 성공(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getSearchPlaceApiUrl)
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("keyword", "id");

        //USER 권한 부여
        this.mockAccount.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_USER));

        String accessToken = JwtUtils.createAccessToken(userDetails, LocalDateTime.now().plusHours(1));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);

        ResponseEntity<CommonResponse> response = testRestTemplate.
                exchange(builder.toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        CommonResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }
}
