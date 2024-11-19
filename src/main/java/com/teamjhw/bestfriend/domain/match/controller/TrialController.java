package com.teamjhw.bestfriend.domain.match.controller;

import com.teamjhw.bestfriend.domain.match.service.FlaskService;
import com.teamjhw.bestfriend.domain.match.service.MatchInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bf")
@RequiredArgsConstructor
public class TrialController {

    private final MatchInfoService matchInfoService;
    private final FlaskService flaskService;

    @GetMapping("/trial/{id}")
    ResponseEntity<?> updateAndSendToFlask(@PathVariable("id") Long matchInfoId){
        String matchTime = matchInfoService.updateMatchTime(matchInfoId, 1L);
        // 나머지 경기는 오전으로 당기기 -> flask controller 경기 저장하는 요청 넣기
        matchInfoService.makeFinishMatch(matchInfoId);

        flaskService.sendToFlaskStartBroadcast(matchTime);
        return ResponseEntity.ok().build();
    }

}
