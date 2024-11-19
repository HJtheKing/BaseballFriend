package com.teamjhw.bestfriend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    /**
     * 메일 인증 템플릿 양식과 데이터 (인증 링크)를 합쳐서 html 형식으로 반환
     * */
    public String build(String verificationCode) {
        Context context = new Context();

        context.setVariable("verificationCode", verificationCode);
        return templateEngine.process("mail-template", context);
    }
}
