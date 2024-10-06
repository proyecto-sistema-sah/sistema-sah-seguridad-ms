package com.sistema.sah.seguridad.service;

public interface ITokenBlackListService {

    void blackListToken(String token);

    boolean isTokenBlackListed(String token);

}
