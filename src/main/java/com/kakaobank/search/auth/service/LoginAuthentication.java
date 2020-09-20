package com.kakaobank.search.auth.service;

import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.common.exception.CustomBusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public interface LoginAuthentication {

    UserDetails authenticate(LoginRequestVo loginRequestVo) throws CustomBusinessException;
}
