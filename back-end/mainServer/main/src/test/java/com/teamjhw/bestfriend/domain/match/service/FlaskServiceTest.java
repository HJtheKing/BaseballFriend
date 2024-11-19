//package com.teamjhw.bestfriend.domain.match.service;
//
//import com.teamjhw.bestfriend.domain.match.dto.MatchScheduleDTO;
//import jakarta.transaction.Transactional;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class FlaskServiceTest {
//    @Autowired
//    private FlaskService flaskService;
//    @Autowired
//    private MatchInfoService matchInfoService;
//
//    @Test
//    @Transactional
//    void 경기일정_데이터_생성() {
//        List<MatchScheduleDTO.FlaskResponse> responses = flaskService.createMatchSchedulesOnMonth(10);
//        matchInfoService.createMatchSchedulesOnMonth(responses);
//
//    }
//}
