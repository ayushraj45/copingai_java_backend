package com.example.copingai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExpoPushNotificationService {
    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    private final RestTemplate restTemplate;

    public ExpoPushNotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean sendPushNotification (String expoToken, String title, String body) {
        try {
            HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> payload = new HashMap<>();
            payload.put("to", expoToken);
            payload.put("title", title);
            payload.put("body", body);

            HttpEntity<Map<String,String>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    EXPO_PUSH_URL,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Push notification sent successfully");
                return true;
            } else {
                System.err.println("Failed to send push notification");
                System.err.println("Status Code: " + response.getStatusCode());
                System.err.println("Response Body: " + response.getBody());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error sending push notification: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        }


}

