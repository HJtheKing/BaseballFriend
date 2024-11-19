package com.teamjhw.bestfriend.domain.character.repository;

import com.teamjhw.bestfriend.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {

    // MemberId ID를 기준으로 MemberItem 리스트를 조회
    List<MemberItem> findByMemberId(Long memberId);

    //멤버 아이디와 아이템 아이디로 MemberItem 조회
    Optional<MemberItem> findByMemberIdAndItemId(Long memberId, Long itemId);

    // 멤버 아이디와 캐릭터정보 아이디, 아이템 일련번호로 MemberItem 조회
    Optional<MemberItem> findByMemberIdAndMemberCharacterIdAndItem_ItemSerialNumber(Long memberId, Long characterId, Long itemSerialNumber);
}
