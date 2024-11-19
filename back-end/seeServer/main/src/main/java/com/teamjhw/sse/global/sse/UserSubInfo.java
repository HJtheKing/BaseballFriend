package com.teamjhw.sse.global.sse;


import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class UserSubInfo {
    private String topic;
    private SseEmitter sseEmitter;

    public UserSubInfo(String topic, SseEmitter sseEmitter) {
        this.topic = topic;
        this.sseEmitter = sseEmitter;
    }
}
