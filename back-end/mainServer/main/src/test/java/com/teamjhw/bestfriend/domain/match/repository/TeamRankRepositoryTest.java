//package com.teamjhw.bestfriend.domain.match.repository;
//
//import com.teamjhw.bestfriend.entity.TeamRank;
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
//public class TeamRankRepositoryTest {
//    @Autowired
//    private TeamRankRepository teamRankRepository;
//
//    @Test
//    @Transactional
//    void 더미_팀순위_데이터_생성() {
//        teamRankRepository.deleteAll();
//
//        List<TeamRank> list = new ArrayList<TeamRank>();
//        LocalDateTime now = LocalDateTime.now();
//
//        TeamRank teamRank1 = TeamRank.builder()
//                .teamRank(1)
//                .createdAt(now)
//                .teamName("KIA")
//                .winCount(87)
//                .lossCount(55)
//                .drawCount(2)
//                .odds(0.613)
//                .last10GamesResults("5승0무5패")
//                .build();
//        list.add(teamRank1);
//
//        TeamRank teamRank2 = TeamRank.builder()
//                                     .teamRank(2)
//                                     .createdAt(now)
//                                     .teamName("삼성")
//                                     .winCount(78)
//                                     .lossCount(64)
//                                     .drawCount(2)
//                                     .odds(0.549)
//                                     .last10GamesResults("3승0무7패")
//                                     .build();
//        list.add(teamRank2);
//        TeamRank teamRank3 = TeamRank.builder()
//                                     .teamRank(3)
//                                     .createdAt(now)
//                                     .teamName("LG")
//                                     .winCount(76)
//                                     .lossCount(66)
//                                     .drawCount(2)
//                                     .odds(0.535)
//                                     .last10GamesResults("7승0무3패")
//                                     .build();
//        list.add(teamRank3);
//        TeamRank teamRank4 = TeamRank.builder()
//                                     .teamRank(4)
//                                     .createdAt(now)
//                                     .teamName("두산")
//                                     .winCount(74)
//                                     .lossCount(68)
//                                     .drawCount(2)
//                                     .odds(0.521)
//                                     .last10GamesResults("8승0무2패")
//                                     .build();
//        list.add(teamRank4);
//        TeamRank teamRank5 = TeamRank.builder()
//                                     .teamRank(5)
//                                     .createdAt(now)
//                                     .teamName("KT")
//                                     .winCount(72)
//                                     .lossCount(70)
//                                     .drawCount(2)
//                                     .odds(0.507)
//                                     .last10GamesResults("5승0무5패")
//                                     .build();
//        list.add(teamRank5);
//        TeamRank teamRank6 = TeamRank.builder()
//                                     .teamRank(6)
//                                     .createdAt(now)
//                                     .teamName("SSG")
//                                     .winCount(72)
//                                     .lossCount(70)
//                                     .drawCount(2)
//                                     .odds(0.507)
//                                     .last10GamesResults("8승0무2패")
//                                     .build();
//        list.add(teamRank6);
//        TeamRank teamRank7 = TeamRank.builder()
//                                     .teamRank(7)
//                                     .createdAt(now)
//                                     .teamName("롯데")
//                                     .winCount(66)
//                                     .lossCount(74)
//                                     .drawCount(4)
//                                     .odds(0.471)
//                                     .last10GamesResults("4승0무6패")
//                                     .build();
//        list.add(teamRank7);
//        TeamRank teamRank8 = TeamRank.builder()
//                                     .teamRank(8)
//                                     .createdAt(now)
//                                     .teamName("한화")
//                                     .winCount(66)
//                                     .lossCount(76)
//                                     .drawCount(2)
//                                     .odds(0.465)
//                                     .last10GamesResults("5승0무5패")
//                                     .build();
//        list.add(teamRank8);
//        TeamRank teamRank9 = TeamRank.builder()
//                                     .teamRank(9)
//                                     .createdAt(now)
//                                     .teamName("NC")
//                                     .winCount(61)
//                                     .lossCount(81)
//                                     .drawCount(2)
//                                     .odds(0.430)
//                                     .last10GamesResults("2승0무8패")
//                                     .build();
//        list.add(teamRank9);
//        TeamRank teamRank10 = TeamRank.builder()
//                                      .teamRank(10)
//                                     .createdAt(now)
//                                     .teamName("키움")
//                                     .winCount(58)
//                                     .lossCount(86)
//                                     .drawCount(0)
//                                     .odds(0.403)
//                                     .last10GamesResults("1승0무9패")
//                                     .build();
//        list.add(teamRank10);
//        teamRankRepository.saveAll(list);
//    }
//}
