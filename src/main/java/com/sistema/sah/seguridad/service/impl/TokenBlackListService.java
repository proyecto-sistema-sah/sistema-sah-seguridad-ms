package com.sistema.sah.seguridad.service.impl;

import com.sistema.sah.seguridad.service.ITokenBlackListService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlackListService implements ITokenBlackListService {

    // Set que actuará como lista negra de los tokens
    private Set<String> blacklistedTokens = new HashSet<>();

    @Override
    public void blackListToken(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public boolean isTokenBlackListed(String token) {
        return blacklistedTokens.contains(token);
    }
}
