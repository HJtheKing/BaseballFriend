package com.teamjhw.sse.global.sse.scheduler;

import com.teamjhw.sse.global.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SseScheduler {
    private final SseService sseService;

    /**
     * 오전 9시 30분마다 SSE 연결된 사용자에게 뉴스 브리핑 알림 전송
     *
     */
//    @Scheduled(cron = "0 30 10 * * *", zone = "Asia/Seoul")
//    public void schedule() {
//        for(int i = 1; i <= 10; i++){
//            log.info("brief send to topic {}", i);
//            sseService.sendNotificationV2(i + "", "morning-brief", "brief");
//        }
//    }

}
