package com.teamjhw.bestfriend.domain.match.dto;

import lombok.Builder;
import lombok.Getter;

public class GameMoneyDTO {

    @Getter
    @Builder
    public static class Response {

        private Long gameMoney;

        public static GameMoneyDTO.Response of(Long gameMoney) {
            return Response.builder()
                                .gameMoney(gameMoney)
                                .build();
        }
    }

}
