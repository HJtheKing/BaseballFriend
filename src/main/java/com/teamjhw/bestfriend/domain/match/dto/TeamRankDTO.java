package com.teamjhw.bestfriend.domain.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamjhw.bestfriend.entity.TeamRank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TeamRankDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {

        private int teamRank;
        private String teamName;
        private int winCount;
        private int lossCount;
        private int drawCount;
        private double odds;
        private String last10GamesResults;

        public static TeamRankDTO.Response of(TeamRank teamRank) {
            return TeamRankDTO.Response.builder()
                           .teamRank(teamRank.getTeamRank())
                           .teamName(teamRank.getTeamName())
                           .winCount(teamRank.getWinCount())
                           .lossCount(teamRank.getLossCount())
                           .drawCount(teamRank.getDrawCount())
                           .odds(teamRank.getOdds())
                           .last10GamesResults(teamRank.getLast10GamesResults())
                           .build();
        }

        public static TeamRank toEntity(TeamRankDTO.Response response) {
            LocalDateTime now = LocalDateTime.now();

            return TeamRank.builder()
                    .teamRank(response.getTeamRank())
                    .createdAt(now)
                    .teamName(response.getTeamName())
                    .winCount(response.getWinCount())
                    .lossCount(response.getLossCount())
                    .drawCount(response.getDrawCount())
                    .odds(response.getOdds())
                    .last10GamesResults(response.getLast10GamesResults())
                    .build();
        }
    }
}
