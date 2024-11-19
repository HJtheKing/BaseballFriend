package com.teamjhw.bestfriend.domain.match.dto;

import lombok.Builder;
import lombok.Getter;

public class MatchFinishDTO {

    @Getter
    @Builder
    public static class Request {

        private int matchResult;
        private int awayScore;
        private int homeScore;
        private String matchTime;
        private String location;
    }
}
