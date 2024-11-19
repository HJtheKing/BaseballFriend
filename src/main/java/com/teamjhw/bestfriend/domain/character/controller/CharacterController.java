package com.teamjhw.bestfriend.domain.character.controller;

import com.teamjhw.bestfriend.domain.character.dto.*;
import com.teamjhw.bestfriend.domain.character.service.CharacterService;
import com.teamjhw.bestfriend.domain.member.dto.MemberDetails;
import com.teamjhw.bestfriend.global.common.response.CommonResponse;
import com.teamjhw.bestfriend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bf/character")
@Tag(name = "Character", description = "캐릭터 관련 API")
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/status")
    @Operation(summary = "캐릭터 보유 현황 및 착용 여부 조회", description = "캐릭터 보유 현황 및 착용 여부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "캐릭터 보유 현황 및 착용 여부 조회 성공",
                    content = @Content(schema = @Schema(implementation = CharacterStatusDTO.Response.class))),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getStatusInfo(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        CharacterStatusDTO.Response response =
                characterService.getStatusInfo(memberDetails.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shop")
    @Operation(summary = "판매중인 물건 조회", description = "판매중인 물건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "판매중인 물건 조회 성공",
                    content = @Content(schema = @Schema(implementation = CharacterShopDTO.Response.class)))
    })
    public ResponseEntity<?> getShopInfo(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        CharacterShopDTO.Response response =
                characterService.getShopInfo(memberDetails.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/shop/character")
    @Operation(summary = "캐릭터 구매", description = "캐릭터 구매")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "캐릭터 구매 성공",
                    content = @Content(schema = @Schema(implementation = BuyCharacterDTO.Response.class))),
            @ApiResponse(responseCode = "404(1)", description = "회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404(2)", description = "캐릭터가 존재 하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(1)", description = "보유한 캐릭터입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(2)", description = "돈의 액수가 틀렸습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> postBuyCharacter(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody BuyCharacterDTO.Request request
    ) {
        BuyCharacterDTO.Response response = characterService.postBuyCharacterInfo(memberDetails.getId(), request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/shop/item")
    @Operation(summary = "아이템 구매", description = "아이템 구매")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "캐릭터 구매 성공",
                    content = @Content(schema = @Schema(implementation = BuyCharacterDTO.Response.class))),
            @ApiResponse(responseCode = "404(1)", description = "회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404(2)", description = "아이템이 존재 하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404(3)", description = "보유하지 않은 캐릭터의 아이템 구매는 불가입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(1)", description = "보유한 아이템입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(2)", description = "돈의 액수가 틀렸습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> postBuyItem(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody BuyItemDTO.Request request
    ) {
        BuyItemDTO.Response response = characterService.postBuyItem(memberDetails.getId(), request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/shop/background")
    @Operation(summary = "배경 구매", description = "배경 구매")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배경 구매 성공",
                    content = @Content(schema = @Schema(implementation = BuyCharacterDTO.Response.class))),
            @ApiResponse(responseCode = "404(1)", description = "회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404(2)", description = "배경이 존재 하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(1)", description = "보유한 배경입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(2)", description = "돈의 액수가 틀렸습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> postBuyBackground(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody BuyBackgroundDTO.Request request
    ) {
        BuyBackgroundDTO.Response response = characterService.postBuyBackground(memberDetails.getId(), request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/customization")
    @Operation(summary = "착용 여부 최신화", description = "착용 여부 최신화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "착용 여부 최신화 성공",
                    content = @Content(schema = @Schema(implementation = BuyCharacterDTO.Response.class))),
            @ApiResponse(responseCode = "404(1)", description = "회원을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404(2)", description = "보유하지 않은 캐릭입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404(3)", description = "보유하지 않은 아이템입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(1)", description = "카테고리가 머리가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(2)", description = "카테고리가 몸통이 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400(3)", description = "카테고리가 팔이 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404(4)", description = "보유하지 않은 배경입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> postCustomization(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody CharacterCustomizationDTO.Request request
    ) {
        characterService.postCustomization(memberDetails.getId(), request);

        CommonResponse response = CommonResponse.builder()
                .isSuccess(true)
                .message("캐릭터에 잘 적용되었습니다.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}