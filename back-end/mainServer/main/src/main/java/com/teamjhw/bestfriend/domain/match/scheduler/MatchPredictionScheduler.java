package com.teamjhw.bestfriend.domain.match.scheduler;

import com.teamjhw.bestfriend.domain.match.repository.MatchInfoRepository;
import com.teamjhw.bestfriend.domain.match.repository.MatchPredictionRepository;
import com.teamjhw.bestfriend.domain.member.repository.MemberRepository;
import com.teamjhw.bestfriend.entity.Member;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class MatchPredictionScheduler {

    private final MatchPredictionRepository matchPredictionRepository;
    private final MatchInfoRepository matchInfoRepository;
    private final MemberRepository memberRepository;

    /**
     * 승부 예측 미참여 유저의 consecutiveDays 초기화
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void resetConsecutiveDays() {
        // 1. 어제 경기가 있다면 진행
        if (!matchInfoRepository.existsMatchInfoByYesterday(LocalDate.now().minusDays(1L))) {
            return;
        }

        // 2. 어제 승부 예측에 참여하지 않은 멤버 조회
        List<Member> members = matchPredictionRepository.findMembersWithoutPredictionOnYesterday(LocalDate.now().minusDays(1L));

        // 3. 어제 승부 예측 안한 멤버 consecutive-days 0으로 초기화
        for(Member member : members){
            System.out.println(member.getId());
            member.resetConsecutiveDays();
            memberRepository.save(member);
        }
    }
}
