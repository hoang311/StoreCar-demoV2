package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	Token findByToken(String token);
}
