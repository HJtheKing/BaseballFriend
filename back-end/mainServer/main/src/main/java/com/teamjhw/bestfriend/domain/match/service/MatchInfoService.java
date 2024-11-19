package com.teamjhw.bestfriend.domain.match.service;

import com.teamjhw.bestfriend.domain.match.dto.MatchFinishDTO;
import com.teamjhw.bestfriend.domain.match.dto.MatchScheduleDTO;
import com.teamjhw.bestfriend.domain.match.dto.SuspendedMatchInfoDTO;
import com.teamjhw.bestfriend.domain.match.dto.SuspendedMatchInfoDTO.Response;
import com.teamjhw.bestfriend.domain.match.dto.TeamRankDTO;
import com.teamjhw.bestfriend.domain.match.dto.UpcomingMatchInfoDTO;
import com.teamjhw.bestfriend.domain.match.dto.UpcomingMatchInfoDTO.Request;
import com.teamjhw.bestfriend.domain.match.repository.MatchInfoRepository;
import com.teamjhw.bestfriend.domain.match.repository.TeamRankRepository;
import com.teamjhw.bestfriend.domain.match.repository.TeamRepository;
import com.teamjhw.bestfriend.domain.match.util.MatchDateUtil;
import com.teamjhw.bestfriend.entity.MatchInfo;
import com.teamjhw.bestfriend.entity.MatchResult;
import com.teamjhw.bestfriend.entity.Team;
import com.teamjhw.bestfriend.entity.TeamRank;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MatchException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MatchInfoService {

    private final MatchInfoRepository matchInfoRepository;
    private final TeamRankRepository teamRankRepository;
    private final TeamRepository teamRepository;

    /**
     * 오늘 경기 시각 체크
     */
    public Set<String> getTodayMatchesTime() {
        Set<String> todayMatchesTime = new HashSet<>();

        // 오늘의 경기를 조회
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        List<MatchInfo> matches = matchInfoRepository.findByMatchDateBetween(todayStart, todayEnd);

        for (MatchInfo matchInfo : matches) {
            String matchTime = MatchDateUtil.toStringTime(matchInfo.getMatchDate());
            todayMatchesTime.add(matchTime);
        }
        return todayMatchesTime;
    }

    /**
     * 곧 있을 경기 위치 list 조회
     */
    public List<Request> getUpcomingMatchInfo(String matchTime) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = MatchDateUtil.parseDateTime(matchTime, today);

        List<MatchInfo> matches = matchInfoRepository.findByMatchDate(now);
        List<UpcomingMatchInfoDTO.Request> dtos = new ArrayList<>();
        for (MatchInfo match : matches) {
            UpcomingMatchInfoDTO.Request request = UpcomingMatchInfoDTO.Request.of(matchTime, match.getLocation());
            dtos.add(request);
        }
        return dtos;
    }

    /**
     * 날짜별 경기 일정 조회
     */
    public MatchScheduleDTO.Responses getMatchesOnDate(LocalDate date) {
        String strDate = MatchDateUtil.toStringDate(LocalDate.now());
        List<MatchScheduleDTO.Response> matchSchedule = matchInfoRepository.findMatchesByDate(date).stream()
                                                                           .map(MatchScheduleDTO.Response::of)
                                                                           .collect(Collectors.toList());

        return MatchScheduleDTO.Responses.of(strDate, matchSchedule);
    }

    /**
     * 팀 일정 저장
     */
    public void createMatchSchedulesOnMonth(List<MatchScheduleDTO.FlaskResponse> responses) {
        List<MatchInfo> entities = new ArrayList<>();

        for (MatchScheduleDTO.FlaskResponse f : responses) {
            Team teamHome = findTeamByTeamName(f.getTeamHomeName());
            Team teamAway = findTeamByTeamName(f.getTeamAwayName());
            LocalDateTime parsingMatchDate = MatchDateUtil.parseKBODatetoLocalDate(f.getMatchDate(), f.getMatchTime());

            MatchInfo entity = MatchScheduleDTO.FlaskResponse.toEntity(f, teamHome, teamAway, parsingMatchDate);
            entities.add(entity);
        }
        try {
            matchInfoRepository.saveAll(entities);
        } catch (Exception e) {
            throw new MatchException(ErrorCode.DUPLICATE_MATCH_SCHEDULE);
        }
    }


    /**
     * 팀 순위 조회
     */
    public List<TeamRankDTO.Response> getTeamRank() {
        return teamRankRepository.findAll().stream()
                                 .map(TeamRankDTO.Response::of)
                                 .collect(Collectors.toList());

    }

    /**
     * 팀 순위 저장
     */
    public void saveTeamRank(List<TeamRankDTO.Response> responses) {
        // 저장할 데이터를 리스트로 변환
        List<TeamRank> teamRanks = responses.stream()
                                            .map(TeamRankDTO.Response::toEntity)
                                            .collect(Collectors.toList());

        // 저장
        teamRankRepository.saveAll(teamRanks);
    }

    /**
     * 경기 결과 값 저장
     */
    public Long updateMatchInfoResult(MatchFinishDTO.Request request) {
        // 1. 예외 처리
        if (request.getMatchResult() < 1 || request.getMatchResult() > 5) {
            throw new MatchException(ErrorCode.UNDEFINED_MATCH_RESULT);
        }

        // 2. 날짜, 장소 기반으로 MatchInfo 조회
        MatchInfo matchInfo = findMatchInfoByMatchDateAndLocation(request.getMatchTime(), LocalDate.now(),
                                                                  request.getLocation());

        // 3. 결과 업데이트
        matchInfo.updateResult(request.getHomeScore(), request.getAwayScore(), request.getMatchResult());
        matchInfoRepository.save(matchInfo);

        // 4. 부모 경기 있는지 확인, 부모 결과 업데이트
        if(matchInfo.getParentMatchInfoId() != null) {
            MatchInfo parentMatchInfo = findMatchInfoById(matchInfo.getParentMatchInfoId());
            parentMatchInfo.updateResult(request.getHomeScore(), request.getAwayScore(), request.getMatchResult());
        }

        return matchInfo.getId();
    }

    /**
     * 어제 경기 중 취소된 경기가 있는지 확인
     * */
    public List<MatchInfo> checkMatchCanceled() {
        LocalDate yesterday = LocalDate.now().minusDays(1);


        List<MatchInfo> canceledMatches = matchInfoRepository.findAllByMatchDateAndResultAndNoParent(yesterday, MatchResult.NOT_FINISHED);

        if(canceledMatches == null || canceledMatches.isEmpty()) {
            log.info("취소할 경기 없음");
            return canceledMatches;
        }

        for(MatchInfo match : canceledMatches){
            match.cancelMatch();
        }

        matchInfoRepository.saveAll(canceledMatches);
        return canceledMatches;
    }

    /**
     * 중단 경기에 대해 새로운 경기 일정 생성
     */
    public Response createSuspendedMatchInfo(SuspendedMatchInfoDTO.Request request) {
        // 1. 예외 처리
        if (request.getMatchResult() != MatchResult.STOP.getKey()) {
            throw new MatchException(ErrorCode.UNDEFINED_MATCH_RESULT);
        }

        // 2. 날짜, 장소 기반으로 기존 MatchInfo 조회
        MatchInfo matchInfo = findMatchInfoByMatchDateAndLocation(request.getOldMatchTime(), LocalDate.now(),
                                                                  request.getOldLocation());

        // 3. 서스펜디드 경기 생성
        MatchInfo suspendedMatchInfo = SuspendedMatchInfoDTO.Request.toEntity(matchInfo, request);
        return SuspendedMatchInfoDTO.Response.of(matchInfoRepository.save(suspendedMatchInfo));
    }

    public void deleteTeamRank() {
        teamRankRepository.deleteAll();
    }

    public Team findTeamByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName)
                             .orElseThrow(() -> new MatchException(ErrorCode.TEAM_NOT_FOUND));
    }

    private MatchInfo findMatchInfoByMatchDateAndLocation(String time, LocalDate date, String location) {
        LocalDateTime matchDate = MatchDateUtil.parseDateTime(time, date);
        return matchInfoRepository.findByMatchDateAndLocation(matchDate, location)
                                  .orElseThrow(() -> new MatchException(ErrorCode.MATCH_NOT_FOUND));
    }

    private MatchInfo findMatchInfoById(Long matchInfoId) {
        return matchInfoRepository.findById(matchInfoId).orElseThrow(() ->
                                                                             new MatchException(
                                                                                     ErrorCode.MATCH_NOT_FOUND));
    }

    public String updateMatchTime(Long matchInfoId, Long min){
        MatchInfo matchInfo = findMatchInfoById(matchInfoId);
        matchInfo.updateMatchDate(LocalDateTime.now().plusMinutes(min).withSecond(0).withNano(0));
        matchInfoRepository.save(matchInfo);
        return MatchDateUtil.toStringTime(matchInfo.getMatchDate());
    }

    public void makeFinishMatch(Long matchInfoId) {
        // matchInfoId 와 같은 날 경기 찾기
        MatchInfo targetMatch = findMatchInfoById(matchInfoId);
        List<MatchInfo> matchInfos = matchInfoRepository.findMatchesByDate(LocalDate.from(targetMatch.getMatchDate()));
        for(MatchInfo matchInfo : matchInfos){
            if(Objects.equals(matchInfo.getId(),matchInfoId)) continue;
            // 나머지 경기 오전 10시로 바꾸기
            matchInfo.updateMatchDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(10,0)));
            matchInfoRepository.save(matchInfo);
        }
    }
}
