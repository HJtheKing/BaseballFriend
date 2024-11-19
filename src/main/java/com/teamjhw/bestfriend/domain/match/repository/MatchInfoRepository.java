package com.teamjhw.bestfriend.domain.match.repository;

import com.teamjhw.bestfriend.entity.MatchInfo;
import com.teamjhw.bestfriend.entity.MatchResult;
import com.teamjhw.bestfriend.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {

    @Query("SELECT mi, mp " +
            "FROM MatchInfo mi LEFT JOIN mi.matchPredictions mp " +
            "WHERE (mp IS NULL OR mp.member = :member) " +
            "AND FUNCTION('DATE_FORMAT', mi.matchDate, '%Y%m%d') = FUNCTION('DATE_FORMAT', :date, '%Y%m%d')")
    List<Object[]> findMatchInfoAndPredictions(@Param("member") Member member, @Param("date") LocalDate date);

    /**
     * startDate와 endDate 사이의 경기들을 조회
     * */
    List<MatchInfo> findByMatchDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * datetime에 시작하는 경기들 조회
     * */
    List<MatchInfo> findByMatchDate(LocalDateTime date);

    /**
     * 날짜에 있는 모든 경기 일정 조회
     * */
    @Query("SELECT m FROM MatchInfo m WHERE DATE(m.matchDate) = :date")
    List<MatchInfo> findMatchesByDate(@Param("date") LocalDate date);

    /**
     * 어제 경기 존재하는지 조회
     */
    @Query("SELECT COUNT(*) > 0 FROM MatchInfo mi WHERE Date(mi.matchDate) = :date")
    boolean existsMatchInfoByYesterday(@Param("date") LocalDate date);

    /**
     * 장소 & 시간으로 MatchInfo 조회
     */
    @Query("SELECT mi FROM MatchInfo mi WHERE mi.matchDate = :date AND mi.location = :location")
    Optional<MatchInfo> findByMatchDateAndLocation(@Param("date") LocalDateTime date, @Param("location") String location);

    /**
     *  날짜와 경기 결과로 MatchInfo 조회
     */
    @Query("SELECT mi FROM MatchInfo mi WHERE Date(mi.matchDate) = :date AND mi.matchResult = :matchResult AND mi.parentMatchInfoId is null")
    @EntityGraph(attributePaths = {"matchPredictions", "matchPredictions.member"})
    List<MatchInfo> findAllByMatchDateAndResultAndNoParent(@Param("date") LocalDate date, @Param("matchResult") MatchResult matchResult);
}