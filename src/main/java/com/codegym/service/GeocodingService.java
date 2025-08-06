package com.codegym.service;

import com.codegym.exception.AppException;
import com.codegym.utils.StatusCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingService {

    @Value("${GOOGLE_API_KEY}")
    private String API_KEY;

    public double[] getLatLngFromAddress(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + API_KEY;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            JsonNode location = jsonNode
                    .get("results").get(0)
                    .get("geometry")
                    .get("location");

            double lat = location.get("lat").asDouble();
            double lng = location.get("lng").asDouble();

            return new double[]{lat, lng};

        } catch (Exception e) {
            throw new AppException(StatusCode.GEOCODING_FAILED);
        }
    }
}
