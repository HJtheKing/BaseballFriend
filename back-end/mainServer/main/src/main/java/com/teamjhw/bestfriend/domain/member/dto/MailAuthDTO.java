package com.teamjhw.bestfriend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MailAuthDTO {
    @Getter
    @Builder
    @Schema(description = "내부에서 인증 메일 전송을 위한 dto")
    public static class SendMailRequest{
        private String receiverAddress;
        private String subject;
        private String content;

        public static SendMailRequest of (String receiverAddress, String subject, String content){
            return SendMailRequest.builder()
                    .receiverAddress(receiverAddress)
                    .subject(subject)
                    .content(content)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "메일 인증 요청을 위한 dto")
    public static class VerifyRequest {
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Schema(description = "이메일 : 빈칸 허용 x")
        @Email
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "인증번호 확인을 위한 dto")
    public static class CheckCodeRequest {
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Schema(description = "이메일 : 빈칸 허용 x")
        @Email
        private String email;

        @Size(min = 6, max = 6, message = "인증코드는 6자리입니다.")
        @NotBlank
        private String code;
    }
}
