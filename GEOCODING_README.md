# GeocodingService - Hướng dẫn sử dụng

## Tổng quan

GeocodingService đã được refactor để cải thiện hiệu suất, độ tin cậy và khả năng bảo trì. Service này sử dụng **OpenStreetMap Nominatim** (hoàn toàn miễn phí) để chuyển đổi địa chỉ thành tọa độ latitude và longitude.

## Tại sao chọn OpenStreetMap?

✅ **Hoàn toàn miễn phí** - Không cần thẻ tín dụng  
✅ **Không giới hạn API calls** - Không lo quota  
✅ **Dữ liệu địa lý phong phú** - Cộng đồng cập nhật liên tục  
✅ **Không cần API key** - Đơn giản, dễ sử dụng  
✅ **Tốc độ nhanh** - Phù hợp cho ứng dụng production

## Tính năng mới

### ✅ **1. Tách hàm gọi API chung**
- `callGeocodingApi()`: Gọi Google Geocoding API với timeout
- `buildGeocodingUrl()`: Xây dựng URL cho API call
- `processGeocodingResponse()`: Xử lý response từ API

### ✅ **2. Trả về DTO thay vì double[]**
- Sử dụng `GeocodingResponse` thay vì `double[]`
- Chứa thông tin đầy đủ: tọa độ, địa chỉ đã format, message
- Dễ dàng mở rộng và bảo trì

### ✅ **3. Thêm timeout + cache**
- **Timeout**: 10 giây cho mỗi request
- **Cache**: Lưu trữ kết quả trong 24 giờ
- **Cache size**: Tối đa 1000 entries
- **Cache management**: Tự động xóa entries hết hạn

### ✅ **4. Check service khi khởi động**
- Validation service khi ứng dụng khởi động
- Test service với request đơn giản
- Thông báo lỗi rõ ràng nếu service không khả dụng

## Cách sử dụng

### 1. Cấu hình (Không cần API Key!)

Không cần cấu hình gì thêm! Service sử dụng OpenStreetMap Nominatim hoàn toàn miễn phí.

**Lưu ý**: Nominatim yêu cầu User-Agent header, đã được cấu hình sẵn trong service.

### 2. Sử dụng trong Service

```java
@Service
public class MyService {
    
    @Autowired
    private GeocodingService geocodingService;
    
    public void processAddress(String address) {
        // Sử dụng method mới (khuyến nghị)
        GeocodingResponse response = geocodingService.getGeocodingResponse(address);
        
        if (response.isValid()) {
            Double lat = response.getLatitude();
            Double lng = response.getLongitude();
            String formattedAddress = response.getFormattedAddress();
            String message = response.getMessage();
            
            // Xử lý tọa độ...
        } else {
            // Xử lý lỗi
            String errorMessage = response.getMessage();
        }
        
        // Hoặc sử dụng method cũ (deprecated)
        try {
            double[] coordinates = geocodingService.getLatLngFromAddress(address);
            // Xử lý coordinates...
        } catch (AppException e) {
            // Xử lý lỗi
        }
    }
}
```

### 3. API Endpoints

#### Kiểm tra địa chỉ
```http
GET /api/houses/validate-address?address=123 Đường ABC
```

#### Lấy tọa độ
```http
GET /api/houses/geocode?address=123 Đường ABC
```

#### Xóa cache (Admin only)
```http
DELETE /api/houses/geocode/cache
```

#### Thống kê cache (Admin only)
```http
GET /api/houses/geocode/cache/stats
```

## Response Format

### GeocodingResponse
```json
{
  "isValid": true,
  "latitude": 21.0285,
  "longitude": 105.8542,
  "formattedAddress": "123 Đường ABC, Quận 1, TP.HCM, Vietnam",
  "originalAddress": "123 Đường ABC",
  "message": "Xác định vị trí thành công"
}
```

## Error Handling

Service xử lý các trường hợp lỗi sau:

- **No results**: Không tìm thấy địa chỉ
- **Invalid response format**: Định dạng dữ liệu không hợp lệ
- **Network errors**: Lỗi mạng, timeout
- **Invalid coordinates**: Tọa độ không hợp lệ

## Cache Management

### Tự động
- Cache tự động xóa entries hết hạn
- Giới hạn kích thước cache (1000 entries)
- Tự động clear cache khi đạt giới hạn

### Thủ công
```java
// Xóa toàn bộ cache
geocodingService.clearCache();

// Lấy thống kê cache
String stats = geocodingService.getCacheStats();
```

## Logging

Service sử dụng SLF4J để logging:
- **INFO**: Thông tin về API calls và kết quả thành công
- **WARN**: Cảnh báo về địa chỉ không tìm thấy
- **ERROR**: Lỗi API, network, validation
- **DEBUG**: Thông tin cache

## Performance

### Cải thiện hiệu suất:
- **Cache**: Giảm số lượng API calls
- **Timeout**: Tránh blocking quá lâu
- **Connection pooling**: Tái sử dụng HTTP connections
- **Error handling**: Graceful degradation

### Monitoring:
- Cache hit/miss ratio
- API response times
- Error rates
- Cache size và cleanup

## Frontend Integration

### Components đã tạo:
- **GoogleMap.jsx**: Component hiển thị bản đồ với OpenStreetMap + LeafletJS
- **AddressMap.jsx**: Component bản đồ cho form với khả năng chọn vị trí

### Features:
- ✅ **Tự động geocoding**: Chuyển đổi địa chỉ thành tọa độ
- ✅ **Interactive map**: Click và kéo marker để chọn vị trí
- ✅ **Search functionality**: Tìm kiếm địa chỉ trên bản đồ
- ✅ **Responsive design**: Hoạt động tốt trên mobile
- ✅ **Loading states**: Hiển thị trạng thái loading
- ✅ **Error handling**: Xử lý lỗi gracefully

### Usage:
```jsx
// Hiển thị bản đồ đơn giản
<GoogleMap
  latitude={house.latitude}
  longitude={house.longitude}
  address={house.address}
  height="400px"
/>

// Bản đồ với khả năng chọn vị trí
<AddressMap
  address={formData.address}
  latitude={formData.latitude}
  longitude={formData.longitude}
  onLocationSelect={(location) => {
    setFormData(prev => ({
      ...prev,
      latitude: location.latitude,
      longitude: location.longitude,
      address: location.address
    }));
  }}
/>
```

## Migration Guide

### Từ version cũ:
```java
// Cũ
double[] coordinates = geocodingService.getLatLngFromAddress(address);

// Mới (khuyến nghị)
GeocodingResponse response = geocodingService.getGeocodingResponse(address);
if (response.isValid()) {
    double lat = response.getLatitude();
    double lng = response.getLongitude();
}
```

### Backward Compatibility:
- Method cũ vẫn hoạt động (deprecated)
- Không breaking changes
- Có thể migrate từ từ

## Troubleshooting

### Service Issues:
1. Kiểm tra kết nối internet
2. Đảm bảo có thể truy cập `nominatim.openstreetmap.org`
3. Kiểm tra User-Agent header (đã cấu hình sẵn)

### Cache Issues:
1. Clear cache nếu cần: `DELETE /api/houses/geocode/cache`
2. Kiểm tra cache stats: `GET /api/houses/geocode/cache/stats`
3. Restart application nếu cần

### Performance Issues:
1. Kiểm tra network connectivity
2. Tăng timeout nếu cần (hiện tại 10 giây)
3. Kiểm tra cache hit ratio
4. Nominatim có rate limit 1 request/giây, cache giúp giảm thiểu vấn đề này
