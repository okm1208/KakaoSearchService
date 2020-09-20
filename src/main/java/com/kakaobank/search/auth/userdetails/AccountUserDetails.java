package com.kakaobank.search.auth.userdetails;

import com.kakaobank.search.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : Custom UserDetails
 */
public class AccountUserDetails implements UserDetails{

	private Account account;
    
    public AccountUserDetails(Account account){
    	if(account == null){
    		throw new RuntimeException();
		}
    	this.account = account;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
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
