package com.teamjhw.bestfriend.domain.news.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamjhw.bestfriend.domain.news.dto.SearchNewsDTO;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.NewsException;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NaverNewsService {

    @Value("${naver.client-id}")
    private String CLIENT_ID;

    @Value("${naver.client-secret}")
    private String CLIENT_SECRET;

    @Value("${naver.news-search-api}")
    private String NAVER_NEWS_SEARCH_API;

    private static Map<String, String> newsMap = new HashMap<>();

    /*
     * Naver Search API 요청 메서드
     */
    public String requestBody(String query) {
        
        // 1. 검색어 인코딩 설정
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new NewsException(ErrorCode.UNSUPPORTED_ENCODING);
        }
        
        // 2. requestHeader 설정
        String apiURL = NAVER_NEWS_SEARCH_API + query;
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
        requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);

        int start = 1;
        SearchNewsDTO.Response response;
        while (true) {
            // 3. API 요청
            String responseBody = get(apiURL + String.format("&start=%s", start++), requestHeaders);

            // 4. responseBody 파싱
            response = parseNaverResponse(responseBody);

            // 5. 결과 쌓기
            newsMap.putAll(response.getNews());

            if (response.isExistsTwoDaysAgo()) {
                break;
            }
        }

        // 6. OpenAI 요약 요청 위한 문장 처리
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> news : newsMap.entrySet()) {
            result.append(news.getKey()).append(": ").append(news.getValue()).append("\\n");
        }

        return result.toString();
    }

    /*
     * Get 요청 메서드
     */
    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new NewsException(ErrorCode.NAVER_NEWS_DEFAULT_ERROR);
        } finally {
            con.disconnect();
        }
    }

    /*
     * URL 연결 요청
     */
    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new NewsException(ErrorCode.MALFORMED_URL);
        } catch (IOException e) {
            throw new NewsException(ErrorCode.NAVER_CONNECT_FAILED);
        }
    }

    /*
     * responseBody to String 메서드
     */
    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new NewsException(ErrorCode.NAVER_NEWS_DEFAULT_ERROR);
        }
    }

    /*
     * 뉴스 응답에서 필요한 정보 추출 : String to DTO
     */
    private static SearchNewsDTO.Response parseNaverResponse(String responseBody) {
        AtomicBoolean existsTwoDaysAgo = new AtomicBoolean(false);
        Map<String, String> news = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode itemsNode = rootNode.path("items");
//            log.info("==== Request API, Start Number : {} ====", rootNode.path("start").asText());
            StreamSupport.stream(itemsNode.spliterator(), false)
                         .filter(item -> {
                             boolean isYesterday = checkDateTime(item.path("pubDate").asText(), 1, new int[]{18, 0},
                                                                 new int[]{23, 59});
                             boolean isTwoDaysAgo = checkDateTime(item.path("pubDate").asText(), 2, new int[]{0, 0},
                                                                  new int[]{23, 59});
                             if (isTwoDaysAgo) {
                                 existsTwoDaysAgo.set(true);
                             }
                             return isYesterday;
                         })
                         .forEach(item -> {
                             String title = preprocessedText(item.path("title").asText(), "title");
                             String description = preprocessedText(item.path("description").asText(), "description");
                             news.put(title, description);
                         });
//            log.info("{} : {}", itemsNode.get(99).path("pubDate").asText(), existsTwoDaysAgo.get());
        } catch (Exception e) {
            throw new NewsException(ErrorCode.JSON_PARSING_ERROR);
        }
        return SearchNewsDTO.Response.of(existsTwoDaysAgo.get(), news);
    }

    /*
     * 특정 요일 & 시간 확인
     */
    private static boolean checkDateTime(String date, int minusDay, int[] startTime, int[] endTime) {
        LocalDateTime nowDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime targetStartTime = nowDateTime.minusDays(minusDay).truncatedTo(ChronoUnit.DAYS)
                                                   .withHour(startTime[0]).withMinute(startTime[1]);
        LocalDateTime targetEndTime = nowDateTime.minusDays(minusDay).truncatedTo(ChronoUnit.DAYS).withHour(endTime[0])
                                                 .withMinute(endTime[1]);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        LocalDateTime parsedDateTime = LocalDateTime.parse(date, formatter);
        return (parsedDateTime.isAfter(targetStartTime) && parsedDateTime.isBefore(targetEndTime));
    }

    /*
     * 텍스트 전처리
     */
    private static String preprocessedText(String text, String category) {
        String newText = text.replaceAll("<b>", "") // 1. HTML 태그 제거
                             .replaceAll("</b>", "")
                             .replaceAll("&quot;", "")
                             .replaceAll("&lt;", "")
                             .replaceAll("&gt;", "")
                             .replaceAll("\\[.*?\\]", ""); // 2. [포토], [사진] 등 사진 관련 텍스트 제거

        if (category.equals("description")) {
            newText = newText.replaceAll("[^\\.]*?\\.{3}", ""); // 3. 마무리 되지 않은 문장 제거
        }

        return newText.strip(); // 4. 공백제거
    }
}