package com.teamjhw.bestfriend.global.security.service;

import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.domain.member.repository.MemberRepository;
import com.teamjhw.bestfriend.entity.Member;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * *  ("/qufit/admin/login")로 요청된 관리자 로그인 요청을 처리함
 * */
@Service
@RequiredArgsConstructor
@Log4j2
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        log.info("[login try] : " + email);
        return MemberDetails.builder()
                                .id(member.getId())
                                .email("")
                                .pw(member.getPw())
                                .role(member.getRole())
                                .build();
        }
    }