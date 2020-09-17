package com.kakaobank.search.auth.userdetails;

import com.kakaobank.search.account.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AccountUserDetails implements UserDetails{

	private Account account;
    
    public AccountUserDetails(Account account){
    	this.account = account;
    }



	@Override
	public Collection<Account.AccountAuthority> getAuthorities() {
		return account.getRoles();
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		return account.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return account.isActive();
	}

	@Override
	public boolean isAccountNonLocked() {
		return account.getStatus() != Account.AccountStatus.LOCKED;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return account.isActive();
	}

	@Override
	public boolean isEnabled() {
		return account.isActive();
	}

	public Integer getUserNo() { return this.getUserNo();}

}
