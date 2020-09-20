package com.kakaobank.search.auth.service;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public interface LogoutService {
    void logout(String accessToken);
}
