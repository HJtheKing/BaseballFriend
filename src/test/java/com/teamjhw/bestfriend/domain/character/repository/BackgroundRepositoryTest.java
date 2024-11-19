//package com.teamjhw.bestfriend.domain.character.repository;
//
//import com.teamjhw.bestfriend.entity.Background;
//import jakarta.transaction.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@Disabled
//@SpringBootTest
//public class BackgroundRepositoryTest {
//
//    @Autowired
//    private BackgroundRepository backgroundRepository;
//
//    @Test
//    @Transactional
//    void 배경_데이터_생성(){
//        backgroundRepository.deleteAll();
//
//        List<Background> backgrounds = new ArrayList<Background>();
//
//        for (long i = 50; i <= 67; i++) {
//            Background background = Background.builder()
//                    .price(200L)
//                    .backgroundSerialNumber(i)
//                    .build();
//            backgrounds.add(background);
//        }
//
//
//
//        backgroundRepository.saveAll(backgrounds);
//    }
//
//}
