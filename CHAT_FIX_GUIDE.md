# 🔧 Hướng dẫn khắc phục lỗi Chat

## 🚨 **Các lỗi đã được sửa:**

### **1. Lỗi Authentication (500 Internal Server Error)**
- **Nguyên nhân:** JWT token không được parse đúng cách để lấy user ID
- **Giải pháp:** 
  - Sửa `JwtTokenFilter` để set claims làm principal thay vì UserDetails
  - Thêm fallback để lấy user ID từ email nếu không lấy được từ claims
  - Thêm method `getUserIdByEmail()` trong `ChatService`

### **2. Lỗi Database Migration**
- **Nguyên nhân:** File migration V1 bị trùng lặp với V2
- **Giải pháp:** Xóa file V1, giữ lại V2 với cấu trúc đầy đủ hơn

### **3. Lỗi Repository Methods**
- **Nguyên nhân:** Thiếu method `countByConversationIdAndReceiverIdAndIsReadFalse`
- **Giải pháp:** Thêm method vào `MessageRepository`

## 🛠️ **Các thay đổi đã thực hiện:**

### **Backend Changes:**
1. **ChatController.java:**
   - Sửa method `getCurrentUserId()` để xử lý JWT claims đúng cách
   - Thêm fallback để lấy user ID từ email

2. **JwtTokenFilter.java:**
   - Set claims làm principal thay vì UserDetails

3. **ChatService.java:**
   - Thêm method `getUserIdByEmail(String email)`

4. **ChatServiceImpl.java:**
   - Implement method `getUserIdByEmail()`

5. **MessageRepository.java:**
   - Thêm method `countByConversationIdAndReceiverIdAndIsReadFalse()`

6. **Database Migration:**
   - Xóa V1__create_chat_tables.sql (trùng lặp)
   - Giữ lại V2__create_chat_tables.sql

## 🧪 **Cách test:**

### **1. Restart Backend:**
```bash
cd rental-house-backend
pkill -f "spring-boot:run"
./mvnw spring-boot:run
```

### **2. Test với script:**
```bash
chmod +x test-chat.sh
./test-chat.sh
```

### **3. Test thủ công:**
```bash
# 1. Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123456","fullName":"Test User","phone":"0123456789","username":"testuser"}'

# 2. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"123456"}'

# 3. Create conversation (sử dụng token từ login)
curl -X POST http://localhost:8080/api/chat/conversations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"hostId":3,"houseId":24}'
```

## ✅ **Kết quả mong đợi:**

- ✅ Không còn lỗi 500 Internal Server Error
- ✅ Không còn lỗi "Invalid user ID format"
- ✅ Chat conversation được tạo thành công
- ✅ Tin nhắn được gửi thành công
- ✅ Frontend hiển thị chat bình thường

## 🔍 **Debug nếu vẫn có lỗi:**

### **1. Kiểm tra logs:**
```bash
tail -f logs/rental-house.log
```

### **2. Kiểm tra database:**
```sql
-- Kiểm tra bảng đã được tạo
SHOW TABLES LIKE '%chat%';

-- Kiểm tra dữ liệu
SELECT * FROM conversations;
SELECT * FROM messages;
```

### **3. Kiểm tra JWT token:**
- Decode token tại https://jwt.io
- Kiểm tra có field "id" trong payload không

## 🚀 **Deploy:**

Sau khi test thành công, có thể deploy bình thường. Các thay đổi không ảnh hưởng đến tính năng khác. 