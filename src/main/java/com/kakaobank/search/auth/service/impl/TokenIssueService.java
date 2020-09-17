package com.kakaobank.search.auth.service.impl;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.entity.RefreshToken;
import com.kakaobank.search.account.repository.AccountRepository;
import com.kakaobank.search.account.repository.RefreshTokenRepository;
import com.kakaobank.search.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.CollectionTable;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 설명 : RefreshToken 발급 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2020. 05. 15.
 */

@Service
@Slf4j
public class TokenIssueService {
//    @Autowired
//    private AuthCacheService authCacheService;
//
//    @Autowired
//    private RefreshTokenRepository refreshTokenRepository;
//
//    @Autowired
//    private DeviceInfoRepository deviceInfoRepository;
//
//    @Autowired
//    private AdminUserRepository adminUserRepository;

//    @Autowired
//    EntityManager entityManager;


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    public String issueAccessToken(UserDetails userDetails ){
        return JwtUtils.createAccessToken(userDetails, LocalDateTime.now().plusMinutes(5));
    }
    @Transactional
    public String issueRefreshToken(UserDetails userDetails ) {

        String userId  = userDetails.getUsername();
        Account account = accountRepository.findByUserId(userId);

        if(account == null ){
            throw new UsernameNotFoundException(userId);
        }

        List<RefreshToken> refreshTokenList = account.getRefreshTokens();

        if(!CollectionUtils.isEmpty(refreshTokenList)){
            //기존 재사용
            RefreshToken findValidRefreshToken =
                    refreshTokenList.stream().filter(refreshToken -> LocalDateTime.now().isBefore(refreshToken.getExpireDt())).findFirst().orElseGet(null);
            if(findValidRefreshToken != null){
                return findValidRefreshToken.getRefreshToken();
            }
        }


        //새로 발급

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setExpireDt(LocalDateTime.now().plusMinutes(10));
        refreshToken.setRefreshToken(JwtUtils.createRefreshToken(userDetails, LocalDateTime.now().plusMinutes(10)));

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getRefreshToken();
    }

//    @Transactional
//    public String issueAccessTokenFromRefreshToken(ReissueAccessTokenVo reissueAccessToken){
//
//        if(!StringUtils.isEmpty(reissueAccessToken.getRefreshToken())){
//            RawJwtToken rawRefreshToken = new RawJwtToken(reissueAccessToken.getRefreshToken());
//
//            UserVo refreshTokenUserVo = rawRefreshToken.parseUserVo();
//            Integer refreshTokenDeviceId = rawRefreshToken.parseAdminDeviceId();
//            DeviceInfo requestDeviceInfo =  deviceInfoRepository.findByAdminUserIdAndDeviceKey(refreshTokenUserVo.getUserId() , reissueAccessToken.getDeviceKey());
//
//            if(refreshTokenUserVo == null || StringUtils.isEmpty(refreshTokenUserVo.getUserId())  ){
//                throw new BusinessException(CommonErrorType.AUTHENTICATION_CREDENTIALS_NOT_FOUND );
//            }
//            if(requestDeviceInfo == null || !refreshTokenDeviceId.equals(requestDeviceInfo.getAdminUserDeviceId())){
//                log.error("requestDeviceInfo : {}",requestDeviceInfo);
//                log.error("refreshTokenDeviceId : {}",refreshTokenDeviceId);
//
//                throw new BusinessException(CommonErrorType.AUTHENTICATION_CREDENTIALS_NOT_FOUND );
//            }
//            if(!isActiveRefreshTokenWhereDeviceId(refreshTokenUserVo.getUserId(), refreshTokenDeviceId,  rawRefreshToken.getToken())){
//                log.error("isActiveRefreshTokenWhereDeviceId false : {},{}",refreshTokenUserVo.getUserId(),refreshTokenDeviceId);
//                throw new BusinessException(CommonErrorType.AUTHENTICATION_CREDENTIALS_NOT_FOUND);
//            }
//
//            AdminUser foundAdminUser = adminUserRepository.findById(refreshTokenUserVo.getUserId()).orElseGet(null);
//
//            if(foundAdminUser == null){
//                throw new BusinessException(CommonErrorType.AUTHENTICATION_CREDENTIALS_NOT_FOUND);
//            }
//
//            List<AdminUserRole> adminUserRoleList = adminUserRepository.finaAdminUserRoleList(foundAdminUser.getAdminUserId(), 1);
//            if(adminUserRoleList.isEmpty()){
//                throw new BusinessException(CommonErrorType.AUTHENTICATION_CREDENTIALS_NOT_FOUND);
//            }
//
//            Collection<? extends GrantedAuthority> authorities =
//                    adminUserRoleList.stream().map(adminUserRole -> new SimpleGrantedAuthority(adminUserRole.getAdminRole().getAdminRoleKey())).collect(Collectors.toList());
//            List<String> roleList  =adminUserRoleList.stream().map(adminUserRole -> adminUserRole.getAdminRole().getAdminRoleKey()).collect(Collectors.toList());
//
//            UserVo userVo= new UserVo(foundAdminUser);
////            userVo.setBlocked(cacheService.findCode("ACCESSIBLE_STATUS.PASSWD_FAIL_LOCKED").orElseGet(null));
//            userVo.setRoleList(roleList);
//            userVo.setLoginLang("KO");
//
//            return JwtUtils.createAccessToken(new CustomUserDetails(userVo, userVo.getUserId() , userVo.getPassword() , authorities),refreshTokenDeviceId,reissueAccessToken.getExpireDt());
//        }
//        return null;
//    }
//
//    //유저의 모든 refresh token을 폐기 ( 단말 여부 상관 X )
//    @Transactional
//    public void expireAllRefreshToken(String adminUserId){
//        List<RefreshToken> activeRefreshTokenList = authCacheService.findAllActiveRefreshTokenByUserId(adminUserId);
//        expireRefreshTokenList(activeRefreshTokenList);
//    }
//
//
//    //유저의 특정 단말의 refresh token을 모두 폐기
//    @Transactional
//    public void expireAllRefreshTokenWhereDeviceId(String adminUserId, Integer adminUserDeviceId){
//        List<RefreshToken> activeRefreshTokenList = authCacheService.findAllActiveRefreshTokenByUserIdAndAdminUserDeviceId(adminUserId , adminUserDeviceId);
//        expireRefreshTokenList(activeRefreshTokenList);
//    }
//
//    @Transactional
//    public void expireRefreshTokenList(List<RefreshToken> activeRefreshTokenList){
//        if(!CollectionUtils.isEmpty(activeRefreshTokenList)){
//            List<RefreshToken> filterRefreshToken = activeRefreshTokenList.stream()
//                    .filter(refreshToken -> RefreshTokenActiveStatusType.ACTIVE.getCode().equals(refreshToken.getTokenStatus()))
//                    .collect(Collectors.toList());
//            if(!CollectionUtils.isEmpty(filterRefreshToken)){
//                filterRefreshToken.forEach(refreshToken -> {
//                    refreshToken.setTokenStatus(RefreshTokenActiveStatusType.DEACTIVE.getCode());
//                    refreshToken.setExpireDt(LocalDateTime.now());
//                    authCacheService.removeRefreshToken(refreshToken.getAdminUserId(), refreshToken.getAdminUserDeviceId());
//
//                });
//                refreshTokenRepository.saveAll(filterRefreshToken);
//            }
//
//        }
//    }
//
//
//
//    @Transactional
//    public boolean isActiveRefreshTokenWhereDeviceId(String userId ,Integer adminUserDeviceId, String refreshToken){
//        List<RefreshToken> activeRefreshTokenList =  authCacheService.findAllActiveRefreshTokenByUserIdAndAdminUserDeviceId(userId,adminUserDeviceId);
//
//        if(!CollectionUtils.isEmpty(activeRefreshTokenList)){
//            return activeRefreshTokenList.stream()
//                    .filter(activeRefreshToken ->
//                            LocalDateTime.now().isBefore(activeRefreshToken.getExpireDt()) &&
//                                    RefreshTokenActiveStatusType.ACTIVE.getCode().equals(activeRefreshToken.getTokenStatus())
//                    ).anyMatch(activeRefreshToken -> {
//                        try {
//                            String token = new String(activeRefreshToken.getRefreshToken(), "UTF-8");
//                            return token.equals(refreshToken);
//                        }catch (Exception e){
//                            return false;
//                        }
//                    });
//        }
//        return false;
//    }

}
