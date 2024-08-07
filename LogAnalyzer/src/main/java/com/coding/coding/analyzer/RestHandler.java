package com.coding.coding.analyzer;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

@Service
public class RestHandler {

    private final RestTemplate restTemplate = new RestTemplate();

    public String makeHttpGetCall(String url) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
