package com.teamjhw.bestfriend.domain.character.repository;

import com.teamjhw.bestfriend.entity.Background;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BackgroundRepository extends JpaRepository<Background, Long> {

    // memberId 에 포함되지 않는 Background 를 조회
    @Query("SELECT b FROM Background b WHERE b.id NOT IN " +
            "(SELECT mb.background.id FROM MemberBackground mb WHERE mb.member.id = :memberId)")
    List<Background> findShopBackgrounds(@Param("memberId") Long memberId);

    // 배경 일련번호로 Background 를 조회
    Optional<Background> findByBackgroundSerialNumber(Long backgroundSerialNumber);
}
