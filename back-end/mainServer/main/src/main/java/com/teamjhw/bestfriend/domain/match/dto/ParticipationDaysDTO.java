package com.teamjhw.bestfriend.domain.match.dto;

import lombok.Builder;
import lombok.Getter;

public class ParticipationDaysDTO {

    @Getter
    @Builder
    public static class Response {

        private int consecutiveDays;
        private int totalDays;

        public static ParticipationDaysDTO.Response of(int consecutiveDays, int totalDays) {
            return Response.builder()
                           .consecutiveDays(consecutiveDays)
                           .totalDays(totalDays)
                           .build();
        }
    }

}
