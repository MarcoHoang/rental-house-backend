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

            // Kiểm tra status của response
            String status = jsonNode.get("status").asText();
            if (!"OK".equals(status)) {
                System.err.println("Google Geocoding API returned status: " + status + " for address: " + address);
                throw new AppException(StatusCode.GEOCODING_FAILED);
            }

            // Kiểm tra có results không
            JsonNode results = jsonNode.get("results");
            if (results == null || results.isEmpty() || results.size() == 0) {
                System.err.println("Google Geocoding API returned no results for address: " + address);
                throw new AppException(StatusCode.GEOCODING_FAILED);
            }

            // Lấy kết quả đầu tiên
            JsonNode firstResult = results.get(0);
            if (firstResult == null) {
                System.err.println("Google Geocoding API returned null first result for address: " + address);
                throw new AppException(StatusCode.GEOCODING_FAILED);
            }

            // Kiểm tra geometry và location
            JsonNode geometry = firstResult.get("geometry");
            if (geometry == null) {
                System.err.println("Google Geocoding API returned null geometry for address: " + address);
                throw new AppException(StatusCode.GEOCODING_FAILED);
            }

            JsonNode location = geometry.get("location");
            if (location == null) {
                System.err.println("Google Geocoding API returned null location for address: " + address);
                throw new AppException(StatusCode.GEOCODING_FAILED);
            }

            // Kiểm tra lat và lng
            JsonNode latNode = location.get("lat");
            JsonNode lngNode = location.get("lng");
            
            if (latNode == null || lngNode == null) {
                System.err.println("Google Geocoding API returned null lat/lng for address: " + address);
                throw new AppException(StatusCode.GEOCODING_FAILED);
            }

            double lat = latNode.asDouble();
            double lng = lngNode.asDouble();

            // Kiểm tra giá trị hợp lệ
            if (lat == 0.0 && lng == 0.0) {
                System.err.println("Google Geocoding API returned invalid coordinates (0,0) for address: " + address);
                throw new AppException(StatusCode.GEOCODING_FAILED);
            }

            return new double[]{lat, lng};

        } catch (Exception e) {
            System.err.println("Exception in geocoding for address: " + address + ". Error: " + e.getMessage());
            throw new AppException(StatusCode.GEOCODING_FAILED);
        }
    }
}
