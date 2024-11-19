package com.teamjhw.bestfriend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team_rank", uniqueConstraints = {@UniqueConstraint(columnNames = "team_name")})
public class TeamRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_rank_id")
    private Long id;

    private LocalDateTime createdAt;

    private int teamRank;

    private String teamName;

    private int winCount;

    private int lossCount;

    private int drawCount;

    private double odds;

    private String last10GamesResults;

}
