package com.kakaobank.search.auth.service;

import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.common.exception.CustomBusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface LoginAuthentication {

    UserDetails authenticate(LoginRequestVo loginRequestVo) throws CustomBusinessException;
}
