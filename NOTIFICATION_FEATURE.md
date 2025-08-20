# Tính năng Thông báo - Notification Feature

## Tổng quan
Hệ thống thông báo đã được cập nhật để hỗ trợ các loại thông báo mới theo yêu cầu từ Sprint 3:

### Các loại thông báo mới:
1. **RENTAL_REQUEST** - Thông báo cho host khi có khách đặt thuê nhà
2. **RENTAL_CANCELED** - Thông báo cho host khi khách hủy thuê nhà  
3. **REVIEW_ONE_STAR** - Thông báo cho host khi có đánh giá 1 sao

## Cấu trúc Database

### Bảng `notifications`
```sql
ALTER TABLE notifications 
ADD COLUMN house_id BIGINT,
ADD COLUMN review_id BIGINT,
ADD CONSTRAINT fk_notifications_house FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE,
ADD CONSTRAINT fk_notifications_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE;
```

### Enum `Type` mới:
```java
public enum Type {
    RENTAL_REQUEST,      // Thông báo cho host khi có yêu cầu thuê mới
    RENTAL_APPROVED,     // Thông báo cho user khi yêu cầu được chấp nhận
    RENTAL_REJECTED,     // Thông báo cho user khi yêu cầu bị từ chối
    RENTAL_CANCELED,     // Thông báo cho host khi khách hủy thuê
    HOUSE_DELETED,       // Thông báo cho host khi nhà bị xóa
    REVIEW_ONE_STAR,     // Thông báo cho host khi có đánh giá 1 sao
    GENERAL             // Thông báo chung
}
```

## API Endpoints

### 1. Lấy thông báo của user hiện tại
```
GET /api/notifications/my-notifications
```

### 2. Lấy thông báo của user theo ID
```
GET /api/notifications/user/{userId}
```

### 3. Đánh dấu thông báo đã đọc
```
PUT /api/notifications/{id}/read
```

### 4. Xóa thông báo
```
DELETE /api/notifications/{id}
```

### 5. Tạo thông báo mới
```
POST /api/notifications
```

## Service Methods

### NotificationService Interface
```java
// Thông báo cho host khi có khách đặt thuê
void createRentalRequestNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId);

// Thông báo cho host khi khách hủy thuê
void createRentalCanceledNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId);

// Thông báo cho host khi có đánh giá 1 sao
void createReviewOneStarNotification(Long hostId, String userName, String houseName, Long reviewId, Long houseId);
```

## Tích hợp với các Service khác

### 1. RentalService
- Tự động tạo thông báo khi có đơn đặt thuê mới
- Tự động tạo thông báo khi khách hủy thuê

### 2. ReviewService  
- Tự động tạo thông báo khi có đánh giá 1 sao

## Frontend Components

### 1. NotificationBell
- Hiển thị icon chuông với badge số thông báo chưa đọc
- Polling tự động mỗi 10 giây để cập nhật thông báo mới
- Hiển thị toast notification khi có thông báo mới

### 2. NotificationList
- Hiển thị danh sách thông báo dạng dropdown
- Hỗ trợ đánh dấu đã đọc và xóa thông báo
- Hiển thị icon và label phù hợp cho từng loại thông báo

## Cách sử dụng

### 1. Backend
```java
// Trong service method
@Autowired
private NotificationService notificationService;

// Tạo thông báo khi có đơn đặt thuê mới
notificationService.createRentalRequestNotification(
    house.getHost().getId(),
    renter.getFullName(),
    house.getTitle(),
    rental.getId(),
    house.getId()
);
```

### 2. Frontend
```jsx
// Import component
import NotificationBell from '../components/common/NotificationBell';

// Sử dụng trong layout
<NotificationBell />
```

## Migration
Chạy migration để cập nhật database:
```sql
-- File: V3__add_notification_fields.sql
ALTER TABLE notifications 
ADD COLUMN house_id BIGINT,
ADD COLUMN review_id BIGINT,
ADD CONSTRAINT fk_notifications_house FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE,
ADD CONSTRAINT fk_notifications_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE;
```

## Lưu ý
- Thông báo được sắp xếp theo thời gian mới nhất lên trước
- Hệ thống tự động tạo thông báo khi có các sự kiện tương ứng
- Frontend polling mỗi 10 giây để cập nhật thông báo mới
- Toast notification hiển thị khi có thông báo mới trong 1 phút gần đây
