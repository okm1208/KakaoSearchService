package com.kakaobank.search.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseVo {
    private String accessToken;
    private String refreshToken;
}
