package com.teamjhw.bestfriend.domain.news.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SummarizeNewsDTO {

    @Getter
    @Builder
    public static class Response {

        String title;
        String content;

        @Setter
        String imageUrl;

        public static Response of(String title, String content) {
            return Response.builder()
                           .title(title)
                           .content(content)
                           .imageUrl(null)
                           .build();
        }
    }
}
