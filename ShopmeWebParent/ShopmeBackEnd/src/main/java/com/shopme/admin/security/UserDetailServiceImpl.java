package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.UserRepository;
import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entity.User;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
		if (!userRepository.existsByEmail(email)) throw new UserNotFoundException("Email not found: " + email);
		
		User user = userRepository.findUserByEmail(email);
		
		return UserDetailsImpl.build(user);
	}
}
