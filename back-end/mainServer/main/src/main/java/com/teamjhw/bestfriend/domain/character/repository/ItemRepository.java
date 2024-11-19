package com.teamjhw.bestfriend.domain.character.repository;

import com.teamjhw.bestfriend.entity.CharacterInfo;
import com.teamjhw.bestfriend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // memberId 에 포함되지 않는 Item 를 조회
    @Query("SELECT i FROM Item i WHERE i.id NOT IN " +
            "(SELECT mi.item.id FROM MemberItem mi WHERE mi.member.id = :memberId)")
    List<Item> findShopItems(@Param("memberId") Long memberId);

    // 아이템 일련번호로 Item 조회
    Optional<Item> findByItemSerialNumber(Long itemSerialNumber);
}
