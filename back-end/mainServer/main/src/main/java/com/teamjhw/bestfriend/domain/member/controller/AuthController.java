package com.teamjhw.bestfriend.domain.member.controller;

import com.teamjhw.bestfriend.domain.member.dto.MailAuthDTO;
import com.teamjhw.bestfriend.domain.character.dto.BuyCharacterDTO;
import com.teamjhw.bestfriend.domain.character.dto.CharacterCustomizationDTO;
import com.teamjhw.bestfriend.domain.character.service.CharacterService;
import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.domain.member.dto.MemberJoinDTO;
import com.teamjhw.bestfriend.domain.member.service.AuthService;
import com.teamjhw.bestfriend.domain.member.service.MemberService;
import com.teamjhw.bestfriend.global.common.response.BodyValidationExceptionResponse;
import com.teamjhw.bestfriend.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bf/auth")
@Slf4j
@Tag(name = "Auth", description = "인증인가 관련 API")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final CharacterService characterService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입 및 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                         content = @Content(schema = @Schema(implementation = MemberJoinDTO.Request.class)),
                         headers = @Header(name = "Authorization", description = "Bearer + /공백/ + 액세스토큰")),
            @ApiResponse(responseCode = "400", description = "DTO 유효성 검사 오류",
                         content = @Content(schema = @Schema(implementation = BodyValidationExceptionResponse.class))),
            @ApiResponse(responseCode = "400 / 409", description = "비밀번호 불일치, 이메일 중복",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> joinMember(@Valid @RequestBody MemberJoinDTO.Request request) {
        MemberDetails memberDetails = authService.joinMember(request);

        String accessToken = memberService.signIn(memberDetails);

        BuyCharacterDTO.Request buyRequest = new BuyCharacterDTO.Request(0L);
        characterService.postBuyCharacterInfo(memberDetails.getId(), buyRequest);

        CharacterCustomizationDTO.Request customizationRequest = new CharacterCustomizationDTO.Request(0L,-1L,-1L,-1L,-1L);
        characterService.postCustomization(memberDetails.getId(), customizationRequest);

        // 헤더에 토큰 저장
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        return new ResponseEntity<>(CommonResponse.of(true, "회원가입에 성공했습니다."), headers, HttpStatus.OK);
    }

    @PostMapping("/email")
    @Operation(summary = "이메일 인증번호 전송", description = "작성한 이메일로 인증번호를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공",
                         content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "dto validation 실패",
                         content = @Content(schema = @Schema(implementation = BodyValidationExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 이메일 오류",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "인증 메일 전송 오류",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> joinEmailAuthentication(@Valid @RequestBody MailAuthDTO.VerifyRequest request) {
        authService.joinEmailAuthentication(request.getEmail());
        return new ResponseEntity<>(CommonResponse.of(true, "메일 전송에 성공했습니다."), HttpStatus.OK);
    }


    @PatchMapping("/email")
    @Operation(summary = "이메일 인증번호 확인", description = "인증번호를 확인하고 이메일을 인증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 확인 성공",
                         content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400(1)", description = "dto validation 실패",
                         content = @Content(schema = @Schema(implementation = BodyValidationExceptionResponse.class))),
            @ApiResponse(responseCode = "400(2)", description = "인증 코드 없음",
                         content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

    })
    public ResponseEntity<?> checkEmailAuthentication(@Valid @RequestBody MailAuthDTO.CheckCodeRequest request) {
        Boolean isSuccess = authService.checkEmailAuthentication(request);
        String responseMessage = isSuccess? "인증에 성공했습니다." : "인증에 실패했습니다.";
        HttpStatus status = isSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(CommonResponse.of(isSuccess, responseMessage), status);
    }
}
