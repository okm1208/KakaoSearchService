package com.kakaobank.search.account.repository;

import com.kakaobank.search.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description :
 */
public interface AccountRepository extends JpaRepository<Account, Integer > {
    Account findByUserId(String userId);
}
