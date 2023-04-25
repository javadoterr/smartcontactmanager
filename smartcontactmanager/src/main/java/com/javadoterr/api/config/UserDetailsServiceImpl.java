package com.javadoterr.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.javadoterr.api.dao.UserRepositoy;
import com.javadoterr.api.entity.User;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepositoy userRepositoy;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fetching user from db
		User user = userRepositoy.getUserByUserName(username);
		if(null == user) {
			throw new UsernameNotFoundException("Could not found user !!");
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return customUserDetails;
	}

}
