package com.seoultech.codemos.service;

import com.seoultech.codemos.dto.ProblemResponseDto;
import java.util.HashMap;
import java.util.Map;

import com.seoultech.codemos.dto.JudgeResultResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class JudgeService {

    private final RestTemplate restTemplate;
//    private static final String url = "https://distinctive-odele-codemos.koyeb.app/score";
    private static final String url = "http://localhost:3001/score";

    public JudgeResultResponseDTO judgeCode(ProblemResponseDto problem, String code) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("problem", problem);
        requestBody.put("code", code);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);

            Map<String, Object> responseBody = responseEntity.getBody();

            float score = ((Number) responseBody.get("score")).floatValue();
            float fuel = ((Number) responseBody.get("fuel")).floatValue();
            int time = (int) responseBody.get("time");
            int bytes = (int) responseBody.get("bytes");
            float angle = ((Number) responseBody.get("angle")).floatValue();
            float velX = ((Number) responseBody.get("velX")).floatValue();
            float velY = ((Number) responseBody.get("velY")).floatValue();


            return new JudgeResultResponseDTO(score, fuel, time, bytes, angle, velX, velY);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}