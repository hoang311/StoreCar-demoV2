package com.collectiongarage.service;

import com.collectiongarage.model.User;
import com.collectiongarage.security.UserPrincipal;

public interface UserService {
	User createUser(User user);

	UserPrincipal findByUsername(String username);
}
