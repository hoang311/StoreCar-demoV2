package com.collectiongarage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.collectiongarage.model.User;
import com.collectiongarage.repository.UserRepository;
import com.collectiongarage.service.UserService;

/**
 * @author hieuha
 *
 */
@RestController
@CrossOrigin
public class WithoutAuthorizeController {
	@Autowired
	private UserRepository userRepository;

	/**
	 * Test trường hợp khôngcheck quyền Authorize Tạo mới user
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/users/createAdmin")
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public ResponseEntity<Object> create(@RequestBody User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		return new ResponseEntity(userRepository.save(user), HttpStatus.CREATED);
	}

	@Autowired
	private UserService userService;

	/**
	 * Test có kiểm tra quyền.
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/users/create")
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public User register(@RequestBody User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

		return userService.createUser(user);
	}
}
