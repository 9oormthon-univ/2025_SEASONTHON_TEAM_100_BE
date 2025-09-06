package com.example.nextvalue.auth.oauth.handler;


import com.example.nextvalue.apiPayload.ApiResponse;
import com.example.nextvalue.config.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 인증 관련 예외처리

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // JwtFilter에서 넣어준 예외코드
        Object exception = request.getAttribute("exception");


        ErrorCode errorCode = ErrorCode.EMPTY_TOKEN;
        if (exception instanceof ErrorCode) {//JwtErrorCode 객체 아니면 setAttribute 안한 경우니까 emptyToken
            errorCode = (ErrorCode) exception;
        }

        ApiResponse<Object> errorResponse = ApiResponse.error(errorCode);
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }

}