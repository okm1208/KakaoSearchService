package com.kakaobank.search.auth.filter;

import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.config.JwtProperties;
import com.kakaobank.search.common.exception.AuthorityException;
import com.kakaobank.search.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;
import com.kakaobank.search.auth.token.JwtAuthenticationToken;
import com.kakaobank.search.auth.token.RawJwtToken;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 인증 확인이 필요한 요청을 필터한다.
 */

@Slf4j
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationFailureHandler failureHandler;

    public JwtAuthenticationProcessingFilter(RequestMatcher matcher ){
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException  {
        String accessToken = request.getHeader(JwtProperties.ACCESS_TOKEN_HEADER_NAME);
        if(StringUtils.isEmpty(accessToken)){
            throw AuthorityException.of(ErrorMessageProperties.REQUIRED_TOKEN);
        }
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(new RawJwtToken(accessToken)));
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        chain.doFilter(request, response);
    }



    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException, AuthenticationException {
        SecurityContextHolder.clearContext();
        throw failed;
//        failureHandler.onAuthenticationFailure(request, response, failed);
    }


}
