package com.example.nextvalue.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
    private final SecretKey secretKey;

    public JwtTokenUtil(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String nickname, String email) {
        String jwt = Jwts.builder()
                .claim("nickname", nickname)
                .claim("email", email)
                .claim("role", "USER")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public Claims validateToken(String token) {
        log.info(token);
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getNickname(String token) {
        Claims claims = validateToken(token);
        return claims.get("nickname", String.class);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = validateToken(token);
        return claims.getExpiration().before(new Date());
    }

    public String getRole(String token) {
        Claims claims = validateToken(token);
        return claims.get("role", String.class);
    }

    public String getEmail(String token) {
        Claims claims = validateToken(token);
        return claims.get("email", String.class);
    }
}
