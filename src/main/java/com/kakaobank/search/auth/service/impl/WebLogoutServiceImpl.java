package com.kakaobank.search.auth.service.impl;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.auth.service.LogoutService;
import com.kakaobank.search.auth.token.RawJwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 로그 아웃 기능
 */
@Service
public class WebLogoutServiceImpl implements LogoutService {

    @Autowired
    private TokenIssueService tokenIssueService;

    @Override
    @Transactional
    public void logout(String accessToken) {

        RawJwtToken rawAccessToken = new RawJwtToken(accessToken);
        Account account = rawAccessToken.parseAccount();

        tokenIssueService.expireAllRefreshTokenWhereUserId(account.getUserId());
    }

}
