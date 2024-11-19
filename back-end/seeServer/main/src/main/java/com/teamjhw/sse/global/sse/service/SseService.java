package com.teamjhw.sse.global.sse.service;

import com.teamjhw.sse.global.sse.UserSubInfo;
import com.teamjhw.sse.global.sse.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {
    private final Map<Long, UserSubInfo> emitters = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final RedisTemplate<String, Object> redisTemplate;
    private final SseRepository sseRepository;

    public SseEmitter subscribe(Long userid, String topic) {
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        emitters.put(userid, new UserSubInfo(topic, emitter));

        emitter.onCompletion(() -> emitters.remove(userid));
        emitter.onTimeout(() -> emitters.remove(userid));
        emitter.onError((e) -> emitters.remove(userid));

        return emitter;
    }

    /**
     * SSE 연결. 해당 유저에 대한 SseEmitter 객체를 생성하여 repository에 저장하고, 완료/시간초과/에러 시에 해당 객체를 제거합니다.
     *
     * @param userId
     * @param topic : 팀 id
     * @return : 유저에 대한 SseEmitter 객체
     */
    public SseEmitter subscribeV2(Long userId, Long topic) {
        SseEmitter emitter = new SseEmitter(365 * 24 * 60 * 1000L);
        sseRepository.save(topic, userId, emitter);
        try {
            emitter.send(SseEmitter.event().data("connected").name("sse"));
        } catch (IOException e) {
            emitter.complete();
        }
        log.info("user {} subscribed to topic: {}", userId, topic);

        emitter.onCompletion(() -> sseRepository.remove(topic, userId));
        emitter.onTimeout(emitter::complete);
        emitter.onError((e) -> emitter.complete());

        return emitter;
    }

    /**
     * SSE 연결을 수동으로 해제하기 위한 메서드
     *
     * @param userid
     * @param topic : 팀 id
     */
    public void unsubscribe(Long userid, Long topic) {
        sseRepository.remove(userid, topic);
    }


    @Transactional
    public void sendNotification(String topic, String message) {
        if (!emitters.isEmpty()) {
            emitters.forEach((userid, info) -> {
                if (info.getTopic().equals(topic)) {
                    executor.execute(() -> {
                        try {
                            info.getSseEmitter().send(SseEmitter.event().name("notification").data(message));
                            log.info("send notification to user {} : {}", userid, message);
                        } catch (Exception e) {
                            emitters.remove(userid);
                            e.printStackTrace();
                        }
                    });
                }
            });
        } else {
            log.info("emitter is empty");
        }
    }

    /**
     * 팀에 대한 데이터(알림 or 문자중계)를 SSE로 연결된 클라이언트로 전송하는 로직
     *
     * @param topic : 토픽
     * @param message : 메세지
     */
    public void sendNotificationV2(String topic, String message, String eventName) {
        Long tp;
        try{ tp = Long.valueOf(topic);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return;
        }

        if (sseRepository.contains(tp)) {
            Map<Long, SseEmitter> map = sseRepository.get(tp);
            map.values().parallelStream().forEach(emitter -> {
                try{
                    emitter.send(SseEmitter.event().name(eventName).data(message));
                    log.info("send to event: {}, emitter: {}", eventName, emitter);
                }
                catch (IOException e){
                    log.warn("Client not connected");
                    emitter.complete();
                }
            });
            log.info("send complete");
        }
        else log.info("repository not contain team topic: {}", tp);
    }

    public String findLatestText(Long teamId){
        Object result = redisTemplate.opsForValue().get(teamId + "");
        return result == null ? "" : result.toString();
    }

    public SseEmitter createErrorEmitter() {
        SseEmitter emitter = new SseEmitter();
        try {
            emitter.send(SseEmitter.event().data("Member_NOT_FOUND").name("error"));
            emitter.complete();
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }
}