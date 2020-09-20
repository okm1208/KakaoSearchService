package com.kakaobank.search.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenReissueResponseVo {
    String accessToken;
    private Long expireSec;
}
