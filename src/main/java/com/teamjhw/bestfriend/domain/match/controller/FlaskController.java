package com.teamjhw.bestfriend.domain.match.controller;

import com.teamjhw.bestfriend.domain.match.dto.MatchFinishDTO;
import com.teamjhw.bestfriend.domain.match.dto.SuspendedMatchInfoDTO;
import com.teamjhw.bestfriend.domain.match.service.GameService;
import com.teamjhw.bestfriend.domain.match.service.MatchInfoService;
import com.teamjhw.bestfriend.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bf/match")
public class FlaskController {

    private final MatchInfoService matchInfoService;
    private final GameService gameService;

    @PatchMapping("/result")
    ResponseEntity<?> finishGame(@RequestBody MatchFinishDTO.Request request) {
        Long matchInfoId = matchInfoService.updateMatchInfoResult(request);
        gameService.updateMatchPredictionByResult(matchInfoId);
        CommonResponse response = CommonResponse.builder()
                                                .isSuccess(true)
                                                .message("경기 결과 저장 & 승부 예측 결과 반영 성공")
                                                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping
    ResponseEntity<?> createSuspendedGame(@RequestBody SuspendedMatchInfoDTO.Request request) {
        SuspendedMatchInfoDTO.Response response = matchInfoService.createSuspendedMatchInfo(request);;
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
