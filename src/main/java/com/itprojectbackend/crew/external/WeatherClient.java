package com.itprojectbackend.crew.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.itprojectbackend.crew.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherClient {

    @Value("${openweatherMap.secret}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherResponse getWeatherByCity(String city){
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=kr",
                city, apiKey
        );

        try {
            JsonNode body = restTemplate.getForEntity(url, JsonNode.class).getBody();

            String description = body.get("weather").get(0).get("description").asText();
            double temp = body.get("main").get("temp").asDouble();
            double rainfall = 0.0;

            if (body.has("rain") && body.get("rain").has("1h")) {
                rainfall = body.get("rain").get("1h").asDouble();
            }

            return new WeatherResponse(description, temp, rainfall);
        } catch (Exception e) {
            return new WeatherResponse("정보 없음", 0.0, 0.0);
        }
    }
}
