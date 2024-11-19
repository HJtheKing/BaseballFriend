package com.teamjhw.sse.global.kafka.consumer;

import com.teamjhw.sse.global.sse.service.SseService;
import com.teamjhw.sse.global.utils.StringSearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

    private final SseService sseService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 경기 중인 팀에 대한 '문자 중계' 데이터를 SSE로 연결된 클라이언트로 전송하는 로직
     *
     * @param record : kafka를 통해서 오는 데이터를 담는 객체입니다. topic, value 등 전송 받은 메세지의 정보를 알 수 있음
     */
    @KafkaListener(
            topics = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            groupId = "bf-text"
    )
    public void consumeTextMessage(ConsumerRecord<String, String> record) {
        log.info("topic : {}, text : {}", record.topic(), record.value());
        if(isGameOver(record.value())){
            redisTemplate.delete(record.topic());
        } else redisTemplate.opsForValue().set(record.topic(), record.value(), Duration.ofDays(7));

        try {
            sseService.sendNotificationV2(record.topic(), record.value(), "text");
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * 문자중계 데이터에 "경기종료" 있는지 확인하는 문자열 탐색
     *
     * @param value : 탐색하고자 하는 문자열
     */
    public boolean isGameOver(String value){
        StringSearchUtil bm = new StringSearchUtil();
        return bm.search(value, "\\uacbd\\uae30\\uc885\\ub8cc");
    }


    /**
     * 경기 중인 팀에 대한 '알림' 데이터를 SSE로 연결된 클라이언트로 전송하는 로직
     *
     * @param record : kafka를 통해서 오는 데이터를 담는 객체입니다. topic, value 등 전송 받은 메세지의 정보를 알 수 있음
     */
    @KafkaListener(
            topics = {"alert.1", "alert.2", "alert.3", "alert.4", "alert.5", "alert.6", "alert.7", "alert.8", "alert.9", "alert.10"},
            groupId = "bf-alert"
    )
    public void consumeAlertMessage(ConsumerRecord<String, String> record) {
        log.info("topic : {}, alert : {}", record.topic(), record.value());
        try {
            String topic = record.topic().split("\\.")[1];
            log.info("topic : {}", topic);
            sseService.sendNotificationV2(topic, record.value(), "alert");
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
