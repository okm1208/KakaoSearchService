package com.kakaobank.search.account.repository;

import com.kakaobank.search.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer > {
    Account findByUserId(String userId);
}
