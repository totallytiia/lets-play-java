package com.example.letsplay.services.impl;


import com.example.letsplay.exceptions.InvalidUserException;
import com.example.letsplay.model.User;
import com.example.letsplay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public User loadUserByUsername(String name) {
		User returnValue = null;
		Optional<User> userOpt = Optional.ofNullable(userService.getUserByEmail(name));
		if (userOpt.isEmpty()) {
			throw new InvalidUserException(new Error("Invalid user"));
		} else {
			returnValue = userOpt.get();
		}
		return returnValue;

	}
}
