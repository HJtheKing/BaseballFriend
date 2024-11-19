package com.teamjhw.bestfriend.domain.character.repository;

import com.teamjhw.bestfriend.entity.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CharacterInfoRepository extends JpaRepository<CharacterInfo, Long> {

    // memberId 에 포함되지 않는 characterInfo 를 조회
    @Query("SELECT c FROM CharacterInfo c WHERE c.id NOT IN " +
            "(SELECT mc.characterInfo.id FROM MemberCharacter mc WHERE mc.member.id = :memberId)")
    List<CharacterInfo> findShopCharacters(@Param("memberId") Long memberId);

    // 캐릭터 일련번호로 characterInfo 를 조회
    Optional<CharacterInfo> findByCharacterInfoSerialNumber(Long characterInfoSerialNumber);
}
