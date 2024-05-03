package com.seoultech.codemos.service;

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
    private static final String url = "https://distinctive-odele-codemos.koyeb.app/score";

    public JudgeResultResponseDTO judgeCode(String code) {
        System.out.println("code = " + code);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);

            Map<String, Object> responseBody = responseEntity.getBody();

            float score = ((Number) responseBody.get("score")).floatValue();
            float fuel = ((Number) responseBody.get("fuel")).floatValue();
            int time = (int) responseBody.get("time");

            return new JudgeResultResponseDTO(score, fuel, time);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
