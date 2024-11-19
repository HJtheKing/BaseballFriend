package com.teamjhw.bestfriend.domain.match.controller;

import com.teamjhw.bestfriend.domain.match.dto.MatchScheduleDTO;
import com.teamjhw.bestfriend.domain.match.dto.TeamRankDTO.Response;
import com.teamjhw.bestfriend.domain.match.service.MatchInfoService;
import com.teamjhw.bestfriend.global.common.response.CommonResponse;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MatchException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bf/match")
public class MatchInfoController {

    private final MatchInfoService matchInfoService;

    /**
     * 경기 일정 조회
     *
     * @param date 일정 조회할 날짜
     */
    @GetMapping
    ResponseEntity<?> getMatchInfo(@RequestParam LocalDate date) {
        MatchScheduleDTO.Responses responses = matchInfoService.getMatchesOnDate(date);
        if (responses.getMatchList() == null || responses.getMatchList().isEmpty()) {
            return new ResponseEntity<>(CommonResponse.of(true, "해당 일자의 경기가 없습니다."), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * 경기 순위 조회
     */
    @GetMapping("/rank")
    ResponseEntity<?> getTeamRank() {
        try {
            List<Response> response = matchInfoService.getTeamRank();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) { throw new MatchException(ErrorCode.TEAM_RANK_ERROR);
        }
    }
}
