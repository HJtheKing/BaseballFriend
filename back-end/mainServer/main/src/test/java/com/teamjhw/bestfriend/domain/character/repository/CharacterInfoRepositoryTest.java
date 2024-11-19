//package com.teamjhw.bestfriend.domain.character.repository;
//
//import com.teamjhw.bestfriend.entity.CharacterInfo;
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
//public class CharacterInfoRepositoryTest {
//
//    @Autowired
//    private CharacterInfoRepository characterInfoRepository;
//
//    @Test
//    @Transactional
//    void 캐릭터_데이터_생성(){
//        characterInfoRepository.deleteAll();
//
//        List<CharacterInfo> characterInfos = new ArrayList<CharacterInfo>();
//
//        // 기본 고양이 캐릭터 추가
//        CharacterInfo CharacterInfo1 = CharacterInfo.builder()
//                .characterName("Cat0")
//                .price(0L)
//                .characterInfoSerialNumber(0L)
//                .build();
//        characterInfos.add(CharacterInfo1);
//
//        CharacterInfo CharacterInfo2 = CharacterInfo.builder()
//                .characterName("Cat1")
//                .price(1500L)
//                .characterInfoSerialNumber(1L)
//                .build();
//        characterInfos.add(CharacterInfo2);
//
//        CharacterInfo CharacterInfo3 = CharacterInfo.builder()
//                .characterName("Cat2")
//                .price(1500L)
//                .characterInfoSerialNumber(2L)
//                .build();
//        characterInfos.add(CharacterInfo3);
//
//        // 기본 곰 캐릭터 추가
//        CharacterInfo CharacterInfo4 = CharacterInfo.builder()
//                .characterName("Bear0")
//                .price(1500L)
//                .characterInfoSerialNumber(3L)
//                .build();
//        characterInfos.add(CharacterInfo4);
//
//        CharacterInfo CharacterInfo5 = CharacterInfo.builder()
//                .characterName("Bear1")
//                .price(1500L)
//                .characterInfoSerialNumber(4L)
//                .build();
//        characterInfos.add(CharacterInfo5);
//
//        CharacterInfo CharacterInfo6 = CharacterInfo.builder()
//                .characterName("Bear2")
//                .price(1500L)
//                .characterInfoSerialNumber(5L)
//                .build();
//        characterInfos.add(CharacterInfo6);
//
//        // 기본 새 캐릭터 추가
//        CharacterInfo CharacterInfo7 = CharacterInfo.builder()
//                .characterName("Bird0")
//                .price(1500L)
//                .characterInfoSerialNumber(6L)
//                .build();
//        characterInfos.add(CharacterInfo7);
//
//        CharacterInfo CharacterInfo8 = CharacterInfo.builder()
//                .characterName("Bird1")
//                .price(1500L)
//                .characterInfoSerialNumber(7L)
//                .build();
//        characterInfos.add(CharacterInfo8);
//
//        CharacterInfo CharacterInfo9 = CharacterInfo.builder()
//                .characterName("Bird2")
//                .price(1500L)
//                .characterInfoSerialNumber(8L)
//                .build();
//        characterInfos.add(CharacterInfo9);
//
//
//
//        characterInfoRepository.saveAll(characterInfos);
//    }
//
//}