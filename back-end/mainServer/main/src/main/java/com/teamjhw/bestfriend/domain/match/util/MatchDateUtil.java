package com.teamjhw.bestfriend.domain.match.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchDateUtil {

    /**
     * LocalTime 형의 시간으로 파싱
     *
     * @param time HH:mm 형태의 String 시간
     * @return HH:mm 형태의 LocalTime 시간
     * */
    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }
    /**
     * LocalTime 형의 시간으로 파싱
     *
     * @param time HH:mm 형태의 String 시간
     * @param date LocalDate형의 날짜
     * @return LocalDateTIme 형의 날짜와 시간
     * */
    public static LocalDateTime parseDateTime(String time, LocalDate date) {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(date, localTime);
    }

    /**
     * String 형의 날짜로 파싱
     *
     * @param date LocalDate 형의 날짜
     * @return 2022.01.01 String 형의 날짜
     * */
    public static String toStringDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return date.format(formatter);
    }

    /**
     * String 형의 날짜로 파싱
     *
     * @param date LocalDateTime 형의 날짜
     * @return 2022.01.01 String 형의 날짜
     * */
    public static String toStringDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return date.format(formatter);
    }

    /**
     * String 형의 날짜로 파싱
     *
     * @param date LocalDateTime 형의 날짜
     * @return HH:mm String 형의 날짜
     * */
    public static String toStringTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return date.format(formatter);
    }

    /**
     * KBO에서 제공하는 경기날짜 형식을 LocalDateTime형으로 파싱
     *
     * @param matchDate 01.01(월) 형식의 String
     * @param matchTime 01:01 형식의 String
     * */
    public static LocalDateTime parseKBODatetoLocalDate(String matchDate, String matchTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM.dd");
        LocalDate localMatchDate = LocalDate.parse("2024" + matchDate.substring(0, 5), formatter);
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localMatchTime = LocalTime.parse(matchTime, formatter);
        return LocalDateTime.of(localMatchDate, localMatchTime);
    }

    /**
     * 승부 예측 참여 가능한 시간인지 판단
     * @param matchDate MatchInfo에 저장된 경기 날짜와 시간
     */
    public static boolean isPossibleMatchPrediction(LocalDateTime matchDate){
        LocalDate today = LocalDate.now();
        LocalDateTime oneAM = today.atTime(LocalTime.of(1, 0));
        return (matchDate.toLocalDate().equals(today)  // 오늘 경기이고
                && (LocalDateTime.now().isAfter(oneAM) && LocalDateTime.now().isBefore(matchDate))); // 01:00AM~경기 시작 전이면 가능
    }

    /**
     * 오늘보다 이후 날짜에 대한 조회 요청인지 판단
     * @param date RequestParam으로 온 요청 날짜
     */
    public static boolean isAfterToday(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
}
