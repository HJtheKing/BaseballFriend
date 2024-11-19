package com.teamjhw.bestfriend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String pw;

    private String token;

    private Long gameMoney;

    private int consecutiveDays;

    private int totalDays;

    private Long selectedCharacterSerialNumber;

    private Long wornBackgroundSerialNumber;

    private Long wornHeadItemSerialNumber;

    private Long wornArmItemSerialNumber;

    private Long wornBodyItemSerialNumber;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_team_id")
    private Team team;

    private Boolean isBriefingAllowed;

    private Boolean isBroadcastAllowed;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public void setName(String name){
        if(name != null) this.name = name;
    }

    public void setIsBriefingAllowed(Boolean isBriefingAllowed){
        if(isBriefingAllowed != null) this.isBriefingAllowed = isBriefingAllowed;
    }

    public void setIsBroadcastAllowed(Boolean isBroadcastAllowed){
        if(isBroadcastAllowed != null) this.isBroadcastAllowed = isBroadcastAllowed;
    }

    /*
     * 연관관계
     */
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    List<MatchPrediction> matchPredictions = new ArrayList<>();

    /*
     * 비즈니스 로직
     */
    public void changeGameMoney(Long reward) {
        this.gameMoney += reward;
        if(gameMoney < 0) this.gameMoney = 0L;
    }

    public void addDays() {
        this.consecutiveDays++;
        this.totalDays++;
    }

    public void resetConsecutiveDays(){
        this.consecutiveDays = 0;
    }

    public void updateCharacterItemAndBackgroundIds(
            Long selectedCharacterSerialNumber,
            Long wornHeadItemSerialNumber,
            Long wornBodyItemSerialNumber,
            Long wornArmItemSerialNumber,
            Long wornBackgroundSerialNumber
    ) {
        this.selectedCharacterSerialNumber = selectedCharacterSerialNumber;
        this.wornHeadItemSerialNumber = wornHeadItemSerialNumber;
        this.wornBodyItemSerialNumber = wornBodyItemSerialNumber;
        this.wornArmItemSerialNumber = wornArmItemSerialNumber;
        this.wornBackgroundSerialNumber = wornBackgroundSerialNumber;
    }
    
}
