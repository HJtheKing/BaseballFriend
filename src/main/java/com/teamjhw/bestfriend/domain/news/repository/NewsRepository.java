package com.teamjhw.bestfriend.domain.news.repository;

import com.teamjhw.bestfriend.entity.News;
import com.teamjhw.bestfriend.entity.Team;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByTeamAndCreatedAt(Team team, LocalDate createAt);

    List<News> findAllByCreatedAt(LocalDate date);
}
