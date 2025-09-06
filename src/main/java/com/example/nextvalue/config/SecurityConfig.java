package com.example.nextvalue.config;

import com.example.nextvalue.jwt.JwtFilter;
import com.example.nextvalue.jwt.JwtTokenUtil;
import com.example.nextvalue.service.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Oauth2UserService customOAuth2UserService;

    // 허용할 URL을 배열의 형태로 관리
    private final String[] allowedUrls = {
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/memento/**",
            "*"
    };
    @Bean
    @Order(0)
    SecurityFilterChain docsChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a.anyRequest().permitAll());
        return http.build();
    }
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenUtil jwtTokenUtil) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtTokenUtil);
        http
                .csrf(csrf->csrf.disable())
//                .securityMatcher("/**")
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(Customizer.withDefaults())//bean에서 corsConfigurationSource 찾아와서 알아서 등록
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(allowedUrls).permitAll()
//                        // 이외의 요청에 대해서는 인증이 필요하도록 설정
//                        .anyRequest().authenticated())
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/",true)
        );
        return http.build();
    }
}
