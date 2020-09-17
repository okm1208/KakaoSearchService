package com.kakaobank.search.auth.controller;


import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.auth.model.LoginResponseVo;
import com.kakaobank.search.auth.service.LoginAuthentication;
import com.kakaobank.search.auth.service.impl.TokenIssueService;
import com.kakaobank.search.common.model.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginAuthentication loginAuthentication;

    @Autowired
    private TokenIssueService tokenIssueService;

    @PostMapping(value="/login")
    public CommonResponse<LoginResponseVo> login(@RequestBody @Valid LoginRequestVo loginRequestVo){

        UserDetails userDetails = loginAuthentication.authenticate(loginRequestVo);

        String refreshToken = tokenIssueService.issueRefreshToken(userDetails);
        String accessToken = tokenIssueService.issueAccessToken(userDetails);

        return CommonResponse.success(new LoginResponseVo(accessToken,refreshToken));
    }


}
