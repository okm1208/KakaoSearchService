package com.kakaobank.search.account.repository;


import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer > {
}
