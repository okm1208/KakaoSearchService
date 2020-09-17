package com.kakaobank.search.auth.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequestVo {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;

}
