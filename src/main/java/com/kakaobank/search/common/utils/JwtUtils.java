package com.kakaobank.search.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.common.exception.AuthorityException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2020. 05. 12.
 */
public class JwtUtils {

    private static final String ISSUER = "kakaobank";
    private static final String TOKEN_SECRET = "token-secret";

    public static String createAccessToken(UserDetails userDetails ,LocalDateTime expireDt) {
        return createToken(userDetails, expireDt );
    }

    public static String createRefreshToken(UserDetails userDetails , LocalDateTime expireDt) {
        return createToken(userDetails , expireDt );
    }

    public static DecodedJWT tokenToJwt(String token) {
        try {
            return JWT.decode(token);
        }catch (JWTDecodeException decodeEx) {
            return null;
        }
    }
    public static void verify(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        verifier.verify(token);
    }

    private static String createToken(UserDetails userDetails, LocalDateTime expireDt) {
        try {

            AccountUserDetails customUserDetails = (AccountUserDetails)userDetails;
            String userId = customUserDetails.getUsername();
            String[] authoritiesArray = null;

            if(userDetails.getAuthorities()!=null){
                authoritiesArray = userDetails.getAuthorities().stream().map(auth->auth.getAuthority()).toArray(String[]::new);

                return JWT.create()
                        .withIssuer(ISSUER)
                        .withClaim("userId", userId )
                        .withArrayClaim("authorities", authoritiesArray)
                        .withExpiresAt(Date.from(expireDt.toInstant(ZoneOffset.ofHours(9))))
                        .sign(getAlgorithm());
            }else{
                throw new AuthorityException();
            }
        } catch (JWTCreationException createEx) {
            return null;
        }
    }

    private static Algorithm getAlgorithm() {
        try {
            return Algorithm.HMAC256(TOKEN_SECRET);
        } catch (IllegalArgumentException e) {
            return Algorithm.none();
        }
    }

}
