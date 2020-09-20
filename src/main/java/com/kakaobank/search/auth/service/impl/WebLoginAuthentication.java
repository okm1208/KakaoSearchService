package com.kakaobank.search.auth.service.impl;


import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.auth.service.LoginAuthentication;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 웹 기반 인증 요청을 처리 한다.
 */
@Service
public class WebLoginAuthentication implements LoginAuthentication {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public UserDetails authenticate(LoginRequestVo loginRequestVo) {

        String userId = loginRequestVo.getUserId();
        String password = loginRequestVo.getPassword();

        AccountUserDetails userDetails = (AccountUserDetails) userDetailsService.loadUserByUsername(userId);

        if(!matchPassword(password,userDetails.getPassword())){
            throw new BadCredentialsException(ErrorMessageProperties.MISMATCH_PASSWORD);
        }
        if(!userDetails.isEnabled()){
            throw new BadCredentialsException(ErrorMessageProperties.INVALID_ACCOUNT);
        }
        return userDetails;
    }

    private boolean matchPassword(String loginPwd, String password){
        return passwordEncoder.matches(loginPwd, password);
    }
}
