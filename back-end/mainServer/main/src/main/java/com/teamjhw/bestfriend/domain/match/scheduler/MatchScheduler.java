package com.teamjhw.bestfriend.domain.match.scheduler;

import com.teamjhw.bestfriend.domain.match.dto.TeamRankDTO;
import com.teamjhw.bestfriend.domain.match.service.FlaskService;
import com.teamjhw.bestfriend.domain.match.service.GameService;
import com.teamjhw.bestfriend.domain.match.service.MatchInfoService;
import com.teamjhw.bestfriend.domain.match.util.MatchDateUtil;
import com.teamjhw.bestfriend.entity.MatchInfo;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class MatchScheduler {

    private final MatchInfoService matchInfoService;
    private final FlaskService flaskService;
    private final GameService gameService;

    private Set<String> todayMatchesTime = new HashSet<>(); // 오늘 경기 시각

    /**
     * 오늘 경기 시각 체크
     * <p>
     * 매일 03시에 오늘 있을 경기 시각을 체크합니다.
     */
    @Async
    @Scheduled(cron = "0 0 03 * * ?")
    public void getTodayMatchesTime() {
        log.info("---오늘의 경기일정은---");
        todayMatchesTime = matchInfoService.getTodayMatchesTime();
        log.info(todayMatchesTime.toString());
    }

    /**
     * 경기 중계 요청
     * <p>
     * 13시부터 19시까지 30분마다 중계할 경기가 있는지 체크합니다.
     */
//    @Async
//    @Scheduled(cron = "0 0/30 13-19 * * ?")
//    public void checkMatchesAtHalfHour() {
//        log.info("-----곧 경기가 시작하는지 확인----");
//        LocalTime now = LocalTime.now();
//        LocalTime nowPlus30Min = now.plusMinutes(120);
//
//        for (String matchTime : todayMatchesTime) {
//            LocalTime matchLocalTime = MatchDateUtil.parseTime(matchTime);
//
//            // 30분 내 시작하는 경기 있는지 확인
//            if (matchLocalTime.isAfter(now) && matchLocalTime.isBefore(nowPlus30Min)) {
//                // 있다면 실행 요청
//                log.info("곧 경기가 있어요! ={}", matchTime);
//                flaskService.sendToFlaskStartBroadcast(matchTime);
//                todayMatchesTime.remove(matchTime);
//                break;
//            }
//        }
//    }

    /**
     * 팀 순위 조회 요청
     * <p>
     * 매일 00시에 팀 전체 순위를 조회합니다.
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void getTeamRank() {
        log.info("-----팀 순위 조회 및 저장-----");
        List<TeamRankDTO.Response> responses = flaskService.getTeamRank();

        matchInfoService.deleteTeamRank();
        matchInfoService.saveTeamRank(responses);
    }

    /**
     * 경기 취소 확인
     * <p>
     * 1: MatchInfo 결과 반영 2 : MatchPrediction의 Amount 돌려주기
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkMatchCanceled() {
        log.info("-----오늘 취소된 경기가 있는지 확인합니다.-----");

        List<MatchInfo> canceledMatches = matchInfoService.checkMatchCanceled();

        if (canceledMatches == null || canceledMatches.isEmpty()) {
            return;
        }

        for (MatchInfo matchInfo : canceledMatches) {
            gameService.updateMatchPredictionByResult(matchInfo);
        }
    }
}
