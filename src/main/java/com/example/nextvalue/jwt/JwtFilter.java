package com.example.nextvalue.jwt;

import com.example.nextvalue.memberdetail.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter { // jwt 검증 과정을 수행하는 Filter
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String authorization = request.getHeader("Authorization");
        log.info("JWT Token: {}", authorization);
        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replaceFirst("Bearer ", "");
            if(jwtTokenUtil.isTokenExpired(token)) {
                filterChain.doFilter(request, response);
            }

            MemberDetails member = new MemberDetails(jwtTokenUtil.getNickname(token), jwtTokenUtil.getEmail(token), jwtTokenUtil.getRole(token));
            Authentication auth = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
            return;
        }
    }
}
