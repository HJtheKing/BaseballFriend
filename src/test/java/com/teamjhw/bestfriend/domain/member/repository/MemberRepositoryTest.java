//package com.teamjhw.bestfriend.domain.member.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.teamjhw.bestfriend.entity.Member;
//import com.teamjhw.bestfriend.entity.MemberRole;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//
//@Disabled
//@SpringBootTest
//public class MemberRepositoryTest {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    @Transactional
//    void 더미_회원_데이터_생성() {
//
//        Member member1 = Member.builder()
//                               .id(1L)
//                               .name("김싸피")
//                               .email("kimssafy@ssafy.com")
//                               .pw(BCrypt.hashpw("1234567!", BCrypt.gensalt()))
//                               .token("")
//                               .gameMoney(0L)
//                               .consecutiveDays(0)
//                               .totalDays(0)
//                               .isBriefingAllowed(true)
//                               .isBroadcastAllowed(true)
//                               .role(MemberRole.USER)
//                               .build();
//
//        memberRepository.save(member1);
//
//        // 저장 확인
//        assertThat(memberRepository.findById(member1.getId()).isPresent());
//    }
//}
