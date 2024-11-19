package com.teamjhw.bestfriend.domain.character.service;

import com.teamjhw.bestfriend.domain.character.dto.*;
import com.teamjhw.bestfriend.domain.character.repository.*;
import com.teamjhw.bestfriend.domain.member.repository.MemberRepository;
import com.teamjhw.bestfriend.entity.*;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.CharacterException;
import com.teamjhw.bestfriend.global.exception.exceptionType.MemberException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterService {
    private final MemberCharacterRepository memberCharacterRepository;
    private final MemberItemRepository memberItemRepository;
    private final MemberBackgroundRepository memberBackgroundRepository;
    private final MemberRepository memberRepository;
    private final CharacterInfoRepository characterInfoRepository;
    private final BackgroundRepository backgroundRepository;
    private final ItemRepository itemRepository;


    /**
     * 앱 실행시 사용자의 보유 캐릭터 아이템 배경 보유 현황고 현재 착용하는 여부까지 전송
     */
    public CharacterStatusDTO.Response getStatusInfo(Long memberId) {
        // 1. 멤버의 캐릭터 목록 조회
        List<MemberCharacter> memberCharacters = memberCharacterRepository.findByMemberId(memberId);

        // 2. 멤버의 아이템 목록 조회
        List<MemberItem> memberItems = memberItemRepository.findByMemberId(memberId);

        // 3. 멤버의 배경 목록 조회
        List<MemberBackground> memberBackgrounds = memberBackgroundRepository.findByMemberId(memberId);

        // 4. 멤버 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));


        return CharacterStatusDTO.Response.of(memberCharacters, memberItems, memberBackgrounds, member);
    }

    /**
     * 사용자가 구매하지 않은 물품들을 알려주는 api
     */
    public CharacterShopDTO.Response getShopInfo(Long memberId){
        // 1. 판매 캐릭터 목록 조회
        List<CharacterInfo> characters = characterInfoRepository.findShopCharacters(memberId);

        // 2. 판매 아이템 목록 조회
        List<Item> items = itemRepository.findShopItems(memberId);

        // 3. 판매 배경 목록 조회
        List<Background> backgrounds = backgroundRepository.findShopBackgrounds(memberId);

        return CharacterShopDTO.Response.of(characters, items, backgrounds);
    }

    /**
     * 캐릭터 구매 후 gameMoney 업데이트. 새로운 memberCharacter 생성
     */
    public BuyCharacterDTO.Response postBuyCharacterInfo(Long memberId, BuyCharacterDTO.Request request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        CharacterInfo characterInfo = characterInfoRepository.findByCharacterInfoSerialNumber(request.getCharacterInfoSerialNumber())
                .orElseThrow(() -> new CharacterException(ErrorCode.CHARACTER_NOT_FOUND));

        // 이미 보유한 캐릭터인지 확인
        memberCharacterRepository.findByMemberIdAndCharacterInfoId(memberId, characterInfo.getId())
                .ifPresent(ownedCharacter -> {
                    throw new CharacterException(ErrorCode.CHARACTER_OWNERSHIP_ERROR);
                });

        // 게임머니에 오차가 존재하는 경우
        if((member.getGameMoney()- characterInfo.getPrice()) <0 ){
            throw new MemberException(ErrorCode.MONEY_AMOUNT_ERROR);
        }

        // 게임머니 업데이트
        member.changeGameMoney(-characterInfo.getPrice());
        memberRepository.save(member);

        //새로운 멤버 캐릭터 생성
        MemberCharacter newMemberCharacter = request.toEntity(member, characterInfo);
        memberCharacterRepository.save(newMemberCharacter);

        return BuyCharacterDTO.Response.of(member);
    }

    /**
     * 아이템 구매 후 gameMoney 업데이트. 새로운 memberItem 생성
     */
    public BuyItemDTO.Response postBuyItem(Long memberId, BuyItemDTO.Request request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        Item item = itemRepository.findByItemSerialNumber(request.getItemSerialNumber())
                .orElseThrow(() -> new CharacterException(ErrorCode.ITEM_NOT_FOUND));

        // 내가 구매하지 않은 캐릭터의 아이템인 경우 오류 전송
        MemberCharacter memberCharacter = memberCharacterRepository.findByMemberIdAndCharacterInfoId(memberId, item.getCharacterInfo().getId())
                .orElseThrow(() -> new CharacterException(ErrorCode.CHARACTER_ITEM_NOT_OWNED));

        // 이미 구매한 아이템인지 확인
        memberItemRepository.findByMemberIdAndItemId(memberId, item.getId())
                .ifPresent(ownedItem -> {
                    throw new CharacterException(ErrorCode.ITEM_OWNERSHIP_ERROR);
                });

        // 게임머니에 오차가 존재하는 경우
        if((member.getGameMoney()- item.getPrice()) <0 ){
            throw new CharacterException(ErrorCode.MONEY_AMOUNT_ERROR);
        }

        // 게임머니 업데이트
        member.changeGameMoney(-item.getPrice());
        memberRepository.save(member);

        //새로운 멤버 아이템 생성
        MemberItem newMemberItem = request.toEntity(member, memberCharacter ,item);
        memberItemRepository.save(newMemberItem);

        return BuyItemDTO.Response.of(member);
    }

    /**
     * 배경 구매 후 gameMoney 업데이트. 새로운 memberBackground 생성
     */
    public BuyBackgroundDTO.Response postBuyBackground(Long memberId, BuyBackgroundDTO.Request request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        Background background = backgroundRepository.findByBackgroundSerialNumber(request.getBackgroundSerialNumber())
                .orElseThrow(() -> new CharacterException(ErrorCode.BACKGROUND_NOT_FOUND));

        // 이미 보유한 캐릭터인지 확인
        memberBackgroundRepository.findByMemberIdAndBackgroundId(memberId, background.getId())
                .ifPresent(ownedBackground -> {
                    throw new CharacterException(ErrorCode.BACKGROUND_OWNERSHIP_ERROR);
                });

        // 게임머니에 오차가 존재하는 경우
        if((member.getGameMoney()- background.getPrice()) <0 ){
            throw new CharacterException(ErrorCode.MONEY_AMOUNT_ERROR);
        }

        // 게임머니 업데이트
        member.changeGameMoney(-background.getPrice());
        memberRepository.save(member);

        //새로운 멤버 캐릭터 생성
        MemberBackground newMemberBackground = request.toEntity(member, background);
        memberBackgroundRepository.save(newMemberBackground);

        return BuyBackgroundDTO.Response.of(member);
    }

    /**
     *  캐릭터와 악세서리가 바뀌정보를 받아서  member의 값을 변경을 하는 것입니다.
     *  -1 은 착용하지 않은 값입니다.
     */
    public void postCustomization(Long memberId, CharacterCustomizationDTO.Request request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        MemberCharacter memberCharacter = memberCharacterRepository.findByMemberIdAndCharacterInfo_CharacterInfoSerialNumber(memberId, request.getCharacterInfoSerialNumber())
                .orElseThrow(() -> new CharacterException(ErrorCode.CHARACTER_NOT_OWNERSHIP));

        if(request.getHeadItemSerialNumber() != -1) {
            MemberItem memberHeadItem = memberItemRepository.findByMemberIdAndMemberCharacterIdAndItem_ItemSerialNumber(memberId, memberCharacter.getId(), request.getHeadItemSerialNumber())
                    .orElseThrow(() -> new CharacterException(ErrorCode.ITEM_NOT_OWNERSHIP));
            if(memberHeadItem.getItem().getItemCategory().getKey() != 0) {
                throw new CharacterException(ErrorCode.INVALID_HEAD_CATEGORY);
            }
        }

        if(request.getBodyItemSerialNumber() != -1) {
            MemberItem memberBodyItem = memberItemRepository.findByMemberIdAndMemberCharacterIdAndItem_ItemSerialNumber(memberId, memberCharacter.getId(), request.getBodyItemSerialNumber())
                    .orElseThrow(() -> new CharacterException(ErrorCode.ITEM_NOT_OWNERSHIP));
            if(memberBodyItem.getItem().getItemCategory().getKey() != 1) {
                throw new CharacterException(ErrorCode.INVALID_BODY_CATEGORY);
            }
        }

        if(request.getArmItemSerialNumber() != -1) {
            MemberItem memberArmItem = memberItemRepository.findByMemberIdAndMemberCharacterIdAndItem_ItemSerialNumber(memberId, memberCharacter.getId(), request.getArmItemSerialNumber())
                    .orElseThrow(() -> new CharacterException(ErrorCode.ITEM_NOT_OWNERSHIP));
            if(memberArmItem.getItem().getItemCategory().getKey() != 2) {
                throw new CharacterException(ErrorCode.INVALID_ARM_CATEGORY);
            }
        }

        if(request.getBackgroundSerialNumber() != -1) {
            memberBackgroundRepository.findByMemberIdAndBackground_BackgroundSerialNumber(memberId, request.getBackgroundSerialNumber())
                    .orElseThrow(() -> new CharacterException(ErrorCode.BACKGROUND_NOT_OWNERSHIP));
        }

        member.updateCharacterItemAndBackgroundIds(
                request.getCharacterInfoSerialNumber(),
                request.getHeadItemSerialNumber(),
                request.getBodyItemSerialNumber(),
                request.getArmItemSerialNumber(),
                request.getBackgroundSerialNumber());
        memberRepository.save(member);
    }

}

