package com.example.nextvalue.auth.jwt;



import com.example.nextvalue.auth.oauth.PrincipalDetailsService;
import com.example.nextvalue.config.TokenProperties;
import com.example.nextvalue.config.exception.CustomException;
import com.example.nextvalue.config.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final PrincipalDetailsService principalDetailsService;
    private final TokenProperties props;
    private final SecretKey signingKey;

    private static final String CLAIM_TYP = "typ"; // TokenType.name()
    private static final String CLAIM_DID = "did"; // deviceId
    private static final String CLAIM_JTI = "jti"; // refresh 회전용

    // Claim 추출
    private Claims parse(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }



    public Map<String,Object> issueTokens(long userId) {
        Instant now = Instant.now();
        // Access Token
        Instant atExp = now.plusSeconds(props.getAccessTtlSec());
        String jwt = Jwts.builder()
                .subject(Long.toString(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(atExp))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();

        return Map.of(
                "accessToken", jwt,
                "accessTokenExp", atExp
        );
    }


    public Long getMemberId(String token) {
        Claims c = parse(token);
        String sub = c.getSubject();
        try {
            return Long.parseLong(sub);
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Instant getExpiration(String jwt) {
        return parse(jwt).getExpiration().toInstant();
    }
    //토큰 검증
    public boolean isValidToken(String token) {
        try {
            parse(token); // 파싱 시도 (예외 발생 시 catch)
            return true;
        } catch (CustomException e) {
            return false;
        }
    }


    public Authentication getAuthentication(String token) {
        Long id = getMemberId(token);
        UserDetails userDetails = principalDetailsService.loadUserById(id);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}