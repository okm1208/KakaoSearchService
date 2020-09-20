package com.kakaobank.search.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :  Refresh Token Entity
 */
@Table(name = "refresh_token")
@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refreshTokenId;

    @Column(length = 30, nullable = false)
    private String userId;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String refreshToken;
    @Column(nullable = false)
    private LocalDateTime expiresDt;
}
