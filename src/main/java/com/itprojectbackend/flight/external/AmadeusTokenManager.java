package com.itprojectbackend.flight.external;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AmadeusTokenManager {

    private final AmadeusProperties properties;
    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;
    private long expiryTimeMillis;

    public String getAccessToken() {
        long now = System.currentTimeMillis();

        // 만료되었거나 없으면 새로 발급
        if (accessToken == null || now >= expiryTimeMillis) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            params.add("client_id", properties.getClientId());
            params.add("client_secret", properties.getClientSecret());

            HttpEntity<?> request = new HttpEntity<>(params, headers);

            ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                    "https://test.api.amadeus.com/v1/security/oauth2/token",
                    request,
                    JsonNode.class
            );

            JsonNode body = response.getBody();
            this.accessToken = body.get("access_token").asText();
            long expiresIn = body.get("expires_in").asLong();
            this.expiryTimeMillis = now + (expiresIn - 60) * 1000;

        }

        return this.accessToken;
    }
}