package com.kakaobank.search.auth.controller;


import com.kakaobank.search.auth.model.LoginRequestVo;
import com.kakaobank.search.auth.model.LoginResponseVo;
import com.kakaobank.search.auth.model.TokenIssueVo;
import com.kakaobank.search.auth.model.TokenReissueResponseVo;
import com.kakaobank.search.auth.service.LoginAuthentication;
import com.kakaobank.search.auth.service.LogoutService;
import com.kakaobank.search.auth.service.impl.TokenIssueService;
import com.kakaobank.search.common.model.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 인증 컨트롤러
 */

@RestController
@RequestMapping("/auth")
@Api(tags = "인증 APIs")
public class AuthController {

    @Autowired
    private LoginAuthentication loginAuthentication;

    @Autowired
    private TokenIssueService tokenIssueService;

    @Autowired
    private LogoutService logoutService;

    @ApiOperation(value = "로그인 API")
    @PostMapping(value="/login")
    public CommonResponse<LoginResponseVo> login(@RequestBody @Valid LoginRequestVo loginRequestVo){

        UserDetails userDetails = loginAuthentication.authenticate(loginRequestVo);

        TokenIssueVo refreshToken = tokenIssueService.issueRefreshToken(userDetails);
        TokenIssueVo accessToken = tokenIssueService.issueAccessToken(userDetails);

        return CommonResponse.success(new LoginResponseVo(accessToken.getToken()
                                        , accessToken.getExpiresSec()
                                        , refreshToken.getToken()
                                        , refreshToken.getExpiresSec()));
    }

    @ApiOperation(value = "AccessToken 재발급 API" )
    @PostMapping(value="/token/reissue")
    public CommonResponse<TokenReissueResponseVo> reissueAccessToken(@RequestHeader(name = "Authorization") String refreshToken){

        TokenIssueVo token = tokenIssueService.issueAccessTokenFromRefreshToken(refreshToken);
        return CommonResponse.success( new TokenReissueResponseVo(token.getToken(),token.getExpiresSec()));
    }

    @ApiOperation(value = "로그아웃 API")
    @PostMapping(value="/logout")
    public CommonResponse logout(@RequestHeader(name = "Authorization") String accessToken){
        logoutService.logout(accessToken);
        return CommonResponse.success();
    }
}
