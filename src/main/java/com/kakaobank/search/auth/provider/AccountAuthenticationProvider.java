package com.kakaobank.search.auth.provider;

import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


public class AccountAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        AccountUserDetails user = (AccountUserDetails)userDetailsService.loadUserByUsername(userId);

        if(!matchPassword(password,user.getPassword())){
            throw new BadCredentialsException(userId);
        }
        if(!user.isEnabled()){
            throw new BadCredentialsException(userId);
        }
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(),user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private boolean matchPassword(String loginPwd, String password){
        return passwordEncoder.matches(loginPwd, password);
    }
}
