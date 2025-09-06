package com.example.nextvalue.auth.oauth.handler;


import com.example.nextvalue.config.exception.CustomException;
import com.example.nextvalue.config.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


    @Component
    @RequiredArgsConstructor
    public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

        private final ObjectMapper objectMapper;

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            org.springframework.security.core.AuthenticationException exception)
                throws IOException, ServletException {

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(response.getWriter(), new CustomException(ErrorCode.OAUTH_LOGIN_FAIL));
        }
    }