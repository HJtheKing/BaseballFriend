package com.teamjhw.bestfriend.domain.member.service;

import com.teamjhw.bestfriend.domain.match.service.MatchInfoService;
import com.teamjhw.bestfriend.domain.member.dto.MailAuthDTO;
import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.domain.member.dto.MemberJoinDTO;
import com.teamjhw.bestfriend.domain.member.repository.MemberRepository;
import com.teamjhw.bestfriend.entity.Member;
import com.teamjhw.bestfriend.entity.Team;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final MatchInfoService matchInfoService;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;
    private final RedisMailService redisMailService;

    /**
     * 회원가입 처리
     * */
    public MemberDetails joinMember(MemberJoinDTO.Request request) {
        // 예외처리 - 비밀번호 확인
        if(!checkPWSame(request)) throw new MemberException(ErrorCode.NOT_SAME_PW);

        Team team = null;
        if(!Objects.equals(request.getFavoriteTeamName(), "")) {
            team = matchInfoService.findTeamByTeamName(request.getFavoriteTeamName());
        }
        Member member = MemberJoinDTO.Request.toEntity(request, team);
        Member newMember = memberRepository.save(member);
        return entityToMemberDetails(newMember);
    }

    /**
     * * member 객체를 PrincipalDetails DTO로 만드는 생성자
     */
    private MemberDetails entityToMemberDetails(Member member) {
        return new MemberDetails(member.getId(), "", "", member.getRole());
    }

    /**
     * 이메일 전송
     * */
    public void joinEmailAuthentication(String email){
        // 예외처리 - 이메일 중복검사
        if(checkEmailUnique(email)) throw new MemberException(ErrorCode.DUPLICATE_EMAIL);

        // 이메일 인증코드 전송
        String verificationCode = mailService.createVerificationCode();
        String content = mailContentBuilder.build(verificationCode);
        MailAuthDTO.SendMailRequest request = MailAuthDTO.SendMailRequest.of(email, "Baseball Friend 회원가입 인증번호 안내", content);
        try {
            mailService.sendMail(request);
        } catch(MailException e) {
            throw new MemberException(ErrorCode.MAIL_SEND_ERROR);
        }

        // 메일 전송 성공 시 redis에 인증번호 등록
        redisMailService.saveData(email, verificationCode);
    }

    /**
     * 인증 코드 확인
     * */
    public Boolean checkEmailAuthentication(MailAuthDTO.CheckCodeRequest request) {
        String verificationCode = redisMailService.getData(request.getEmail());
        if(verificationCode == null) throw new MemberException(ErrorCode.MAIL_AUTH_CODE_NOT_FOUND);
        return verificationCode.equals(request.getCode());
    }

    /**
     * 이메일 중복검사
     * */
    private boolean checkEmailUnique(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 비밀번호 확인
     * */
    public Boolean checkPWSame(MemberJoinDTO.Request request) {
        return Objects.equals(request.getCheckPw(), request.getPw());
    }
}
