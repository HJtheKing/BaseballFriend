package com.teamjhw.bestfriend.domain.match.dto;

import com.teamjhw.bestfriend.entity.MatchInfo;
import com.teamjhw.bestfriend.entity.MatchResult;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class SuspendedMatchInfoDTO {

    @Getter
    @Builder
    public static class Request {

        private int matchResult;
        private int awayScore;
        private int homeScore;
        private String oldMatchTime;
        private String oldLocation;
        private LocalDateTime newMatchDate;
        private String newLocation;

        public static MatchInfo toEntity(MatchInfo matchInfo, Request request) {
            return MatchInfo.builder()
                            .matchDate(request.getNewMatchDate())
                            .location(request.getNewLocation())
                            .teamHome(matchInfo.getTeamHome())
                            .teamAway(matchInfo.getTeamAway())
                            .matchResult(MatchResult.NOT_FINISHED)
                            .homeScore(request.getHomeScore())
                            .awayScore(request.getAwayScore())
                            .parentMatchInfoId(matchInfo.getId())
                            .build();
        }
    }

    @Getter
    @Builder
    public static class Response {

        private Long matchInfoId;
        private LocalDateTime matchDate;
        private String homeTeamName;
        private String awayTeamName;
        private int matchResult;
        private int homeScore;
        private int awayScore;
        private Long parentMatchInfoId;

        public static Response of(MatchInfo matchInfo) {
            return Response.builder()
                           .matchInfoId(matchInfo.getId())
                           .matchDate(matchInfo.getMatchDate())
                           .homeTeamName(matchInfo.getTeamHome().getTeamName())
                           .awayTeamName(matchInfo.getTeamAway().getTeamName())
                           .matchResult(matchInfo.getMatchResult().getKey())
                           .homeScore(matchInfo.getHomeScore())
                           .awayScore(matchInfo.getAwayScore())
                           .parentMatchInfoId(matchInfo.getParentMatchInfoId())
                           .build();
        }
    }
}