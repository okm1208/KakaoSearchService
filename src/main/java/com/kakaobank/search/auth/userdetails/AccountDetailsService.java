package com.kakaobank.search.auth.userdetails;

import com.kakaobank.search.account.entity.Account;
import com.kakaobank.search.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AccountDetailsService implements UserDetailsService{


	@Autowired
	private AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String userId)
			throws UsernameNotFoundException {

		Account account = accountRepository.findByUserId(userId);
		if(account == null){
			throw new UsernameNotFoundException(userId);
		}
		return new AccountUserDetails(account);
	}

}
