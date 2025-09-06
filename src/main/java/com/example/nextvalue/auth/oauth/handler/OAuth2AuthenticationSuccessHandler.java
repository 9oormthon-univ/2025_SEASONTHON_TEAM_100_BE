package com.example.nextvalue.auth.oauth.handler;


import com.example.nextvalue.auth.jwt.JwtProvider;
import com.example.nextvalue.auth.oauth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        PrincipalDetails user = (PrincipalDetails) authentication.getPrincipal();
        Long userId = user.getMember().getId();
        Map<String,Object> jwt = jwtProvider.issueTokens(userId);

        ResponseCookie jwtCookie = buildCookie(
                (String)jwt.get("accessToken"), (long)jwt.get("accessTokenExp")
        );
        response.addHeader("Set-Cookie", jwtCookie.toString());
        // 302는 캐시되면 안됨 → 캐시 방지
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");

        //response.sendRedirect("https://www.syncly-io.com/oauth2/success");
        //프론트 코드 배포 시 위 주소로 변경
        response.sendRedirect("http://localhost:5173/oauth2/success");
    }
    private ResponseCookie buildCookie(String jwt, long ttlSec) {
        return ResponseCookie.from("JWT", jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(Duration.ofSeconds(ttlSec))
                .build();
    }

}