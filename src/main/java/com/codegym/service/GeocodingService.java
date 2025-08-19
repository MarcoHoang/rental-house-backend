package com.codegym.service;

import com.codegym.dto.response.GeocodingResponse;
import com.codegym.exception.AppException;
import com.codegym.utils.StatusCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class GeocodingService {

    // Sử dụng Nominatim (OpenStreetMap geocoding service) - hoàn toàn miễn phí
    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);
    private static final int CACHE_SIZE = 1000;
    private static final Duration CACHE_DURATION = Duration.ofHours(24);

    private final HttpClient httpClient;
    private final ConcurrentHashMap<String, CachedGeocodingResult> cache;

    public GeocodingService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
        this.cache = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void validateService() {
        try {
            String testAddress = "Hanoi, Vietnam";
            String testUrl = buildGeocodingUrl(testAddress);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(testUrl))
                    .timeout(REQUEST_TIMEOUT)
                    .header("User-Agent", "RentalHouseApp/1.0") // Nominatim yêu cầu User-Agent
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                log.info("OpenStreetMap Nominatim service is accessible");
            } else {
                log.warn("OpenStreetMap Nominatim service returned status: {}", response.statusCode());
            }
        } catch (Exception e) {
            log.warn("OpenStreetMap Nominatim service validation failed: {}", e.getMessage());
            // Không throw exception vì service vẫn có thể hoạt động
        }
    }

    private static class CachedGeocodingResult {
        final GeocodingResponse result;
        final long timestamp;

        CachedGeocodingResult(GeocodingResponse result) {
            this.result = result;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_DURATION.toMillis();
        }
    }

    /**
     * Chuyển đổi địa chỉ thành tọa độ latitude và longitude
     * @param address Địa chỉ cần chuyển đổi
     * @return Mảng double[2] chứa [latitude, longitude]
     * @throws AppException nếu không thể xác định được vị trí
     * @deprecated Sử dụng getGeocodingResponse thay thế
     */
    @Deprecated
    public double[] getLatLngFromAddress(String address) {
        GeocodingResponse response = getGeocodingResponse(address);
        if (!response.isValid() || response.getLatitude() == null || response.getLongitude() == null) {
            throw new AppException(StatusCode.GEOCODING_FAILED);
        }
        return new double[]{response.getLatitude(), response.getLongitude()};
    }

    /**
     * Chuyển đổi địa chỉ thành GeocodingResponse
     * @param address Địa chỉ cần chuyển đổi
     * @return GeocodingResponse chứa thông tin tọa độ và địa chỉ đã format
     */
    public GeocodingResponse getGeocodingResponse(String address) {
        if (address == null || address.trim().isEmpty()) {
            log.warn("Address is null or empty");
            return GeocodingResponse.builder()
                    .isValid(false)
                    .originalAddress(address)
                    .message("Địa chỉ không được để trống")
                    .build();
        }

        String normalizedAddress = address.trim();
        
        // Kiểm tra cache trước
        CachedGeocodingResult cached = cache.get(normalizedAddress);
        if (cached != null && !cached.isExpired()) {
            log.debug("Returning cached result for address: {}", normalizedAddress);
            return cached.result;
        }

        try {
            log.info("Calling OpenStreetMap Nominatim API for address: {}", normalizedAddress);
            
            JsonNode jsonNode = callGeocodingApi(normalizedAddress);
            
            log.info("Nominatim API response received for address: {}", normalizedAddress);

            GeocodingResponse response = processGeocodingResponse(jsonNode, normalizedAddress);
            
            // Cache kết quả thành công
            if (response.isValid()) {
                cacheResult(normalizedAddress, response);
            }
            
            return response;

        } catch (Exception e) {
            log.error("Exception in geocoding for address: {}. Error: {}", normalizedAddress, e.getMessage(), e);
            return GeocodingResponse.builder()
                    .isValid(false)
                    .originalAddress(normalizedAddress)
                    .message("Lỗi khi xử lý địa chỉ: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Gọi OpenStreetMap Nominatim API
     */
    private JsonNode callGeocodingApi(String address) throws Exception {
        String url = buildGeocodingUrl(address);
        log.info("Calling Nominatim API with URL: {}", url);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(REQUEST_TIMEOUT)
                .header("User-Agent", "RentalHouseApp/1.0") // Nominatim yêu cầu User-Agent
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Nominatim API response status: {}", response.statusCode());
        
        if (response.statusCode() != 200) {
            log.error("Nominatim API returned error status: {} with body: {}", response.statusCode(), response.body());
            throw new RuntimeException("Nominatim API returned status: " + response.statusCode());
        }
        
        String responseBody = response.body();
        log.debug("Nominatim API response body: {}", responseBody);
        
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody);
    }

    /**
     * Xây dựng URL cho OpenStreetMap Nominatim API
     */
    private String buildGeocodingUrl(String address) {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        return NOMINATIM_API_URL + "?q=" + encodedAddress + "&format=json&limit=1&addressdetails=1";
    }

    /**
     * Xử lý response từ OpenStreetMap Nominatim API
     */
    private GeocodingResponse processGeocodingResponse(JsonNode jsonNode, String address) {
        // Nominatim trả về array, không có status field như Google
        if (jsonNode.isArray()) {
            if (jsonNode.size() > 0) {
                return extractGeocodingData(jsonNode, address);
            } else {
                log.warn("No results found for address: {}", address);
                return GeocodingResponse.builder()
                        .isValid(false)
                        .originalAddress(address)
                        .message("Không tìm thấy địa chỉ này")
                        .build();
            }
        } else {
            log.error("Invalid response format from Nominatim for address: {}", address);
            return GeocodingResponse.builder()
                    .isValid(false)
                    .originalAddress(address)
                    .message("Lỗi định dạng dữ liệu từ OpenStreetMap")
                    .build();
        }
    }

    /**
     * Trích xuất dữ liệu từ response JSON của OpenStreetMap Nominatim API
     */
    private GeocodingResponse extractGeocodingData(JsonNode jsonNode, String address) {
        // Nominatim trả về array, lấy kết quả đầu tiên
        JsonNode firstResult = jsonNode.get(0);
        if (firstResult == null) {
            log.warn("Nominatim returned null first result for address: {}", address);
            return GeocodingResponse.builder()
                    .isValid(false)
                    .originalAddress(address)
                    .message("Dữ liệu không hợp lệ từ OpenStreetMap")
                    .build();
        }

        // Lấy formatted address từ display_name
        String formattedAddress = firstResult.has("display_name") 
                ? firstResult.get("display_name").asText() 
                : address;

        // Kiểm tra lat và lon (Nominatim dùng "lat" và "lon")
        JsonNode latNode = firstResult.get("lat");
        JsonNode lonNode = firstResult.get("lon");
        
        if (latNode == null || lonNode == null) {
            log.warn("Nominatim returned null lat/lon for address: {}", address);
            return GeocodingResponse.builder()
                    .isValid(false)
                    .originalAddress(address)
                    .formattedAddress(formattedAddress)
                    .message("Tọa độ không hợp lệ")
                    .build();
        }

        double lat = latNode.asDouble();
        double lng = lonNode.asDouble();

        // Kiểm tra giá trị hợp lệ
        if (lat == 0.0 && lng == 0.0) {
            log.warn("Nominatim returned invalid coordinates (0,0) for address: {}", address);
            return GeocodingResponse.builder()
                    .isValid(false)
                    .originalAddress(address)
                    .formattedAddress(formattedAddress)
                    .message("Tọa độ không hợp lệ (0,0)")
                    .build();
        }

        // Kiểm tra tọa độ có hợp lệ không (latitude: -90 to 90, longitude: -180 to 180)
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            log.warn("Nominatim returned out-of-range coordinates ({}, {}) for address: {}", lat, lng, address);
            return GeocodingResponse.builder()
                    .isValid(false)
                    .originalAddress(address)
                    .formattedAddress(formattedAddress)
                    .message("Tọa độ nằm ngoài phạm vi hợp lệ")
                    .build();
        }

        log.info("Successfully geocoded address: {} to coordinates: ({}, {})", address, lat, lng);
        return GeocodingResponse.builder()
                .isValid(true)
                .latitude(lat)
                .longitude(lng)
                .formattedAddress(formattedAddress)
                .originalAddress(address)
                .message("Xác định vị trí thành công")
                .build();
    }

    /**
     * Cache kết quả geocoding
     */
    private void cacheResult(String address, GeocodingResponse result) {
        // Giới hạn kích thước cache
        if (cache.size() >= CACHE_SIZE) {
            // Xóa các entry cũ nhất
            cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
            
            // Nếu vẫn còn quá lớn, xóa một số entry cũ
            if (cache.size() >= CACHE_SIZE) {
                cache.clear();
                log.info("Cache cleared due to size limit");
            }
        }
        
        cache.put(address, new CachedGeocodingResult(result));
        log.debug("Cached result for address: {}", address);
    }

    /**
     * Kiểm tra xem địa chỉ có thể geocode được không
     * @param address Địa chỉ cần kiểm tra
     * @return true nếu địa chỉ có thể geocode được, false nếu không
     */
    public boolean isAddressValid(String address) {
        GeocodingResponse response = getGeocodingResponse(address);
        return response.isValid();
    }

    /**
     * Lấy thông tin chi tiết về địa chỉ từ Google Geocoding API
     * @param address Địa chỉ cần lấy thông tin
     * @return Thông tin chi tiết về địa chỉ hoặc null nếu không tìm thấy
     */
    public String getFormattedAddress(String address) {
        GeocodingResponse response = getGeocodingResponse(address);
        return response.isValid() ? response.getFormattedAddress() : null;
    }

    /**
     * Xóa cache
     */
    public void clearCache() {
        cache.clear();
        log.info("Geocoding cache cleared");
    }

    /**
     * Lấy thống kê cache
     */
    public String getCacheStats() {
        long expiredCount = cache.values().stream().filter(CachedGeocodingResult::isExpired).count();
        long validCount = cache.size() - expiredCount;
        
        return String.format("Cache stats - Total: %d, Valid: %d, Expired: %d", 
                cache.size(), validCount, expiredCount);
    }
}
