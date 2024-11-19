package com.teamjhw.bestfriend.domain.match.dto;

import lombok.Builder;
import lombok.Getter;

public class UpcomingMatchInfoDTO {
    @Getter
    @Builder
    public static class Request {
        private String matchTime;
        private String location;

        public static Request of(String matchTime, String location) {
            return Request.builder()
                    .matchTime(matchTime)
                    .location(location)
                    .build();
        }
    }
}
