package com.kakaobank.search.auth.token;

import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken{

    private RawJwtToken rawAccessToken;
    private AccountUserDetails userDetails;

    public JwtAuthenticationToken(RawJwtToken unsafeToken) {
        super(null);
        this.rawAccessToken = unsafeToken;
        this.setAuthenticated(false);
    }
    public JwtAuthenticationToken(AccountUserDetails userDetails, Collection<? extends GrantedAuthority> authorities , RawJwtToken rawAccessJwtToken) {
        super(authorities);
        this.eraseCredentials();
        this.userDetails = userDetails;
        this.rawAccessToken = rawAccessJwtToken;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return rawAccessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public Object getDetails() {
        return this.userDetails;
    }
}
