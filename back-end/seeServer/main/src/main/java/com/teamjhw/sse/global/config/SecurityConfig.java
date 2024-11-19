package com.teamjhw.sse.global.config;

import com.teamjhw.sse.global.security.filter.JWTCheckExceptionFilter;
import com.teamjhw.sse.global.security.filter.JWTCheckFilter;
import com.teamjhw.sse.global.security.handler.CustomAccessDeniedHandler;
import com.teamjhw.sse.global.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // csrf 사용 x
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 사용 x
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 폼 활용 로그인 사용 x
                .formLogin(AbstractHttpConfigurer::disable)
        ;

        // jwt 토큰 확인 필터
        http
                .addFilterBefore(new JWTCheckFilter(jwtUtil),
                                 UsernamePasswordAuthenticationFilter.class)
                // JWTCheckFilter를 실행하면서 발생하는 error를 handle함
                .addFilterBefore(new JWTCheckExceptionFilter(), JWTCheckFilter.class)
                .exceptionHandling(config -> {
                    config.accessDeniedHandler(new CustomAccessDeniedHandler());
                })
        ;

        return http.build();
    }
}
