package com.teamjhw.bestfriend.domain.character.repository;

import com.teamjhw.bestfriend.entity.MemberBackground;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberBackgroundRepository extends JpaRepository<MemberBackground, Long> {

    // MemberId ID를 기준으로 MemberBackground 리스트를 조회
    List<MemberBackground> findByMemberId(Long memberId);

    // 멤버 아이디와 배경 아이디로 MemberBackground 조회
    Optional<MemberBackground> findByMemberIdAndBackgroundId(Long memberId, Long backgroundId);

    // 멤버 아이디와 배경 일련번호 MemberBackground 조회
    Optional<MemberBackground> findByMemberIdAndBackground_BackgroundSerialNumber(Long memberId, Long backgroundSerialNumber);
}
