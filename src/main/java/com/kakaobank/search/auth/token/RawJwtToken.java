package com.kakaobank.search.auth.token;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.AuthenticationException;
import com.kakaobank.search.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2020. 05. 13.
 */
@Slf4j
public class RawJwtToken implements Serializable {
    private String token;

    public RawJwtToken(String token ) {
        this.token = token;
    }

    private void verifyJwt(String token){
        try{
            JwtUtils.verify(token);
        }catch(TokenExpiredException e){
            throw new AuthenticationException(ErrorMessageProperties.EXPIRED_ACCESS_TOKEN);
        }catch(JWTVerificationException e){
            throw new AuthenticationException( ErrorMessageProperties.INVALID_TOKEN);
        }
    }
    private DecodedJWT parseJwt() {

        verifyJwt(token);
        DecodedJWT decodedJWT = JwtUtils.tokenToJwt(token);
        if(decodedJWT == null){
           throw new AuthenticationException( ErrorMessageProperties.INVALID_TOKEN);
        }
        return decodedJWT;
    }

    public Account parseAccount(){
        Account account = new Account();
        DecodedJWT decodedJWT = this.parseJwt();
        account.setUserId(decodedJWT.getClaim("userId").asString());
        account.setActive(decodedJWT.getClaim("active").asBoolean());
        List<String> roleList =  decodedJWT.getClaim("authorities").asList(String.class);
        if(!CollectionUtils.isEmpty(roleList)){
            account.setRoles(roleList.stream().map(role-> Account.AccountAuthority.findByName(role)).collect(Collectors.toList()));
        }
        return account;
    }
}
