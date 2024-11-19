package com.teamjhw.bestfriend.domain.match.dto;

import lombok.Builder;
import lombok.Getter;

public class HitGameDTO {

    @Getter
    public static class Request {

        private int score;
    }

    @Getter
    @Builder
    public static class Response {

        private Long reward;
        private Long gameMoney;

        public static HitGameDTO.Response of(Long reward, Long gameMoney) {
            return Response.builder()
                                 .reward(reward)
                                 .gameMoney(gameMoney)
                                 .build();
        }
    }

}
