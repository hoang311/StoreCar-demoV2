package com.collectiongarage.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collectiongarage.model.User;
import com.collectiongarage.repository.UserRepository;
import com.collectiongarage.security.UserPrincipal;
import com.collectiongarage.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User createUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public UserPrincipal findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		UserPrincipal userPrincipal = null;
		if (null != user) {
			userPrincipal = new UserPrincipal();
			Set<String> authorities = new HashSet<>();
			if (null != user.getRoles())
				user.getRoles().forEach(r -> {
					authorities.add(r.getRoleKey());
					r.getPermissions().forEach(p -> authorities.add(p.getPermissionKey()));
				});

			userPrincipal.setUserId(user.getId());
			userPrincipal.setUsername(user.getUsername());
			userPrincipal.setPassword(user.getPassword());
			userPrincipal.setAuthorities(authorities);
		}
		return userPrincipal;
	}

}
