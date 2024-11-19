package com.teamjhw.bestfriend.domain.member.dto;

import com.teamjhw.bestfriend.entity.Member;
import com.teamjhw.bestfriend.entity.MemberRole;
import com.teamjhw.bestfriend.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class MemberJoinDTO {

    @Getter
    @Builder
    @Schema(description = "[ MemberInfoDTO ] 회원 가입, 회원 정보 요청 DTO")
    public static class Request {

        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Schema(description = "이메일 : 빈칸 허용 x")
        @Email
        private String email;
        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        @Pattern(regexp = "^(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$", message = "비밀번호 형식이 올바르지 않습니다. 8자 이상 20자 이하, 특수문자(@$!%*?&#) 포함")
        @Schema(description = "비밀번호 : 8자리 이상 20자리 이하, 특수문자 1자 이상 필수, 빈칸 허용 x")
        private String pw;
        @NotBlank(message = "비밀번호 확인은 필수 입력입니다.")
        @Pattern(regexp = "^(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$", message = "비밀번호 형식이 올바르지 않습니다. 8자 이상 20자 이하, 특수문자(@$!%*?&#) 포함")
        @Schema(description = "비밀번호 확인 : 8자리 이상 20자리 이하, 특수문자 1자 이상 필수, 빈칸 허용 x")
        private String checkPw;

        @NotBlank(message = "이름은 필수 입력입니다.")
        @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
        private String name;

        private String favoriteTeamName;

        @AssertTrue(message = "이메일 인증은 필수입니다.")
        private Boolean isValidEmail;

        public static Member toEntity(Request request, Team team) {
            return Member.builder()
                         .email(request.getEmail())
                         .pw(BCrypt.hashpw(request.getPw(), BCrypt.gensalt()))
                         .name(request.getName())
                         .team(team)
                         .token("")
                         .gameMoney(500L)
                         .consecutiveDays(0)
                         .totalDays(0)
                         .isBriefingAllowed(true)
                         .isBroadcastAllowed(true)
                         .role(MemberRole.USER)
                         .build();
        }
    }
}
