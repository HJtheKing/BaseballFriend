package com.teamjhw.bestfriend.domain.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamjhw.bestfriend.domain.match.dto.MatchScheduleDTO;
import com.teamjhw.bestfriend.domain.match.dto.TeamRankDTO;
import com.teamjhw.bestfriend.domain.match.dto.UpcomingMatchInfoDTO;
import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MatchException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FlaskService {

    private final MatchInfoService matchInfoService;
    private final ObjectMapper objectMapper;
    @Value("${flask.request.url}")
    private String PREFIX_URL_TO_FLASK;

    /**
     * 경기 시작 30분 전, flask 서버로 중계시작 요청을 보냅니다.
     */
    public void sendToFlaskStartBroadcast(String matchTime) {
        log.info("-----sendToFlaskStartBroadcast-----");
        List<UpcomingMatchInfoDTO.Request> requests = matchInfoService.getUpcomingMatchInfo(matchTime);

        if (requests.isEmpty()) {
            throw new MatchException(ErrorCode.MATCH_NOT_FOUND);
        }

        for (UpcomingMatchInfoDTO.Request request : requests) {
            String param = null;
            try {
                param = objectMapper.writeValueAsString(request);
            } catch (JsonProcessingException e) {
                throw new MatchException(ErrorCode.JSON_PARSING_ERROR);
            }

            String url = PREFIX_URL_TO_FLASK + "/match";
            ResponseEntity<String> response = sendToFlaskRequest(url, param);
            log.info("중계시작 보냄 ={}", response);
        }
    }

    /**
     * 팀 순위 조회 요청
     */
    public List<TeamRankDTO.Response> getTeamRank() {
        String url = PREFIX_URL_TO_FLASK + "/match/rank";
        ResponseEntity<String> response = sendToFlaskRequest(url, null);
        List<TeamRankDTO.Response> result = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                for (JsonNode node : rootNode) {
                    TeamRankDTO.Response responseDTO = objectMapper.treeToValue(node, TeamRankDTO.Response.class);
                    result.add(responseDTO);
                }
            } catch (Exception e) {
                throw new MatchException(ErrorCode.JSON_PARSING_ERROR);
            }
        }
        return result;
    }

    /**
     * 경기 일정 크롤링 요청
     *
     * @param month 크롤링 할 월
     */
    public List<MatchScheduleDTO.FlaskResponse> createMatchSchedulesOnMonth(int month) {
        String url = PREFIX_URL_TO_FLASK + "/match/schedule";
        Map<String, Object> map = new HashMap<>();
        map.put("month", month);
        String param = null;
        try {
            param = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new MatchException(ErrorCode.JSON_PARSING_ERROR);
        }

        ResponseEntity<String> response = sendToFlaskRequest(url, param);
        List<MatchScheduleDTO.FlaskResponse> result = new ArrayList<>();
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                for (JsonNode node : rootNode) {
                    MatchScheduleDTO.FlaskResponse responseDTO = objectMapper.treeToValue(node,
                                                                                          MatchScheduleDTO.FlaskResponse.class);
                    result.add(responseDTO);
                }
                return result;
            } catch (Exception e) {
                throw new MatchException(ErrorCode.JSON_PARSING_ERROR);
            }
        } else {
            throw new MatchException(ErrorCode.MATCH_SCHEDULE_CRAWLING_ERROR);
        }
    }

    /**
     * flask 서버 요청 공통 메소드
     */
    public ResponseEntity<String> sendToFlaskRequest(String url, String param) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(param, headers);

        return restTemplate.exchange(url,
                                     HttpMethod.POST,
                                     entity,
                                     String.class);
    }
}
