package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
