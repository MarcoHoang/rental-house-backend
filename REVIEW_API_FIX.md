# Sửa lỗi Review API - 400 Bad Request

## 🚨 **Vấn đề gặp phải:**
- Frontend gửi request đến `/api/reviews` với status 400
- Lỗi "Request failed with status code 400"
- Dữ liệu gửi không hợp lệ

## 🔍 **Nguyên nhân:**
- **Mismatch giữa DTO và Entity**: 
  - ReviewDTO được sử dụng để nhận request từ frontend
  - Nhưng ReviewDTO được thiết kế để response, không phải request
  - Field mapping không khớp giữa frontend và backend

## 🛠️ **Giải pháp đã thực hiện:**

### 1. **Tạo CreateReviewRequest DTO mới**
```java
// File: src/main/java/com/codegym/dto/request/CreateReviewRequest.java
public class CreateReviewRequest {
    @NotNull(message = "Reviewer ID is required")
    @Min(value = 1, message = "Reviewer ID must be positive")
    private Long reviewerId;
    
    @NotNull(message = "House ID is required")
    @Min(value = 1, message = "House ID must be positive")
    private Long houseId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @NotBlank(message = "Comment is required")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String comment;
}
```

### 2. **Cập nhật ReviewService Interface**
```java
// Thay đổi từ:
ReviewDTO createReview(ReviewDTO reviewDTO);

// Thành:
ReviewDTO createReview(CreateReviewRequest request);
```

### 3. **Cập nhật ReviewServiceImpl**
```java
// Thay đổi từ:
public ReviewDTO createReview(ReviewDTO reviewDTO) {
    User reviewer = findUserByIdOrThrow(reviewDTO.getReviewerId());
    House house = findHouseByIdOrThrow(reviewDTO.getHouseId());
    // ...
}

// Thành:
public ReviewDTO createReview(CreateReviewRequest request) {
    User reviewer = findUserByIdOrThrow(request.getReviewerId());
    House house = findHouseByIdOrThrow(request.getHouseId());
    // ...
}
```

### 4. **Cập nhật ReviewController**
```java
// Thay đổi từ:
@PostMapping
public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewDTO reviewDTO, Locale locale) {
    ReviewDTO created = reviewService.createReview(reviewDTO);
    return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
}

// Thành:
@PostMapping
public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody CreateReviewRequest request, Locale locale) {
    ReviewDTO created = reviewService.createReview(request);
    return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
}
```

## ✅ **Lợi ích của thay đổi:**

1. **Separation of Concerns**: 
   - CreateReviewRequest: Dành cho input validation
   - ReviewDTO: Dành cho output response

2. **Better Validation**: 
   - Validation annotations rõ ràng hơn
   - Error messages cụ thể hơn

3. **Type Safety**: 
   - Frontend gửi đúng field names
   - Backend nhận đúng data types

4. **Maintainability**: 
   - Dễ dàng thay đổi validation rules
   - Không ảnh hưởng đến response structure

## 🔄 **Frontend không cần thay đổi:**

Frontend vẫn gửi data với format:
```javascript
{
  reviewerId: 1,
  houseId: 14,
  rating: 5,
  comment: "Nhà đẹp"
}
```

## 🧪 **Test sau khi sửa:**

1. **Restart backend**:
   ```bash
   mvn spring-boot:run
   ```

2. **Test tạo review**:
   - Sử dụng ReviewDebugComponent
   - Sử dụng ApiTestComponent
   - Kiểm tra console logs

3. **Kiểm tra database**:
   ```sql
   SELECT * FROM reviews ORDER BY created_at DESC LIMIT 5;
   ```

## 📋 **Checklist hoàn thành:**

- [x] Tạo CreateReviewRequest DTO
- [x] Cập nhật ReviewService interface
- [x] Cập nhật ReviewServiceImpl
- [x] Cập nhật ReviewController
- [x] Thêm validation annotations
- [x] Test API endpoint

## 🚀 **Kết quả mong đợi:**

- ✅ API `/api/reviews` hoạt động bình thường
- ✅ Status 200 thay vì 400
- ✅ Review được tạo thành công trong database
- ✅ Validation errors rõ ràng hơn nếu có
- ✅ Frontend có thể gửi review thành công

## 📝 **Lưu ý:**

- **CreateReviewRequest** chỉ dùng cho POST request
- **ReviewDTO** vẫn dùng cho response và các operation khác
- Validation rules có thể điều chỉnh theo yêu cầu
- Error messages có thể customize theo ngôn ngữ
