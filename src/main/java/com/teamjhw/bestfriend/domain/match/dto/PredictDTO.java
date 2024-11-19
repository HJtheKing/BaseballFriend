package com.teamjhw.bestfriend.domain.match.dto;

import com.teamjhw.bestfriend.entity.MatchInfo;
import com.teamjhw.bestfriend.entity.MatchPrediction;
import com.teamjhw.bestfriend.entity.MatchResult;
import com.teamjhw.bestfriend.entity.Member;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

public class PredictDTO {

    @Getter
    @Builder
    public static class Request {

        private Long matchInfoId;
        private int memberPrediction;
        private Long amount;

        public static MatchPrediction toEntity(Request request, Member member, MatchInfo matchInfo) {
            return MatchPrediction.builder()
                                  .member(member)
                                  .matchInfo(matchInfo)
                                  .amount(request.getAmount())
                                  .isSuccessed(null)
                                  .memberPredict(MatchResult.fromKey(request.getMemberPrediction()))
                                  .createdAt(LocalDate.now())
                                  .build();
        }
    }

    @Getter
    @Builder
    public static class Response {

        private Long matchPredictionId;
        private Long matchInfoId;
        private int memberPrediction;
        private Long amount;

        public static PredictDTO.Response of(MatchPrediction matchPrediction) {
            return Response.builder()
                           .matchPredictionId(matchPrediction.getId())
                           .matchInfoId(matchPrediction.getMatchInfo().getId())
                           .memberPrediction(matchPrediction.getMemberPredict().getKey())
                           .amount(matchPrediction.getAmount())
                           .build();
        }
    }
}
