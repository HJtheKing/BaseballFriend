//package com.teamjhw.bestfriend.domain.match.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.teamjhw.bestfriend.entity.Team;
//import jakarta.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@Disabled
//@SpringBootTest
//public class TeamRepositoryTest {
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Test
//    @Transactional
//    void 팀_데이터_생성() {
//        List<Team> teams = new ArrayList<Team>();
//
//        Team team1 = Team.builder()
//                .teamName("KIA")
//                .location("광주")
//                .build();
//        teams.add(team1);
//
//        Team team2 = Team.builder()
//                         .teamName("삼성")
//                         .location("대구")
//                         .build();
//        teams.add(team2);
//
//        Team team3 = Team.builder()
//                         .teamName("LG")
//                         .location("잠실")
//                         .build();
//        teams.add(team3);
//
//        Team team4 = Team.builder()
//                         .teamName("두산")
//                         .location("잠실")
//                         .build();
//        teams.add(team4);
//
//        Team team5 = Team.builder()
//                         .teamName("KT")
//                         .location("수원")
//                         .build();
//        teams.add(team5);
//
//        Team team6 = Team.builder()
//                         .teamName("SSG")
//                         .location("문학")
//                         .build();
//        teams.add(team6);
//
//        Team team7 = Team.builder()
//                         .teamName("롯데")
//                         .location("사직")
//                         .build();
//        teams.add(team7);
//
//        Team team8 = Team.builder()
//                         .teamName("한화")
//                         .location("대전")
//                         .build();
//        teams.add(team8);
//
//        Team team9 = Team.builder()
//                         .teamName("NC")
//                         .location("창원")
//                         .build();
//        teams.add(team9);
//
//        Team team10 = Team.builder()
//                         .teamName("키움")
//                         .location("고척")
//                         .build();
//        teams.add(team10);
//
//        Team team11 = Team.builder()
//                         .teamName("없음")
//                         .location("없음")
//                         .build();
//        teams.add(team11);
//
//        teamRepository.saveAll(teams);
//
//        // 저장 확인
//        assertThat(teamRepository.findById(team1.getId()).isPresent());
//    }
//}
