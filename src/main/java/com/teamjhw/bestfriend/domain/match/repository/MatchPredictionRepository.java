package com.teamjhw.bestfriend.domain.match.repository;

import com.teamjhw.bestfriend.entity.MatchPrediction;
import com.teamjhw.bestfriend.entity.Member;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {

    /**
     * 특정 멤버의 특정 경기 승부 예측 참여 여부 조회
     */
    boolean existsByMemberIdAndMatchInfoId(Long memberId, Long matchInfoId);

    /**
     * 특정 멤버가 오늘 승부 예측에 참여한 적이 있는지 조회
     */
    @Query("SELECT COUNT(*) > 0 " +
            "FROM MatchPrediction mp " +
            "WHERE mp.member = :member "+
            "AND mp.createdAt = :today")
    boolean existsPredictionByMemberAndToday(@Param("member") Member member, @Param("today") LocalDate today);

    /**
     * 어제 승부 예측에 참여하지 않은 멤버 목록 조회
     */
    @Query("SELECT mp.member " +
            "FROM MatchPrediction mp " +
            "WHERE mp.member NOT IN (SELECT DISTINCT m.member " +
            "FROM MatchPrediction m " +
            "WHERE m.createdAt = :yesterday)")
    List<Member> findMembersWithoutPredictionOnYesterday(@Param("yesterday") LocalDate yesterday);

}