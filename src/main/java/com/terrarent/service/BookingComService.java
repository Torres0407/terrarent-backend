package com.terrarent.service;

import com.terrarent.dto.bookingcom.BookingSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingComService {

    @Value("${bookingcom.api.base-url:https://demandapi-sandbox.booking.com/3.1}")
    private String baseUrl;

    @Value("${bookingcom.api.affiliate-id:YOUR_AFFILIATE_ID}")
    private String affiliateId;

    @Value("${bookingcom.api.token:YOUR_API_TOKEN}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchAccommodations(BookingSearchRequest request) {
        String url = baseUrl + "/accommodations/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Standard authentication for Demand API usually requires Bearer token or specific headers
        headers.setBearerAuth(apiToken);
        headers.set("X-Affiliate-Id", affiliateId);

        // Map request to the exact payload required by Booking.com
        Map<String, Object> payload = new HashMap<>();
        payload.put("city", Integer.parseInt(request.getCityId()));
        payload.put("checkin", request.getCheckin());
        payload.put("checkout", request.getCheckout());

        Map<String, Object> booker = new HashMap<>();
        booker.put("country", request.getBookerCountry());
        booker.put("platform", request.getPlatform() != null ? request.getPlatform() : "desktop");
        payload.put("booker", booker);

        Map<String, Object> guests = new HashMap<>();
        guests.put("number_of_rooms", request.getNumberOfRooms() > 0 ? request.getNumberOfRooms() : 1);
        guests.put("number_of_adults", request.getNumberOfAdults() > 0 ? request.getNumberOfAdults() : 2);
        payload.put("guests", guests);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            // For now, return the error message. In production, map to a proper Custom Exception.
            return "{\"error\": \"Failed to fetch data from Booking.com: " + e.getMessage() + "\"}";
        }
    }
}
