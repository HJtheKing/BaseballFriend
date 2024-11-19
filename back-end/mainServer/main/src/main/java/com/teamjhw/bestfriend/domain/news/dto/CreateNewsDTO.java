package com.teamjhw.bestfriend.domain.news.dto;

import com.teamjhw.bestfriend.entity.News;
import com.teamjhw.bestfriend.entity.Team;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

public class CreateNewsDTO {

    @Getter
    @Builder
    public static class Request {

        public static News toEntity(SummarizeNewsDTO.Response response, Team team) {
            return News.builder()
                       .team(team)
                       .createdAt(LocalDate.now())
                       .title(response.getTitle())
                       .content(response.getContent())
                       .imageUrl(response.getImageUrl())
                       .build();
        }
    }
}
