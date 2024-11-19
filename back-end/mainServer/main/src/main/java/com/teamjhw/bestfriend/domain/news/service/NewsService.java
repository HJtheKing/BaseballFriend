package com.teamjhw.bestfriend.domain.news.service;

import com.teamjhw.bestfriend.domain.match.repository.TeamRepository;
import com.teamjhw.bestfriend.domain.news.dto.NewsDTO;
import com.teamjhw.bestfriend.domain.news.dto.NewsDTO.Response;
import com.teamjhw.bestfriend.domain.news.repository.NewsRepository;
import com.teamjhw.bestfriend.entity.News;
import com.teamjhw.bestfriend.entity.Team;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import com.teamjhw.bestfriend.global.exception.exceptionType.NewsException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final TeamRepository teamRepository;

    /*
     * 뉴스 조회
     */
    public List<NewsDTO.Response> getNews(LocalDate date, String teamName) {
        List<News> news;

        if(teamName.isBlank()) {
            news = newsRepository.findAllByCreatedAt(date);
        } else {
            Team team = findTeamByName(teamName);
            news = newsRepository.findAllByTeamAndCreatedAt(team, date);
        }
        
        // 뉴스 없을 경우 예외 처리
        if (news.isEmpty())
            throw new NewsException(ErrorCode.NEWS_NOT_FOUND);

        return news.stream().map(Response::of).toList();
    }

    private Team findTeamByName(String teamName) {
        return teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new MemberException(ErrorCode.TEAM_NOT_FOUND));
    }
}