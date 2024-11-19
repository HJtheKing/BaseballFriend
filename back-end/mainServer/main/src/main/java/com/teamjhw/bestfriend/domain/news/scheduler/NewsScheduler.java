package com.teamjhw.bestfriend.domain.news.scheduler;

import com.teamjhw.bestfriend.domain.match.repository.TeamRepository;
import com.teamjhw.bestfriend.domain.news.dto.CreateNewsDTO;
import com.teamjhw.bestfriend.domain.news.dto.SummarizeNewsDTO;
import com.teamjhw.bestfriend.domain.news.repository.NewsRepository;
import com.teamjhw.bestfriend.domain.news.service.NaverNewsService;
import com.teamjhw.bestfriend.entity.Team;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.NewsException;
import com.teamjhw.bestfriend.global.utils.GPTUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class NewsScheduler {

    private final NaverNewsService naverNewsService;
    private final GPTUtil gptUtil;

    private final NewsRepository newsRepository;
    private final TeamRepository teamRepository;

    private final String[] TEAM_NAME = {"KIA 타이거즈", "삼성 라이온즈",
            "LG 트윈스", "두산 베어스",
            "KT 위즈", "SSG 랜더스",
            "롯데 자이언츠", "한화 이글스",
            "NC 다이노스", "키움 히어로즈"};

    /*
     * 오늘의 뉴스 생성
     */
    @Scheduled(cron = "0 50 3 * * *", zone = "Asia/Seoul")
    public void createNews() {

        for (String query : TEAM_NAME) {
            log.info("==== {}의 뉴스 생성 중 ====", query);
            Team team = findTeamByName(query.split(" ")[0]);

            // 1. Naver API 요청, 뉴스 수집
            String originalNews = naverNewsService.requestBody(query);

            // 2. 이미지 생성 요청 1분 당 5개 제한 -> 시간 설정
            try {
                TimeUnit.MINUTES.sleep(1L);
            } catch (InterruptedException e) {
                log.info("==== 시간은 음수일 수 없습니다. ====");
            }

            // 2. OpenAI API 요청
            List<SummarizeNewsDTO.Response> todayNews = gptUtil.requestNewsTextBody(query, originalNews);

            // 3. 뉴스 저장
            for (SummarizeNewsDTO.Response news : todayNews) {
                newsRepository.save(CreateNewsDTO.Request.toEntity(news, team));
            }
        }

        log.info("{} : 뉴스 생성이 완료되었습니다.", LocalDate.now());
    }

    public Team findTeamByName(String name) {
        return teamRepository.findByTeamName(name)
                             .orElseThrow(() -> new NewsException(ErrorCode.TEAM_NOT_FOUND));
    }

}
