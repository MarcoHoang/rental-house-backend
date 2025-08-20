# Host Statistics API

## Endpoints

### 1. Lấy thống kê cho host hiện tại
```
GET /api/hosts/my-statistics?period={period}
```

**Parameters:**
- `period` (optional): Kỳ thời gian
  - `current_month` (default): Tháng này
  - `last_month`: Tháng trước
  - `last_3_months`: 3 tháng gần đây
  - `last_6_months`: 6 tháng gần đây
  - `current_year`: Năm nay

**Response:**
```json
{
  "success": true,
  "data": {
    "hostId": 1,
    "period": "current_month",
    "totalRentals": 15,
    "totalRevenue": 45000000,
    "netRevenue": 36450000,
    "occupancyRate": 85.0,
    "revenueChange": 5000000,
    "revenueChangePercentage": 12.5,
    "rentalChange": 3,
    "rentalChangePercentage": 25.0,
    "topHouses": [...],
    "leastRentedHouses": [...],
    "monthlyTrend": [...],
    "taxAmount": 4500000,
    "platformFee": 4500000,
    "totalDeductions": 9000000
  },
  "message": "success.general"
}
```

### 2. Lấy thống kê cho host cụ thể (Admin only)
```
GET /api/hosts/{id}/statistics?period={period}
```

## Authentication
Tất cả endpoints đều yêu cầu JWT token trong header:
```
Authorization: Bearer <JWT_TOKEN>
```

## Test
Sử dụng file `test-api.http` để test API với các IDE hỗ trợ HTTP client (VS Code, IntelliJ IDEA, etc.)

## Notes
- Thuế thu nhập: 10% trên doanh thu
- Phí sàn: 10% trên doanh thu
- Doanh thu thực nhận = Doanh thu gốc × 0.8
