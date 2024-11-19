package com.teamjhw.bestfriend.domain.character.repository;

import com.teamjhw.bestfriend.entity.MemberCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCharacterRepository extends JpaRepository<MemberCharacter, Long> {

    // MemberId ID를 기준으로 MemberCharacter 리스트를 조회
    List<MemberCharacter> findByMemberId(Long memberId);

    // 멤버 아이디와 캐릭터정보 아이디로 MemberCharacter 조회
    Optional<MemberCharacter> findByMemberIdAndCharacterInfoId(Long memberId, Long characterInfoId);

    // 멤버 아이디와 캐릭터 일련번호로 MemberCaracter 조회
    Optional<MemberCharacter> findByMemberIdAndCharacterInfo_CharacterInfoSerialNumber(Long memberId, Long characterInfoSerialNumber);
}
