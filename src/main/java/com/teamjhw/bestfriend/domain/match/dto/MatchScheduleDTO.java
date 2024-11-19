package com.teamjhw.bestfriend.domain.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamjhw.bestfriend.domain.match.util.MatchDateUtil;
import com.teamjhw.bestfriend.entity.MatchInfo;
import com.teamjhw.bestfriend.entity.MatchResult;
import com.teamjhw.bestfriend.entity.Team;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class MatchScheduleDTO {

    @Getter
    @Builder
    public static class Responses {

        private String matchDate;
        private List<Response> matchList;

        public static Responses of(String matchDate, List<Response> matchList) {
            return Responses.builder()
                            .matchDate(matchDate)
                            .matchList(matchList)
                            .build();
        }
    }

    @Getter
    @Builder
    public static class Response {

        private Long matchInfoId;
        private String matchTime;
        private String teamHomeName;
        private String teamAwayName;
        private int matchResult;
        private int homeScore;
        private int awayScore;
        private String location;

        public static MatchScheduleDTO.Response of(MatchInfo matchInfo) {
            return Response.builder()
                           .matchInfoId(matchInfo.getId())
                           .matchTime(MatchDateUtil.toStringTime(matchInfo.getMatchDate()))
                           .teamHomeName(matchInfo.getTeamHome().getTeamName())
                           .teamAwayName(matchInfo.getTeamAway().getTeamName())
                           .matchResult(matchInfo.getMatchResult().getKey())
                           .homeScore(matchInfo.getHomeScore())
                           .awayScore(matchInfo.getAwayScore())
                           .location(matchInfo.getLocation())
                           .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlaskResponse {

        private String location;
        private String matchDate;
        private String matchTime;
        private String teamHomeName;
        private String teamAwayName;
        private int matchResult;
        private int homeScore;
        private int awayScore;

        public static MatchInfo toEntity(FlaskResponse response, Team teamHome, Team teamAway,
                                         LocalDateTime matchDate) {
            return MatchInfo.builder()
                            .teamHome(teamHome)
                            .teamAway(teamAway)
                            .location(response.getLocation())
                            .homeScore(response.getHomeScore())
                            .awayScore(response.getAwayScore())
                            .matchDate(matchDate)
                            .matchResult(MatchResult.fromKey(response.getMatchResult()))
                            .build();
        }
    }
}
