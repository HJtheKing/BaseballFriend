package com.teamjhw.bestfriend.global.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamjhw.bestfriend.domain.news.dto.SummarizeNewsDTO;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.NewsException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GPTUtil {

    @Value("${open-ai.api-key}")
    private String OPENAI_API_KEY;

    @Value("${open-ai.image-api-key}")
    private String OPENAI_IMAGE_API_KEY;

    @Value("${open-ai.text-generate-api}")
    private String OPENAI_TEXT_GENERATE_API;

    @Value("${open-ai.image-generate-api}")
    private String OPENAI_IMAGE_GENERATE_API;

    private final S3Util s3Util;

    /*
     * 뉴스 요약 텍스트 요청
     * 
     * TODO 프롬프팅 확인 및 수정 필요
     */
    @Retryable(retryFor = {HttpClientErrorException.BadRequest.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public List<SummarizeNewsDTO.Response> requestNewsTextBody(String query, String content) {

        String prompt = content
                + "\\n"
                + "너는 야구 뉴스 요약 Assistant야. 야구 뉴스 텍스트를 제시할게.\\n"
                + "각 줄은 '기사 제목(title) : 한 줄 요약(desciption)'으로 구성되어 있어.\\n"
                + "텍스트를 참고하여, 기사 제목에'" + query + "'가 들어간 오늘의 핵심 뉴스만을 추출해주는 역할을 부여할게"
                + "단계별로 생각해. 핵심 뉴스를 어떻게 추출하고 요약하여 전달할지 생각해."
                + "그런 다음, 가장 많이 언급된 5개의 결과물을 반환해. 중복되는 내용은 생략하되, 중복되는 내용의 요약 비중을 높여."
                + "사례보다는 개념과 논거를 강조하여 요약해줘. 단락에는 친근한 어투를 사용해줘, 너무 어렵거나 전문적인 용어만 사용하지는 말아줘."
                + "가급적 능동태를 이용하고, 수동태를 이용하지마. 문제나 도전에 직면했을 때 긍정적인 상황으로 전환하려는 태도를 보여, 거리감을 만들어내는 공식적이거나 냉담한 어투를 피해줘."
                + "결과는 result: [{title: , content: }]형식으로 줘. JSON으로 parsing 할거라 각 문장과 단어는 큰따옴표로 묶어주고 다른 텍스트 필요없으니까 싹 빼."
                + "title은 15자 이상 넘어가지 않게 요약해줘.";

        String requestBody = "{"
                + "\"model\": \"gpt-4o\","
                + "\"messages\": ["
                + "    {"
                + "        \"role\": \"system\","
                + "        \"content\": [{"
                + "            \"type\": \"text\","
                + "            \"text\": \"You are a sports journalist specializing in South Korean professional baseball. Your task is to provide insightful analysis, updates, and coverage of the KBO League, including player performances, team standings, and game highlights.\""
                + "        }] "
                + "    },"
                + "    {"
                + "        \"role\": \"user\","
                + "        \"content\": [{"
                + "            \"type\": \"text\","
                + "            \"text\": \"" + prompt + "\""
                + "        }] "
                + "    }"
                + "]"
                + "}";

        // 1. 뉴스 요약 요청
        ResponseEntity<String> response = post(requestBody, true);

        // 2. 응답 Body에서 messages만 추출
        String newsResponse;
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                newsResponse = rootNode.path("choices")
                                       .get(0)
                                       .path("message")
                                       .path("content")
                                       .asText();
            } catch (Exception e) {
                throw new NewsException(ErrorCode.JSON_PARSING_ERROR);
            }
        } else {
            throw new NewsException(ErrorCode.OPENAI_PARSING_ERROR);
        }

        // 3. messages -> 각 SummarizeNewsDTO로 변환
        List<SummarizeNewsDTO.Response> newsList = toSummarizeDTO(newsResponse);

        // 4. SummarizeNewDTO 각각에 대한 이미지 생성 요청 후 저장
        for (SummarizeNewsDTO.Response news : newsList) {
            news.setImageUrl(requestNewsImageBody(news.getTitle(), news.getContent()));
        }
        
        return newsList;
    }

    /*
     * 뉴스 이미지 생성 요청
     */
    public String requestNewsImageBody(String title, String content) {

        String prompt = "아래 기사제목으로 뉴스를 만들거야.\\n"
                + "귀여운 일러스트 느낌으로 썸네일 사진 만들어줘.\\n"
                + "사진에 어떠한 문자도 넣지마. 사진만 만들어줘."
                + "\\n" + title + " : " + content;

        String requestBody = "{"
                + "\"model\": \"dall-e-3\","
                + "\"prompt\": \"" + prompt + "\","
                + "\"n\": 1,"
                + "\"size\": \"1024x1024\""
                + "}";
        
        // // 1. 뉴스 이미지 생성 요청
        ResponseEntity<String> response = post(requestBody, false);

        // 2. 응답 Body에서 이미지 URL 추출
        String url;
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                url = rootNode.path("data")
                              .get(0)
                              .path("url")
                              .asText();
            } catch (Exception e) {
                throw new NewsException(ErrorCode.JSON_PARSING_ERROR);
            }
        } else {
            throw new NewsException(ErrorCode.OPENAI_PARSING_ERROR);
        }

        return convertUrlToMultipartFile(url);
    }

    /*
     * 공통 Post 요청 메서드
     */
    public ResponseEntity<String> post(String requestBody, boolean isTextApi) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String apiKey = isTextApi ? OPENAI_API_KEY : OPENAI_IMAGE_API_KEY;
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                isTextApi ? OPENAI_TEXT_GENERATE_API : OPENAI_IMAGE_GENERATE_API,
                HttpMethod.POST,
                requestEntity,
                String.class);
    }

    /**
     * URL To MultiPartFile 변환 메서드
     */
    public String convertUrlToMultipartFile(String imageUrl) {
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (IOException e) {
            throw new NewsException(ErrorCode.S3_PARSING_ERROR);
        }

        try(InputStream inputStream = url.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            // 1) image url -> byte[]
            BufferedImage urlImage = ImageIO.read(inputStream);
            ImageIO.write(urlImage, "jpg", bos);
            byte[] byteArray = bos.toByteArray();
            // 2) byte[] -> MultipartFile
            CustomMultipartFile multipartFile = new CustomMultipartFile(byteArray, imageUrl);
            return s3Util.uploadFile(multipartFile);
        } catch (IOException e) {
            throw new NewsException(ErrorCode.S3_PARSING_ERROR);
        }
    }

    private static List<SummarizeNewsDTO.Response> toSummarizeDTO(String responseBody) {
        // 1. JSON 마크다운 문법 제거
        responseBody = responseBody.replaceAll("```json", "")
                                   .replaceAll("```", "");

        List<SummarizeNewsDTO.Response> responses = new ArrayList<>();
        try {
            // 2. result 노드 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode resultNode = rootNode.path("result");

            // 3. 응답에서 SummarizeNewsDTO로 변환
            StreamSupport.stream(resultNode.spliterator(), false)
                         .forEach(result -> {
                             String title = result.path("title").asText();
                             String content = result.path("content").asText();
                             responses.add(SummarizeNewsDTO.Response.of(title, content));
                         });
        } catch (Exception e) {
            throw new NewsException(ErrorCode.JSON_PARSING_ERROR);
        }
        return responses;
    }

    /**
     * Retry 3번 실패시 처리 로직
     */
    @Recover
    public List<SummarizeNewsDTO.Response> recover(HttpClientErrorException e, String query, String content) {
        log.error("OPEN AI 에러, Error Message : {}, 요청 query : {}, 요청 content : {}", e.getMessage(), query, content);
        return new ArrayList<>();
    }
}
