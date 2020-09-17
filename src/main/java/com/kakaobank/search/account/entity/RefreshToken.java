package com.kakaobank.search.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "refresh_token")
@Entity
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refreshTokenId;

    @Column
    private String userId;
    @Column
    private String refreshToken;
    @Column
    private LocalDateTime expireDt;
}
