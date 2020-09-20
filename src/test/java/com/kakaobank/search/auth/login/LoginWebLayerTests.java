package com.kakaobank.search.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.repository.AccountRepository;
import com.kakaobank.search.auth.model.LoginRequestVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 로그인 API 테스트
 */

@SpringBootTest
@AutoConfigureMockMvc
public class LoginWebLayerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminUserId = "admin";

    @Test
    public void 로그인_파라미터_유효성_테스트()throws Exception{

        LoginRequestVo request = new LoginRequestVo();
        this.mockMvc.perform(
                            post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(request)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());

        request.setUserId(adminUserId);

        this.mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 존재하지_않은_사용자_테스트()throws Exception{

        LoginRequestVo request = makeDefaultLoginRequestVo(adminUserId, "admin" );
        given(accountRepository.findByUserId(eq(adminUserId))).willReturn(null);

        this.mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void 패스워드_불일치및_유효하지않은_상태_테스트()throws Exception{
        LoginRequestVo request =makeDefaultLoginRequestVo(adminUserId,"invalid_password");
        Account passwordInvalidAccount =  makeMockAccount(adminUserId , "invalid_password" , true);

        given(accountRepository.findByUserId(eq(adminUserId))).willReturn(passwordInvalidAccount);

        this.mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());

        Account passiveAccount =  makeMockAccount(adminUserId , "admin" , false);
        given(accountRepository.findByUserId(eq(adminUserId))).willReturn(passiveAccount);

        this.mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void 로그인_성공()throws Exception{

        String validEncodedPassword = passwordEncoder.encode("admin");

        LoginRequestVo request =makeDefaultLoginRequestVo(adminUserId,"admin");
        Account validAccount =  makeMockAccount(adminUserId , validEncodedPassword , true);

        given(accountRepository.findByUserId(eq(adminUserId))).willReturn(validAccount);

        this.mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(not(empty()))))
                .andExpect(jsonPath("$.data.accessToken", is(not(empty()))))
                .andExpect(jsonPath("$.data.expireSec", is(not(empty()))))
                .andExpect(jsonPath("$.data.refreshToken", is(not(empty()))))
                .andExpect(jsonPath("$.data.refreshTokenExpireSec", is(not(empty()))));

    }

    private Account makeMockAccount(String userId,String password , boolean active){
        Account account = new Account();
        account.setUserId(userId);
        account.setPassword(password);
        account.setActive(active);
        account.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_ADMIN));
        return account;
    }
    private LoginRequestVo makeDefaultLoginRequestVo(String userId, String password){
        LoginRequestVo request = new LoginRequestVo();
        request.setUserId(userId);
        request.setPassword(password);

        return request;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
