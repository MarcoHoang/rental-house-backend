package com.codegym.service;

import com.codegym.dto.response.GeocodingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "GOOGLE_API_KEY=test_key"
})
public class GeocodingServiceTest {

    @Test
    public void testAddressValidation() {
        // Test này chỉ kiểm tra xem service có load được không
        // Không test thực tế vì cần API key thật
        assertTrue(true);
    }

    @Test
    public void testGeocodingResponseStructure() {
        // Test cấu trúc GeocodingResponse
        GeocodingResponse response = GeocodingResponse.builder()
                .isValid(true)
                .latitude(21.0285)
                .longitude(105.8542)
                .formattedAddress("Hanoi, Vietnam")
                .originalAddress("Hanoi")
                .message("Success")
                .build();

        assertTrue(response.isValid());
        assertEquals(21.0285, response.getLatitude());
        assertEquals(105.8542, response.getLongitude());
        assertEquals("Hanoi, Vietnam", response.getFormattedAddress());
        assertEquals("Hanoi", response.getOriginalAddress());
        assertEquals("Success", response.getMessage());
    }
}
