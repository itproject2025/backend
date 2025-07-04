package com.itprojectbackend.flight.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itprojectbackend.flight.dto.FlightSearchRequest;
import com.itprojectbackend.flight.dto.FlightSearchResponse;
import com.itprojectbackend.flight.external.AmadeusTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final AmadeusTokenManager tokenManager;
    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_URL = "https://test.api.amadeus.com/v2/shopping/flight-offers";

    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        String accessToken = tokenManager.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("originLocationCode", request.departure())
                .queryParam("destinationLocationCode", request.arrival())
                .queryParam("departureDate", request.date())
                .queryParam("adults", "1")
                .queryParam("nonStop", "true")
                .queryParam("includedAirlineCodes", request.iata());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                String.class
        );


        // 2. Parse JSON response
        List<FlightSearchResponse> results = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.path("data");

            for (JsonNode flightOffer : data) {
                JsonNode segment = flightOffer
                        .path("itineraries").get(0)
                        .path("segments").get(0);

                String departureAirport = segment.path("departure").path("iataCode").asText();
                String departureTime = segment.path("departure").path("at").asText();
                String arrivalAirport = segment.path("arrival").path("iataCode").asText();
                String arrivalTime = segment.path("arrival").path("at").asText();

                String airlineCode = segment.path("carrierCode").asText();
                String flightNum = segment.path("number").asText();
                String flightIataNumber = airlineCode + flightNum;

                results.add(FlightSearchResponse.builder()
                        .flightIataNumber(flightIataNumber)
                        .departureAirportIata(departureAirport)
                        .departureScheduledTime(departureTime)
                        .arrivalAirportIata(arrivalAirport)
                        .arrivalScheduledTime(arrivalTime)
                        .airlineIataCode(airlineCode)
                        .build());
            }

        } catch (Exception e) {
            throw new RuntimeException("Amadeus 응답 파싱 실패", e);
        }

        return results;
    }
}