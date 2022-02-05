package com.collectiongarage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collectiongarage.model.Token;
import com.collectiongarage.repository.TokenRepository;
import com.collectiongarage.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	public Token createToken(Token token) {
		return tokenRepository.saveAndFlush(token);
	}

	@Override
	public Token findByToken(String token) {
		return tokenRepository.findByToken(token);
	}
}
