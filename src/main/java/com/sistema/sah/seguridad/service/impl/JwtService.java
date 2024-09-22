package com.sistema.sah.seguridad.service.impl;

import com.sistema.sah.seguridad.dto.AuthResponseDto;
import com.sistema.sah.seguridad.dto.UserSecurityDto;
import com.sistema.sah.seguridad.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    final String SECRET_KEY = "550d11d069140b3209b555262015721c511b6155e7e2070379faa1fc69e691ae88c66264b6cf9bf3c61487f5218bb2ce65d7eafe729af334763238196538393f88260e38670e382b9784c7b9ec72a57875c016b3f10e1250b952a09006f05c89056c35ec82a593ce794d94968e7186224b7acb140ae1937d1ce46ba7c682191f0bd32176c95793c0eb55b39ec894356af9512909695a0c2959738e9377914d1d085e9df9688cfa8edd9d1e0e3944a3208073c602e0309b813a2e2a994d396145fe16b8458ba7cae7fad72a75c7186da480bbdf2b290d1121638b4c489dd73f128bc9de18ee30f337955e68bf58767ec9982a5e8c841534bb4e0c1a6777f1fe0b";

    @Override
    public String getToken(UserDetails usuarioDto) {
        return getToken(new HashMap<>(), usuarioDto);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public AuthResponseDto generarToken(UserSecurityDto usuarioDto){
        return AuthResponseDto.builder().token(getToken(usuarioDto)).build();
    }

    public <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))).build().parseClaimsJws(token).getBody();
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)); // o HS384, HS512 dependiendo de lo que uses
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(key)
                .compact();
    }

}
