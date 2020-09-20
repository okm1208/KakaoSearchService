package com.kakaobank.search.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 인증 확인이 필요한 요청을 필터한다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseVo {
    private String accessToken;
    private Long expireSec;
    private String refreshToken;
    private Long refreshTokenExpireSec;
}
