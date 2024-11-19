package com.teamjhw.sse.domain.member.service;

import com.teamjhw.sse.domain.member.dto.MemberResDto;
import com.teamjhw.sse.domain.member.repository.MemberRepository;
import com.teamjhw.sse.global.exception.ErrorCode;
import com.teamjhw.sse.global.exception.exceptionType.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    
    public MemberResDto findMemberById(Long id) {
        return memberRepository.getMemberById(id)
                 .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
