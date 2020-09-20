package com.kakaobank.search.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenIssueVo {
    private String token;
    private LocalDateTime expiresDt;

    public Long getExpiresSec(){
        if(expiresDt != null && expiresDt.isAfter(LocalDateTime.now())){
            return Duration.between(LocalDateTime.now(), expiresDt).toMillis() / 1000L ;
        }
        return 0L;
    }
}
