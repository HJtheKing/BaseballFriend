package com.teamjhw.bestfriend.domain.member.service;

import com.teamjhw.bestfriend.domain.member.dto.MailAuthDTO;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String SenderAddress;

    /**
     * 인증 메일 전송
     */
    public boolean sendMail(MailAuthDTO.SendMailRequest request) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(SenderAddress);
            messageHelper.setTo(request.getReceiverAddress());
            messageHelper.setSubject(request.getSubject());
            messageHelper.setText(request.getContent(), true);
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            log.error("메일 전송 실패: {}", e.getMessage());
            throw new MemberException(ErrorCode.MAIL_SEND_ERROR);
        }
        return true;
    }

    /**
     * 6자리 숫자로 된 인증번호 생성
     */
    public String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int tmp = random.nextInt(10);
            sb.append(tmp);
        }
        return sb.toString();
    }
}
