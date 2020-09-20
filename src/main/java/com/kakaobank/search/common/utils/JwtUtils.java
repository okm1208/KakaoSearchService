package com.kakaobank.search.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.config.JwtProperties;
import com.kakaobank.search.common.exception.AuthorityException;
import com.kakaobank.search.common.exception.InternalServerException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : JwtUtils
 */
public class JwtUtils {

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
                        .withIssuer(JwtProperties.ISSUER)
                        .withClaim("userId", userId )
                        .withClaim("active" , true)
                        .withArrayClaim("authorities", authoritiesArray)
                        .withExpiresAt(Date.from(expireDt.toInstant(ZoneOffset.ofHours(9))))
                        .sign(getAlgorithm());
            }else{
                throw new AuthorityException();
            }
        } catch (JWTCreationException createEx) {
            throw InternalServerException.of(ErrorMessageProperties.CREATE_TOKEN_FAILED + " : " + createEx.getMessage());
        }
    }

    private static Algorithm getAlgorithm() {
        try {
            return Algorithm.HMAC256(JwtProperties.TOKEN_SECRET);
        } catch (IllegalArgumentException e) {
            return Algorithm.none();
        }
    }

}
