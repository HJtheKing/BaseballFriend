package com.teamjhw.bestfriend.domain.news.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

public class SearchNewsDTO {

    @Getter
    @Builder
    public static class Response {

        boolean existsTwoDaysAgo;
        Map<String, String> news;

        public static Response of(boolean existsTwoDaysAgo, Map<String, String> news) {
            return Response.builder()
                           .existsTwoDaysAgo(existsTwoDaysAgo)
                           .news(news)
                           .build();
        }
    }

}
