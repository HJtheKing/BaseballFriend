package com.teamjhw.bestfriend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamjhw.bestfriend.global.common.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/*
 * * form형태로 요청되는 관리자 로그인 실패 처리
 *
 * */
@Log4j2
@RequiredArgsConstructor
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("------------login failure-----------");

        String msg = "";

        if(exception instanceof BadCredentialsException) {
            msg = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
        } else if(exception instanceof UsernameNotFoundException) {
            msg = "존재하지 않는 계정입니다. 회원가입이 필요합니다.";
        } else {
            msg = "로그인 중 오류가 발생했습니다.";
        }

        CommonResponse loginResult = CommonResponse.builder()
                                                        .isSuccess(false)
                                                        .message(msg)
                                                        .build();
        // JSON으로 변환
        String jsonResponse = new ObjectMapper().writeValueAsString(loginResult);

        // 응답에 Error Response 저장
        response.setStatus(400);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
