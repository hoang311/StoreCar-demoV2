package com.collectiongarage.service;

import com.collectiongarage.model.Token;

public interface TokenService {

	Token createToken(Token token);

	Token findByToken(String token);
}
