package com.kakaobank.search.auth.provider;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.auth.token.JwtAuthenticationToken;
import com.kakaobank.search.auth.token.RawJwtToken;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.AuthorityException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 토큰의 유효성 검사한다.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        RawJwtToken rawAccessToken = (RawJwtToken) authentication.getCredentials();
        Account account = rawAccessToken.parseAccount();

        if(account == null){
            throw new BadCredentialsException(ErrorMessageProperties.INVALID_ACCOUNT);
        }
        if(CollectionUtils.isEmpty(account.getRoles())){
            throw AuthorityException.of(ErrorMessageProperties.EMPTY_AUTHORITIES);
        }
        AccountUserDetails customUserDetails = new AccountUserDetails(account);
        return new JwtAuthenticationToken(customUserDetails, customUserDetails.getAuthorities(), rawAccessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
