package com.kakaobank.search.auth.userdetails;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 사용자 검색 기능
 */
@Service
public class AccountDetailsService implements UserDetailsService{


	@Autowired
	private AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String userId)
			throws UsernameNotFoundException {

		Account account = accountRepository.findByUserId(userId);
		if(account == null){
			throw new UsernameNotFoundException("존재하지 않은 사용자 입니다.");
		}
		return new AccountUserDetails(account);
	}

}
