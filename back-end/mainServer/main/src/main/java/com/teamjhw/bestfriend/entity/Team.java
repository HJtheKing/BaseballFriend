package com.teamjhw.bestfriend.entity;

import jakarta.persistence.*;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    private String location;

    /*
     * 연관관계
     */
    @Builder.Default
    @OneToMany(mappedBy = "teamHome", cascade = CascadeType.REMOVE)
    private List<MatchInfo> homeMatchInfos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "teamAway", cascade = CascadeType.REMOVE)
    private List<MatchInfo> awayMatchInfos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<News> news = new ArrayList<>();
}
