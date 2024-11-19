package com.teamjhw.bestfriend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "match_info", uniqueConstraints = {@UniqueConstraint(columnNames = "location, match_date")})
public class MatchInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_home_id")
    private Team teamHome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_away_id")
    private Team teamAway;

    private String location;
    @Builder.Default
    private int homeScore = 0;
    @Builder.Default
    private int awayScore = 0;

    private LocalDateTime matchDate;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private MatchResult matchResult = MatchResult.NOT_FINISHED;

    private Long parentMatchInfoId;

    /*
     * 연관관계
     */
    @Builder.Default
    @OneToMany(mappedBy = "matchInfo", cascade = CascadeType.REMOVE)
    List<MatchPrediction> matchPredictions = new ArrayList<>();

    /**
     * 경기 결과 update
     */
    public void updateResult(int homeScore, int awayScore, int matchResult){
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchResult = MatchResult.fromKey(matchResult);
    }

    /**
     * 경기 취소 처리
     * */
    public void cancelMatch(){
        this.matchResult = MatchResult.CANCEL;
    }

    /**
     * 경기 정보 수정(시연)
     */
    public void updateMatchDate(LocalDateTime matchDate){
        this.matchDate = matchDate;
    }
}