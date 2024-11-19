package com.teamjhw.bestfriend.domain.match.dto;

import com.teamjhw.bestfriend.domain.match.util.MatchDateUtil;
import com.teamjhw.bestfriend.entity.MatchInfo;
import com.teamjhw.bestfriend.entity.MatchPrediction;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class MatchInfoDTO {

    @Getter
    @Builder
    public static class Response {

        private String matchDate;
        private List<Common> matchInfos;

        public static Response of(LocalDate matchDate, List<Common> matchInfos) {
            return Response.builder()
                           .matchDate(MatchDateUtil.toStringDate(matchDate))
                           .matchInfos(matchInfos)
                           .build();
        }
    }

    /*
     * 공통 DTO
     */
    @Getter
    @Builder
    public static class Common {

        private Long matchInfoId;
        private String matchTime;
        private String homeTeamName;
        private String awayTeamName;
        private Integer matchResult;
        private Integer homeScore;
        private Integer awayScore;
        private Boolean isBeforeMatch;

        private Long amount;
        private Boolean isSuccessed;
        private Integer memberPrediction;

        public static MatchInfoDTO.Common of(MatchInfo matchInfo) {
            return Common.builder()
                         .matchInfoId(matchInfo.getId())
                         .matchTime(MatchDateUtil.toStringTime(matchInfo.getMatchDate()))
                         .homeTeamName(matchInfo.getTeamHome().getTeamName())
                         .awayTeamName(matchInfo.getTeamAway().getTeamName())
                         .matchResult(matchInfo.getMatchResult().getKey())
                         .homeScore(matchInfo.getHomeScore())
                         .awayScore(matchInfo.getAwayScore())
                         .isBeforeMatch(LocalDateTime.now().isBefore(matchInfo.getMatchDate()))

                         .amount(null)
                         .isSuccessed(null)
                         .memberPrediction(null)
                         .build();
        }

        public static MatchInfoDTO.Common of(MatchInfo matchInfo, MatchPrediction matchPrediction) {
            return Common.builder()
                         .matchInfoId(matchInfo.getId())
                         .matchTime(MatchDateUtil.toStringTime(matchInfo.getMatchDate()))
                         .homeTeamName(matchInfo.getTeamHome().getTeamName())
                         .awayTeamName(matchInfo.getTeamAway().getTeamName())
                         .matchResult(matchInfo.getMatchResult().getKey())
                         .homeScore(matchInfo.getHomeScore())
                         .awayScore(matchInfo.getAwayScore())
                         .isBeforeMatch(LocalDateTime.now().isBefore(matchInfo.getMatchDate()))

                         .amount(matchPrediction.getAmount())
                         .isSuccessed(matchPrediction.getIsSuccessed())
                         .memberPrediction(matchPrediction.getMemberPredict().getKey())
                         .build();
        }
    }
}
