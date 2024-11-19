//package com.teamjhw.bestfriend.domain.character.repository;
//
//import com.teamjhw.bestfriend.domain.match.repository.TeamRepository;
//import com.teamjhw.bestfriend.entity.*;
//import com.teamjhw.bestfriend.global.exception.ErrorCode;
//import com.teamjhw.bestfriend.global.exception.exceptionType.CharacterException;
//import jakarta.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@Disabled
//@SpringBootTest
//public class ItemRepositoryTest {
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    @Autowired
//    private CharacterInfoRepository characterInfoRepository;
//
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Test
//    @Transactional
//    void 아이템_데이터_생성(){
//        itemRepository.deleteAll();
//
//        Team teamKIA = teamRepository.findByTeamName("KIA")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamSamsung = teamRepository.findByTeamName("삼성")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamLG = teamRepository.findByTeamName("LG")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamDoosan = teamRepository.findByTeamName("두산")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamKT = teamRepository.findByTeamName("KT")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamSSG = teamRepository.findByTeamName("SSG")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamLotte = teamRepository.findByTeamName("롯데")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamHanwha = teamRepository.findByTeamName("한화")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamNC = teamRepository.findByTeamName("NC")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamKiwoom = teamRepository.findByTeamName("키움")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        Team teamNull = teamRepository.findByTeamName("없음")
//                .orElseThrow(() -> new CharacterException(ErrorCode.TEAM_NOT_FOUND));
//
//        List<Item> items = new ArrayList<Item>();
//
//// 모든 캐릭터(0-8)에 대해 아이템 생성
//        for (long charId = 0; charId <= 8; charId++) {
//            CharacterInfo characterInfo = characterInfoRepository.findByCharacterInfoSerialNumber(charId)
//                    .orElseThrow(() -> new CharacterException(ErrorCode.CHARACTER_NOT_FOUND));
//
//            // Head items (Category 0) - 시리얼 번호 20씩 증가
//            long headBaseSerial = 100 + (charId * 20);
//            // 팀별 아이템
//            items.add(createItem(characterInfo, headBaseSerial + 0, teamKIA, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 1, teamKT, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 2, teamLG, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 3, teamNC, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 4, teamSSG, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 5, teamDoosan, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 6, teamLotte, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 7, teamSamsung, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 8, teamKiwoom, 500L, ItemCategory.Head));
//            items.add(createItem(characterInfo, headBaseSerial + 9, teamHanwha, 500L, ItemCategory.Head));
//            // 랜덤(없음) 아이템
//            for (int i = 10; i <= 13; i++) {
//                items.add(createItem(characterInfo, headBaseSerial + i, teamNull, 500L, ItemCategory.Head));
//            }
//
//            // Body items (Category 1) - 시리얼 번호 10씩 증가
//            long bodyBaseSerial = 400 + (charId * 10);
//            for (int i = 0; i <= 2; i++) {
//                items.add(createItem(characterInfo, bodyBaseSerial + i, teamNull, 250L, ItemCategory.Body));
//            }
//
//            // Arm items (Category 2) - 시리얼 번호 10씩 증가
//            long armBaseSerial = 700 + (charId * 10);
//            for (int i = 0; i <= 3; i++) {
//                items.add(createItem(characterInfo, armBaseSerial + i, teamNull, 400L, ItemCategory.Arm));
//            }
//        }
//
//        itemRepository.saveAll(items);
//    }
//
//    private Item createItem(CharacterInfo characterInfo, long serialNumber, Team team, long price, ItemCategory category) {
//        return Item.builder()
//                .characterInfo(characterInfo)
//                .itemSerialNumber(serialNumber)
//                .team(team)
//                .price(price)
//                .itemCategory(category)
//                .build();
//    }
//
//}
