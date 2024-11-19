package com.teamjhw.bestfriend.global.config;

import com.teamjhw.bestfriend.global.security.filter.JWTCheckExceptionFilter;
import com.teamjhw.bestfriend.global.security.filter.JWTCheckFilter;
import com.teamjhw.bestfriend.global.security.handler.CustomAccessDeniedHandler;
import com.teamjhw.bestfriend.global.security.handler.CustomLoginFailureHandler;
import com.teamjhw.bestfriend.global.security.handler.CustomLoginSuccessHandler;
import com.teamjhw.bestfriend.global.security.service.MemberDetailService;
import com.teamjhw.bestfriend.global.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    /*
     * * 각 URL 패턴에 대한 보안 필터 설정
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // csrf 사용 x
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 사용 x
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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

        // 일반 로그인
        http
                .formLogin(login -> login
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/bf/auth/login")
                .successHandler(new CustomLoginSuccessHandler(jwtUtil))
                .failureHandler(new CustomLoginFailureHandler())
        );
        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
