package com.teamjhw.bestfriend.domain.match.service;

import com.teamjhw.bestfriend.domain.match.dto.GameMoneyDTO;
import com.teamjhw.bestfriend.domain.match.dto.HitGameDTO;
import com.teamjhw.bestfriend.domain.match.dto.MatchInfoDTO;
import com.teamjhw.bestfriend.domain.match.dto.ParticipationDaysDTO;
import com.teamjhw.bestfriend.domain.match.dto.PredictDTO;
import com.teamjhw.bestfriend.domain.match.repository.MatchInfoRepository;
import com.teamjhw.bestfriend.domain.match.repository.MatchPredictionRepository;
import com.teamjhw.bestfriend.domain.match.util.MatchDateUtil;
import com.teamjhw.bestfriend.domain.member.repository.MemberRepository;
import com.teamjhw.bestfriend.entity.MatchInfo;
import com.teamjhw.bestfriend.entity.MatchPrediction;
import com.teamjhw.bestfriend.entity.MatchResult;
import com.teamjhw.bestfriend.entity.Member;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.GameException;
import com.teamjhw.bestfriend.global.exception.exceptionType.MatchException;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final MemberRepository memberRepository;
    private final MatchInfoRepository matchInfoRepository;
    private final MatchPredictionRepository matchPredictionRepository;
    private final Long MONEY_PER_SCORE = 10L;

    /**
     * 게임머니 잔액 조회
     */
    public GameMoneyDTO.Response getGameMoney(Long memberId) {
        Member member = findMemberById(memberId);
        return GameMoneyDTO.Response.of(member.getGameMoney());
    }

    /**
     * 공 맞추기 게임 완료
     */
    public HitGameDTO.Response finishHitGame(Long memberId, HitGameDTO.Request request) {
        Member member = findMemberById(memberId);

        // 1. 게임 결과 reward
        Long reward = request.getScore() * MONEY_PER_SCORE;

        // 2. gameMoney 증감
        member.changeGameMoney(reward);
        Member savedMember = memberRepository.save(member);

        return HitGameDTO.Response.of(reward, savedMember.getGameMoney());
    }

    /**
     * 승부 예측 참여
     */
    public PredictDTO.Response createMatchPrediction(Long memberId, PredictDTO.Request request) {
        Member member = findMemberById(memberId);
        MatchInfo matchInfo = findMatchInfoById(request.getMatchInfoId());
        // 1. 예외처리
        // 1-1. 게임 머니 잔액 확인
        if (member.getGameMoney() < request.getAmount()) {
            throw new GameException(ErrorCode.INSUFFICIENT_GAME_MONEY);
        }
        // 1-2. 이미 참여 경기 예외 처리
        existsPredictionByMemberIdAndMatchInfoId(member.getId(), matchInfo.getId());
        // 1-3. 잘못된 예측 결과 예외 처리
        if (request.getMemberPrediction() < 1 || request.getMemberPrediction() > 3) {
            throw new GameException(ErrorCode.UNDEFINED_MATCH_RESULT);
        }
        // 1-4. 참여 가능 시간 예외 처리
        if (!MatchDateUtil.isPossibleMatchPrediction(matchInfo.getMatchDate())) {
            throw new GameException(ErrorCode.FORBIDDEN_MATCH_PREDICTION);
        }

        // 2. 게임 머니 차감
        member.changeGameMoney(request.getAmount() * -1);

        // 3. 연속 참여 일 수 & 전체 참여 일 수 증가
        if (!matchPredictionRepository.existsPredictionByMemberAndToday(member, LocalDate.now())) {
            member.addDays();

            // TODO : 리워드 금액
            // 3-1. 연속 3일 참여 리워드
            if (member.getConsecutiveDays() % 3 == 0) {
                member.changeGameMoney(33L);
            }

            // 3-2. 총 100일 참여 리워드
            if (member.getTotalDays() == 100) {
                member.changeGameMoney(100L);
            }
        }

        Member savedMember = memberRepository.save(member);
        MatchPrediction matchPrediction = PredictDTO.Request.toEntity(request, savedMember, matchInfo);
        return PredictDTO.Response.of(matchPredictionRepository.save(matchPrediction));
    }

    /**
     * 승부 예측 정보 조회 (오늘 승부 예측 경기 정보 + 유저 예측)
     */
    public MatchInfoDTO.Response getMatchInfoList(Long memberId, LocalDate date) {
        // 1. 오늘보다 이후 경기 조회 요청 예외 처리
        if(MatchDateUtil.isAfterToday(date)){
            throw new GameException(ErrorCode.FORBIDDEN_MATCH_PREDICTION);
        }

        Member member = findMemberById(memberId);
        List<Object[]> results = matchInfoRepository.findMatchInfoAndPredictions(member, date);

        List<MatchInfoDTO.Common> infoAndPredictions = results.stream()
                                                              .map(list -> {
                                                                  MatchInfo matchInfo = (MatchInfo) list[0];
                                                                  MatchPrediction matchPrediction = (MatchPrediction) list[1];

                                                                  return matchPrediction != null
                                                                         ? MatchInfoDTO.Common.of(matchInfo,
                                                                                                  matchPrediction)
                                                                         : MatchInfoDTO.Common.of(matchInfo);
                                                              })
                                                              .toList();

        return MatchInfoDTO.Response.of(date, infoAndPredictions);
    }

    /**
     * 승부 예측 연속 참여 일수 조회
     */
    public ParticipationDaysDTO.Response getParticipationDays(Long memberId) {
        Member member = findMemberById(memberId);
        return ParticipationDaysDTO.Response.of(member.getConsecutiveDays(), member.getTotalDays());
    }

    /**
     * 승부 예측 결과 반영 메서드
     * 
     * 들어오는 Result 값 : 1, 2, 3, 5(중단)
     */
    public void updateMatchPredictionByResult(Long matchInfoId) {
        MatchInfo matchInfo = findMatchInfoById(matchInfoId);

        // 1. 중단 경기 예외 처리
        if(matchInfo.getMatchResult() == MatchResult.STOP) return;

        // 2. 재개된 경기인지 확인 후, 적합한 승부 예측들 조회
        MatchInfo parentMatchInfo;
        List<MatchPrediction> matchPredictions;
        if(matchInfo.getParentMatchInfoId() != null) {
            parentMatchInfo = findMatchInfoById(matchInfo.getParentMatchInfoId());
            matchPredictions = parentMatchInfo.getMatchPredictions();
        } else {
            matchPredictions = matchInfo.getMatchPredictions();
        }
        
        // 3. 결과에 따른 게임 머니 변경
        List<Member> members = new ArrayList<>();
        for (MatchPrediction matchPrediction : matchPredictions) {
            Member member = matchPrediction.getMember();
            Boolean isSuccessed;

            Long moneyResult = matchPrediction.getAmount();

            if (matchPrediction.getMemberPredict() == matchInfo.getMatchResult()) { // 예측 성공
                if (matchInfo.getMatchResult() == MatchResult.DRAW) { // 무승부
                    moneyResult *= 5; // 5배
                } else { // 특정 팀 승리
                    moneyResult *= 2; // 2배
                }
                isSuccessed = true;
            } else { // 예측 실패
                moneyResult = 0L;
                isSuccessed = false;
            }

            matchPrediction.setIsSuccessed(isSuccessed);
            member.changeGameMoney(moneyResult);
            members.add(member);
        }
        memberRepository.saveAll(members);
        matchPredictionRepository.saveAll(matchPredictions);
    }

    /**
     * 어제 취소된 경기 승부예측 돈 돌려주기
     * */
    public void updateMatchPredictionByResult(MatchInfo matchInfo) {
        // 경기 예측한 사람 찾기
        List<MatchPrediction> matchPredictions = matchInfo.getMatchPredictions();

        // 게임 머니 돌려주기
        List<Member> members = new ArrayList<>();
        for (MatchPrediction matchPrediction : matchPredictions) {
            Member member = matchPrediction.getMember();
            member.changeGameMoney(matchPrediction.getAmount());
            members.add(member);
        }

        memberRepository.saveAll(members);
        matchPredictionRepository.saveAll(matchPredictions);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                               .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private MatchInfo findMatchInfoById(Long matchInfoId) {
        return matchInfoRepository.findById(matchInfoId)
                                  .orElseThrow(() -> new MatchException(ErrorCode.MATCH_NOT_FOUND));
    }

    private void existsPredictionByMemberIdAndMatchInfoId(Long memberId, Long matchInfoId) {
        if (matchPredictionRepository.existsByMemberIdAndMatchInfoId(memberId, matchInfoId)) {
            throw new GameException(ErrorCode.DUPLICATE_MATCH_PREDICTION);
        }
    }
}
