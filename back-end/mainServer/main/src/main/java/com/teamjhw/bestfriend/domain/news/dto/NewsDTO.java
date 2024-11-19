package com.teamjhw.bestfriend.domain.news.dto;

import com.teamjhw.bestfriend.entity.News;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

public class NewsDTO {

    @Getter
    @Builder
    public static class Response {

        private Long newsId;
        private LocalDate createAt;
        private String title;
        private String content;
        private String newsImage;

        public static Response of(News news) {
            return Response.builder()
                           .newsId(news.getId())
                           .createAt(news.getCreatedAt())
                           .title(news.getTitle())
                           .content(news.getContent())
                           .newsImage(news.getImageUrl())
                           .build();
        }
    }
}
