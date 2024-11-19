//package com.teamjhw.bestfriend.domain.match.repository;
//
//import com.teamjhw.bestfriend.entity.MatchInfo;
//import com.teamjhw.bestfriend.entity.Team;
//import jakarta.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@Disabled
//@SpringBootTest
//public class MatchInfoRepositoryTest {
//
//    @Autowired
//    private MatchInfoRepository matchInfoRepository;
//
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Test
//    @Transactional
//    void 더미_경기일정_데이터_생성(){
//        Team kia = teamRepository.findById(1L).get();
//        Team 삼성 = teamRepository.findById(2L).get();
//        Team lg = teamRepository.findById(3L).get();
//        Team 두산 = teamRepository.findById(4L).get();
//        Team kt = teamRepository.findById(5L).get();
//        Team ssg = teamRepository.findById(6L).get();
//
//        List<MatchInfo> matches = new ArrayList<>();
//        MatchInfo match1 = MatchInfo.builder()
//                .teamHome(삼성)
//                .teamAway(kia)
//                .location("대구")
//                .matchDate(LocalDateTime.of(2024,11,18,22, 0))
//                .build();
//        matches.add(match1);
//
//        MatchInfo match2 = MatchInfo.builder()
//                                    .teamHome(lg)
//                                    .teamAway(두산)
//                                    .location("잠실")
//                                    .matchDate(LocalDateTime.of(2024,11,18,22, 0))
//                                    .build();
//        matches.add(match2);
//
//        MatchInfo match3 = MatchInfo.builder()
//                                    .teamHome(kt)
//                                    .teamAway(ssg)
//                                    .location("수원")
//                                    .matchDate(LocalDateTime.of(2024,11,18,22, 0))
//                                    .build();
//        matches.add(match3);
//
//        matchInfoRepository.saveAll(matches);
//    }
//
//}
