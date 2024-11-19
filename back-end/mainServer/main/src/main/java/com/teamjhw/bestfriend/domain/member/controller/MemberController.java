package com.teamjhw.bestfriend.domain.member.controller;

import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.domain.member.dto.MemberInfoDTO;
import com.teamjhw.bestfriend.domain.member.service.MemberService;
import com.teamjhw.bestfriend.entity.Member;
import com.teamjhw.bestfriend.global.common.response.CommonResponse;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.ErrorResponse;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("bf/member")
@Slf4j
@Tag(name = "Member", description = "멤버 관련 API")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 정보 수정
     **/
    @PatchMapping
    @Operation(summary = "회원 정보 수정", description = "회원 정보(관심팀, 알림 설정) 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 수정 성공",
                         content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "500", description = "회원 설정 수정 오류",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> updateMemberInfo(@RequestBody MemberInfoDTO.Request request,
                                              @AuthenticationPrincipal MemberDetails memberDetails) {
        try {
            memberService.updateMemberInfo(request, memberDetails.getId());
            return new ResponseEntity<>(CommonResponse.of(true, "정보 수정에 성공했습니다."), HttpStatus.OK);
        } catch (Exception e) {
            throw new MemberException(ErrorCode.UPDATE_SETTINGS_FAILURE);
        }
    }

    /**
     * 토큰 유효 여부 검사
     */
    @GetMapping("/check")
    @Operation(summary = "토큰 유효 여부 검사", description = "접속시 토큰 유효 여부 검사 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 유효",
                         content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "잘못된/만료된 토큰",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "토큰 처리 중 에러 발생",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> checkTokenIsValid(@AuthenticationPrincipal MemberDetails memberDetails) {
        return new ResponseEntity<>(CommonResponse.of(true, memberDetails.getId() + "번 회원님 인증되었습니다."), HttpStatus.OK);
    }

    /**
     * 회원 정보 조회
     **/
    @GetMapping
    @Operation(summary = "회원 정보 조회", description = "회원 정보(관심팀, 알림 설정) 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 성공",
                         content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "500", description = "회원 설정 조회 오류",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal MemberDetails memberDetails) {
        MemberInfoDTO.Request dto = memberService.getMemberInfo(memberDetails.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * 회원 관심팀명 조회
     **/
    @GetMapping("/team")
    @Operation(summary = "회원 관심팀명 조회", description = "회원 관심팀명 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 성공",
                         content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "회원 정보 조회 오류",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> getMemberFavoriteTeamName(@AuthenticationPrincipal MemberDetails memberDetails) {
        Member member = memberService.findMemberByMemberId(memberDetails.getId());
        String teamName = (member.getTeam() != null)? member.getTeam().getTeamName() : "";
        return new ResponseEntity<>(teamName, HttpStatus.OK);
    }
}
