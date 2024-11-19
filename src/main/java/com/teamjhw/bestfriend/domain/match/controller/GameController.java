package com.teamjhw.bestfriend.domain.match.controller;

import com.teamjhw.bestfriend.domain.match.dto.GameMoneyDTO;
import com.teamjhw.bestfriend.domain.match.dto.HitGameDTO;
import com.teamjhw.bestfriend.domain.match.dto.MatchInfoDTO;
import com.teamjhw.bestfriend.domain.match.dto.ParticipationDaysDTO;
import com.teamjhw.bestfriend.domain.match.dto.PredictDTO;
import com.teamjhw.bestfriend.domain.match.service.GameService;
import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bf/game")
@Tag(name = "Game", description = "게임 관련 API")
public class GameController {

    private final GameService gameService;

    @GetMapping("/money")
    @Operation(summary = "게임 머니 조회", description = "게임 머니 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게임 머니 조회 성공",
                         content = @Content(schema = @Schema(implementation = GameMoneyDTO.Response.class)))
    })
    ResponseEntity<?> getGameMoney(@AuthenticationPrincipal MemberDetails memberDetails) {
        GameMoneyDTO.Response response = gameService.getGameMoney(memberDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/hit-game")
    @Operation(summary = "공 맞추기 게임 종료", description = "공 맞추기 게임 종료 & 게임 머니 계산")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공 맞추기 게임 및 게임 머니 계산 성공",
                         content = @Content(schema = @Schema(implementation = HitGameDTO.Response.class)))
    })
    ResponseEntity<?> finishHitGame(@AuthenticationPrincipal MemberDetails memberDetails,
                                    @RequestBody HitGameDTO.Request request) {
        HitGameDTO.Response response = gameService.finishHitGame(memberDetails.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/prediction")
    @Operation(summary = "승부 예측 참여", description = "경기 정보 단 건에 대한 승부 예측 참여")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "승부 예측 참여 성공",
                         content = @Content(schema = @Schema(implementation = PredictDTO.Response.class))),
            @ApiResponse(responseCode = "400(1)", description = "게임 머니가 부족합니다",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(2)", description = "올바르지 않은 경기 예측 값입니다.",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "접근 가능한 경기가 아닙니다.",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "경기를 찾을 수 없습니다.",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 참여한 경기 예측입니다.",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<?> predictMatch(@AuthenticationPrincipal MemberDetails memberDetails,
                                   @RequestBody PredictDTO.Request request) {
        PredictDTO.Response response = gameService.createMatchPrediction(memberDetails.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/prediction")
    @Operation(summary = "승부 예측 정보 조회", description = "승부 예측 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승부 예측 정보 조회 성공",
                         content = @Content(schema = @Schema(implementation = MatchInfoDTO.Response.class))),
            @ApiResponse(responseCode = "403", description = "접근 가능한 경기가 아닙니다.",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<?> getMatchInfo(@AuthenticationPrincipal MemberDetails memberDetails, @RequestParam LocalDate date) {
        MatchInfoDTO.Response response = gameService.getMatchInfoList(memberDetails.getId(), date);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/participation-days")
    @Operation(summary = "승부 예측 참여 일수 조회", description = "승부예측 연속 참여 & 총 참여 일수 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승부 예측 참여 일수 조회 성공",
                         content = @Content(schema = @Schema(implementation = ParticipationDaysDTO.Response.class)))
    })
    ResponseEntity<?> getParticipationDays(@AuthenticationPrincipal MemberDetails memberDetails) {
        ParticipationDaysDTO.Response response = gameService.getParticipationDays(memberDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
