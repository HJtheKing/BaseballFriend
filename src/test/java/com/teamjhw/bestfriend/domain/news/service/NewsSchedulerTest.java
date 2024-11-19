//package com.teamjhw.bestfriend.domain.news.service;
//
//import com.teamjhw.bestfriend.domain.news.repository.NewsRepository;
//import com.teamjhw.bestfriend.domain.news.scheduler.NewsScheduler;
//import com.teamjhw.bestfriend.entity.News;
//import com.teamjhw.bestfriend.global.utils.GPTUtil;
//import jakarta.transaction.Transactional;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class NewsSchedulerTest {
//
//    @Autowired
//    private NewsScheduler newsScheduler;
//
//    @Autowired
//    private NewsRepository newsRepository;
//
//    @Autowired
//    private GPTUtil gptUtil;
//
//    @Test
//    @Transactional
//    void 뉴스_생성() {
//        newsScheduler.createNews();
//    }
//
//    @Test
//    @Transactional
//    void 뉴스_이미지_재생성() {
//        List<News> newsList = newsRepository.findAllByIdGreaterThan(155L);
//        for (News news : newsList) {
//            news.setImageUrl(gptUtil.requestNewsImageBody(news.getTitle(), news.getContent()));
//            newsRepository.save(news);
//            System.out.println(news.getTitle()+" : "+news.getContent()+ " : "+ news.getImageUrl());
//        }
//    }
//
//}
