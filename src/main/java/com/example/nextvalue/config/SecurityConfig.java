package com.example.nextvalue.config;

import com.example.nextvalue.auth.jwt.JwtProvider;
import com.example.nextvalue.auth.oauth.handler.JwtAccessDeniedHandler;
import com.example.nextvalue.auth.oauth.handler.JwtAuthenticationEntryPoint;
import com.example.nextvalue.auth.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.example.nextvalue.auth.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.example.nextvalue.auth.jwt.JwtFilter;
import com.example.nextvalue.auth.oauth.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Oauth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    private final OAuth2AuthenticationFailureHandler oauth2FailureHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

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
    SecurityFilterChain oauth2Chain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/oauth2/**",//클라이언트 초기 접속 api (GET /oauth2/authorization/{registrationId})
                        "/login/oauth2/**")//구글에서 우리서버 redirect url (GET /login/oauth2/code/{registrationId})
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // OAuth2는 state 보관 등으로 세션이 필요할 수 있으므로 IF_REQUIRED
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2    // 콜백 URL 변경
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oauth2SuccessHandler)
                        .failureHandler(oauth2FailureHandler)
                );
        return http.build();
    }
    @Bean
    @Order(1)
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
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtProvider jwtProvider) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtProvider);
        http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)//커스텀 필터 따로 사용(기본은 세션기반)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(httpBasic -> httpBasic.disable())//헤더에 비번 담아서 보내는 basic인증방식 jwt에서는 비활성화
                .sessionManagement(session -> session//세션 비활성화(무상태)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())//bean에서 corsConfigurationSource 찾아와서 알아서 등록
                .authorizeHttpRequests(a -> a
                        // 프리플라이트는 항상 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(allowedUrls).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .addFilterAfter(jwtFilter, org.springframework.web.filter.CorsFilter.class);
//                .csrf(csrf->csrf.disable())
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
//                .oauth2Login(oauth -> oauth
//                .loginPage("/login")
//                .userInfoEndpoint(userInfo -> userInfo
//                        .userService(customOAuth2UserService)
//                )
//                .defaultSuccessUrl("/",true)
//        );
        return http.build();
    }
}
