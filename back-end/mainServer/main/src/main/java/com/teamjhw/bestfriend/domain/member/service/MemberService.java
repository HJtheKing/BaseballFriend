package com.teamjhw.bestfriend.domain.member.service;

import com.teamjhw.bestfriend.domain.match.repository.TeamRepository;
import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.domain.member.dto.MemberInfoDTO;
import com.teamjhw.bestfriend.domain.member.repository.MemberRepository;
import com.teamjhw.bestfriend.entity.Member;
import com.teamjhw.bestfriend.entity.Team;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MatchException;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import com.teamjhw.bestfriend.global.utils.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final TeamRepository teamRepository;

    /**
     * * 로그인 성공 시 jwt 발급
     *
     * @param memberDetails 로그인 성공 처리된 MemberDetails 객체 - 카카오 로그인된 회원 email이 db에 존재하며 승인된 회원일 경우 로그인 처리
     */
    public String signIn(MemberDetails memberDetails) {
        return jwtUtil.generateToken(memberDetails.getClaims());
    }

    /**
     * 회원 부가 정보 변경
     */
    public void updateMemberInfo(MemberInfoDTO.Request request, Long memberId) {
        Member member = findMemberByMemberId(memberId);

        member.setName(request.getName());
        if (request.getFavoriteTeam() != null) {
            Team team = findTeamByTeamName(request.getFavoriteTeam());
            member.setTeam(team);
        }
        member.setIsBroadcastAllowed(request.getIsBroadcastAllowed());
        member.setIsBriefingAllowed(request.getIsBriefingAllowed());

        memberRepository.save(member);
    }

    /**
     * 회원 부가 정보 조회
     */
    public MemberInfoDTO.Request getMemberInfo(Long memberId) {
        Member member = findMemberByMemberId(memberId);
        String favoriteTeamName = (member.getTeam() == null)? "" : member.getTeam().getTeamName();
        return MemberInfoDTO.Request.of(member.getName(), member.getIsBriefingAllowed(), member.getIsBroadcastAllowed(), favoriteTeamName);
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                               .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

    }

    public Team findTeamByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName)
                             .orElseThrow(() -> new MatchException(ErrorCode.TEAM_NOT_FOUND));
    }
}
