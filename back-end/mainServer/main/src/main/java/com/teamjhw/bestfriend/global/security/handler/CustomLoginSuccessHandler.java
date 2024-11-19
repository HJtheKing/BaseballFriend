package com.teamjhw.bestfriend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.global.common.response.CommonResponse;
import com.teamjhw.bestfriend.global.utils.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/*
* * form형태로 요청되는 관리자 로그인 성공 처리
*
* @return : accessToken header 주입
* */
@Log4j2
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("------------login success-----------");
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(memberDetails.getClaims());


        CommonResponse loginResult = CommonResponse.builder()
                                                        .isSuccess(true)
                                                        .message("로그인에 성공했습니다.")
                                                        .build();
        // JSON으로 변환
        String jsonResponse = new ObjectMapper().writeValueAsString(loginResult);

        // 응답에 Response 저장
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
        response.addHeader("Authorization", "Bearer " + accessToken);
    }
}
