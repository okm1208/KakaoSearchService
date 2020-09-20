package com.kakaobank.search.auth.service.impl;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.entity.RefreshToken;
import com.kakaobank.search.account.repository.AccountRepository;
import com.kakaobank.search.account.repository.RefreshTokenRepository;
import com.kakaobank.search.auth.model.TokenIssueVo;
import com.kakaobank.search.auth.token.RawJwtToken;
import com.kakaobank.search.auth.userdetails.AccountUserDetails;
import com.kakaobank.search.common.config.ErrorMessageProperties;
import com.kakaobank.search.common.exception.AuthorityException;
import com.kakaobank.search.common.exception.BadRequestException;
import com.kakaobank.search.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : AccessToken, RefreshToken을 발급 한다.
 */

@Service
@Slf4j
public class TokenIssueService {

    @Value("${auth.token.expired-minute.access-token:30}")
    private Integer accessTokenExpiredMin;
    @Value("${auth.token.expired-minute.refresh-token:60}")
    private Integer refreshTokenExpiredMin;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountRepository accountRepository;


    public TokenIssueVo issueAccessToken(UserDetails userDetails ){

        LocalDateTime expiresDt = LocalDateTime.now().plusMinutes(accessTokenExpiredMin);

         return new TokenIssueVo(JwtUtils.createAccessToken(userDetails, expiresDt), expiresDt) ;
    }
    @Transactional
    public TokenIssueVo issueRefreshToken(UserDetails userDetails ) {

        String userId  = userDetails.getUsername();
        Account account = accountRepository.findByUserId(userId);

        if(account == null ){
            log.warn("not found account :{}", userId);
            throw new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
        }

        List<RefreshToken> refreshTokenList = account.getRefreshTokens();

        if(!CollectionUtils.isEmpty(refreshTokenList)){
            //기존 Refresh Token이 있다면 재사용
            RefreshToken findValidRefreshToken =
                    refreshTokenList.stream().filter(refreshToken ->
                            LocalDateTime.now().isBefore(refreshToken.getExpiresDt())).findFirst().orElse(null);
            if(findValidRefreshToken != null){
                log.debug("refresh token recycle : {},{}",userId,findValidRefreshToken.getRefreshToken());
                return new TokenIssueVo(findValidRefreshToken.getRefreshToken(), findValidRefreshToken.getExpiresDt()) ;
            }
        }

        //Refresh Token 새로 발급
        LocalDateTime expiresDt = LocalDateTime.now().plusMinutes(refreshTokenExpiredMin);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setExpiresDt(expiresDt);
        refreshToken.setRefreshToken(JwtUtils.createRefreshToken(userDetails, expiresDt));

        refreshTokenRepository.save(refreshToken);

        log.debug("refresh token new issue : {},{}",userId,refreshToken.getRefreshToken());

        return new TokenIssueVo(refreshToken.getRefreshToken(), expiresDt);
    }

    @Transactional
    public String issueAccessTokenFromRefreshToken(String refreshToken){

        if(!StringUtils.isEmpty(refreshToken)){
            RawJwtToken rawRefreshToken = new RawJwtToken(refreshToken);

            Account tokenAccount = rawRefreshToken.parseAccount();
            if(tokenAccount == null || StringUtils.isEmpty(tokenAccount.getUserId())  ){
                throw new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
            }
            Account foundAccount = accountRepository.findByUserId(tokenAccount.getUserId());
            if(foundAccount == null){
                throw new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
            }

            List<Account.AccountAuthority> accountRoleList =  foundAccount.getRoles();
            if(CollectionUtils.isEmpty(accountRoleList)){
                log.warn("account role is empty : {}" , foundAccount.getUserId());
                throw AuthorityException.of(ErrorMessageProperties.EMPTY_AUTHORITIES);
            }

            AccountUserDetails accountUserDetails = new AccountUserDetails(foundAccount);
            return JwtUtils.createAccessToken(accountUserDetails , LocalDateTime.now().plusMinutes(accessTokenExpiredMin));
        }else{
            throw new BadRequestException();
        }
    }

    @Transactional
    public void expireAllRefreshTokenWhereUserId(String userId){
        log.debug("expire all refresh token : {}",userId);
        refreshTokenRepository.updateExpiresDatetimeByUserId(userId,LocalDateTime.now());

    }
}
