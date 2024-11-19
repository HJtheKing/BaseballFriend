package com.teamjhw.bestfriend.domain.match.repository;

import com.teamjhw.bestfriend.entity.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * team 이름으로 team id를 찾음
     */
    Optional<Team> findByTeamName(String teamName);
}
