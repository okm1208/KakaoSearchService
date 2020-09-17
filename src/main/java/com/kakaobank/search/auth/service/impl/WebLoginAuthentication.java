package com.kakaobank.search.auth.service.impl;


import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.auth.service.LoginAuthentication;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class WebLoginAuthentication implements LoginAuthentication {
    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public UserDetails authenticate(LoginRequestVo loginRequestVo) {
        return (AccountUserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestVo.getUserId(), loginRequestVo.getPassword())
        ).getPrincipal();
    }
}
