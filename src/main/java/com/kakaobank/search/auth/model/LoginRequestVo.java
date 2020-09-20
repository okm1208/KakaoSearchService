package com.kakaobank.search.auth.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */

@Data
public class LoginRequestVo {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;

}
