package com.teamjhw.sse.global.sse.controller;

import com.teamjhw.sse.domain.member.dto.MemberDetails;
import com.teamjhw.sse.domain.member.dto.MemberResDto;
import com.teamjhw.sse.domain.member.service.MemberService;
import com.teamjhw.sse.global.exception.exceptionType.MemberException;
import com.teamjhw.sse.global.sse.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/bf/sse")
@RequiredArgsConstructor
@Tag(name = "SSE", description = "SSE 관련 API")
public class SseController {

    private final SseService sseService;
    private final MemberService memberService;

    @GetMapping(value = "/sub", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "sse 구독", description = "sse 구독 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "sse 구독 성공",
            content = @Content(schema = @Schema(implementation = MemberResDto.class))),
            @ApiResponse(responseCode = "500", description = "sse 구독 실패",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> subscribe(@AuthenticationPrincipal MemberDetails memberDetails) {
        MemberResDto m = memberService.findMemberById(memberDetails.getId());
        return ResponseEntity.ok(sseService.subscribe(m.memberId(), m.favoriteTeamId().toString()));
    }

    @GetMapping(value = "/sub/v2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "sse 구독", description = "sse 구독 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "see 구독 성공",
            content = @Content(schema = @Schema(implementation = SseEmitter.class))),
            @ApiResponse(responseCode = "400", description = "sse 구독 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SseEmitter subscribeV2(@AuthenticationPrincipal MemberDetails memberDetails) {
        try{
            MemberResDto m = memberService.findMemberById(memberDetails.getId());
            return sseService.subscribeV2(m.memberId(), m.favoriteTeamId());
        } catch (MemberException e) {
            return sseService.createErrorEmitter();
        }
    }

    @GetMapping(value = "/unsub")
    @Operation(summary = "sse 구독 취소", description = "sse 구독 취소 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "sse 구독 취소 성공",
            content = @Content(schema = @Schema(implementation = MemberResDto.class))),
            @ApiResponse(responseCode = "400", description = "sse 구독 취소 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> unsubscribe(@AuthenticationPrincipal MemberDetails memberDetails) {
        MemberResDto response = memberService.findMemberById(memberDetails.getId());
        sseService.unsubscribe(response.memberId(), response.favoriteTeamId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/latest-text")
    @Operation(summary = "최신 문자 중계 데이터 요청", description = "최신 문자 중계 데이터 요청 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최신 문자 중계 데이터 존재하여 반환",
            content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "204", description = "문자 중계 데이터가 존재하지 않음")
    })
    public ResponseEntity<?> findLatestText(@AuthenticationPrincipal MemberDetails memberDetails){
        MemberResDto m = memberService.findMemberById(memberDetails.getId());
        if(m == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String response = sseService.findLatestText(m.favoriteTeamId());
        if("".equals(response)) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/trial/brief")
    public void schedule() {
        for(int i = 1; i <= 10; i++){
            log.info("brief send to topic {}", i);
            sseService.sendNotificationV2(i + "", "morning-brief", "brief");
        }
    }
}
