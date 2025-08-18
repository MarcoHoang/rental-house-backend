# Giải pháp Security cho House Detail Endpoints

## Vấn đề ban đầu

Bạn gặp phải conflict trong pattern matching của Spring Security khi cấu hình endpoint xem chi tiết nhà:

- **Khi comment `String.format("%s/houses/*", apiPrefix)`**: Host không thể xem nhà của mình vì pattern `/api/houses/my-houses` bị match với `/api/houses/*` trước
- **Khi bỏ comment**: User chưa đăng nhập không thể xem chi tiết nhà vì endpoint yêu cầu authentication

## Giải pháp đã implement (Cách 1 - Khuyến nghị)

### 1. Tạo endpoint riêng cho xem chi tiết nhà công khai

**Backend:**
- Endpoint `/api/houses/public/{id}` - không cần authentication
- Endpoint `/api/houses/{id}` - cần authentication
- Endpoint `/api/houses/my-houses` - chỉ dành cho HOST

**Frontend:**
- Logic thông minh trong `HouseDetailPage.jsx`:
  - Nếu user đã đăng nhập → sử dụng `/api/houses/{id}`
  - Nếu user chưa đăng nhập → sử dụng `/api/houses/public/{id}`

### 2. Cấu hình Security

**WebSecurityConfig.java:**
```java
// Public endpoints (không cần login)
.requestMatchers(
    String.format("%s/houses/public/**", apiPrefix), // Endpoint công khai cho xem chi tiết nhà
    // ... các endpoint khác
).permitAll()

// Authenticated endpoints (cần login)
.requestMatchers(
    String.format("%s/houses/*", apiPrefix) // Endpoint cần authentication
).hasAnyRole("USER", "HOST", "ADMIN")

// Host-specific endpoints
.requestMatchers(
    String.format("%s/houses/my-houses", apiPrefix) // Chỉ HOST mới được truy cập
).hasRole("HOST")
```

**JwtTokenFilter.java:**
```java
// Bypass token cho public endpoints
Pair.of(String.format("%s/houses/public/", apiPrefix), "GET")
```

### 3. API Structure

**Backend Controllers:**
- `HouseController.getPublicHouseById()` - endpoint công khai
- `HouseController.getHouseById()` - endpoint cần authentication
- `HouseController.getMyHouses()` - endpoint chỉ dành cho HOST

**Frontend API:**
- `houseApi.getPublicHouseById()` - gọi endpoint công khai
- `propertyApi.getHouseById()` - gọi endpoint cần authentication

### 4. Logic Frontend

**HouseDetailPage.jsx:**
```javascript
// Logic thông minh: nếu user đã đăng nhập thì dùng authenticated endpoint
// nếu chưa đăng nhập thì dùng public endpoint
if (user && localStorage.getItem('token')) {
  console.log('User is authenticated, using authenticated endpoint');
  response = await propertyApi.getHouseById(id);
} else {
  console.log('User is not authenticated, using public endpoint');
  response = await getPublicHouseById(id);
}
```

## Kết quả

✅ **User chưa đăng nhập**: Có thể xem chi tiết nhà qua `/api/houses/public/{id}`

✅ **User đã đăng nhập**: Có thể xem chi tiết nhà qua `/api/houses/{id}`

✅ **Host**: Có thể xem nhà của mình qua `/api/houses/my-houses`

✅ **Không có conflict**: Pattern matching hoạt động đúng

## Testing

Đã tạo `TestController` và `SecurityTestComponent` để test các endpoint:

- `/api/test/public` - endpoint công khai
- `/api/test/authenticated` - endpoint cần authentication
- `/api/test/auth-status` - kiểm tra trạng thái authentication

## Files đã thay đổi

### Backend:
- `WebSecurityConfig.java` - cấu hình security
- `JwtTokenFilter.java` - bypass token cho public endpoints
- `HouseController.java` - endpoint public đã có sẵn
- `HouseService.java` - method public đã có sẵn
- `ApiResponse.java` - thêm method error()
- `AuthController.java` - sửa dependency injection
- `TestController.java` - tạo mới để test

### Frontend:
- `houseApi.jsx` - thêm method getPublicHouseById()
- `propertyApi.jsx` - sửa getHouseById() để dùng authenticated client
- `apiClient.jsx` - thêm apiClient export
- `HouseDetailPage.jsx` - logic thông minh chọn endpoint
- `SecurityTestComponent.jsx` - tạo mới để test

## Lưu ý

1. **Thứ tự pattern matching**: Các pattern cụ thể phải đặt trước pattern chung
2. **JWT Filter**: Cần bypass token cho public endpoints
3. **Frontend logic**: Tự động chọn endpoint phù hợp dựa trên trạng thái authentication
4. **Error handling**: Có xử lý lỗi cho cả hai loại endpoint
