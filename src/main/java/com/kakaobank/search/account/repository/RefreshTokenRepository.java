package com.kakaobank.search.account.repository;


import com.kakaobank.search.account.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer > {
    @Modifying
    @Query("update RefreshToken rt set rt.expiresDt = :expiresDt WHERE rt.userId = :userId")
    void updateExpiresDatetimeByUserId(@Param("userId") String userId,@Param("expiresDt") LocalDateTime expiresDt);
}
