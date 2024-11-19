package com.teamjhw.bestfriend.domain.news.controller;

import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.domain.news.dto.NewsDTO;
import com.teamjhw.bestfriend.domain.news.dto.NewsDTO.Response;
import com.teamjhw.bestfriend.domain.news.service.NewsService;
import com.teamjhw.bestfriend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bf/news")
@Tag(name = "News", description = "뉴스 관련 API")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    @Operation(summary = "뉴스 조회", description = "날짜, 팀별 뉴스 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "뉴스 조회 성공",
                         content = @Content(schema = @Schema(implementation = NewsDTO.Response.class))),
            @ApiResponse(responseCode = "404", description = "뉴스를 찾을 수 없습니다.",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<?> getNews(@RequestParam LocalDate date, @RequestParam String team) {
        List<Response> responses = newsService.getNews(date, team);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}
