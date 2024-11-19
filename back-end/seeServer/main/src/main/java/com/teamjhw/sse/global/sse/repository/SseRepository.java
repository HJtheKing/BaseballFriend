package com.teamjhw.sse.global.sse.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SseRepository {

    private final Map<Long, Map<Long, SseEmitter>> emittersV2 = new ConcurrentHashMap<>();

    public void save(Long topic, Long userId, SseEmitter emitter) {
        if (!emittersV2.containsKey(topic)) {
            emittersV2.put(topic, new ConcurrentHashMap<>());
            emittersV2.get(topic).put(userId, emitter);

        } else emittersV2.get(topic).put(userId, emitter);
        for(Long id : emittersV2.keySet()){
            log.info("team: {} has Key", id);
        }
    }

    public void remove(Long topic, Long userId) {
        log.info("removing user {} from team topic: {}", userId, topic);
        if(emittersV2.containsKey(topic)) {
            emittersV2.get(topic).remove(userId);
            if (emittersV2.get(topic).isEmpty()) {
                emittersV2.remove(topic);
                log.info("removed team topic: {}", topic);
            }
            log.info("removed user {} from team topic: {}", userId, topic);
        }

    }

    public Map<Long, SseEmitter> get(Long topic) {
        return emittersV2.get(topic);
    }

    public boolean contains(Long topic) {
        return emittersV2.containsKey(topic);
    }
}
