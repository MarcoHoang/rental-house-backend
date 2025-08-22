-- Cập nhật tất cả tên người dùng thành tên đẹp và chuyên nghiệp
-- Đồng thời đổi tất cả mật khẩu về 123456 (đã được mã hóa bằng BCrypt)

-- User 1: Admin
UPDATE users SET 
    full_name = 'Nguyễn Văn An',
    username = 'nguyenvanan',
    email = 'nguyenvanan@gmail.com',
    phone = '0901234567',
    address = '123 Đường Nguyễn Huệ, Quận 1, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 1;

-- User 2: Host
UPDATE users SET 
    full_name = 'Trần Thị Bình',
    username = 'tranthibinh',
    email = 'tranthibinh@gmail.com',
    phone = '0901234568',
    address = '456 Đường Lê Lợi, Quận 3, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 2;

-- User 3: User
UPDATE users SET 
    full_name = 'Lê Văn Cường',
    username = 'levancuong',
    email = 'levancuong@gmail.com',
    phone = '0901234569',
    address = '789 Đường Pasteur, Quận 1, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 3;

-- User 4: Host
UPDATE users SET 
    full_name = 'Phạm Thị Dung',
    username = 'phamthidung',
    email = 'phamthidung@gmail.com',
    phone = '0901234570',
    address = '321 Đường Hai Bà Trưng, Quận 1, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 4;

-- User 5: User
UPDATE users SET 
    full_name = 'Hoàng Văn Em',
    username = 'hoangvanem',
    email = 'hoangvanem@gmail.com',
    phone = '0901234571',
    address = '654 Đường Đồng Khởi, Quận 1, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 5;

-- User 6: Host
UPDATE users SET 
    full_name = 'Võ Thị Phương',
    username = 'vothiphuong',
    email = 'vothiphuong@gmail.com',
    phone = '0901234572',
    address = '987 Đường Võ Văn Tần, Quận 3, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 6;

-- User 7: User
UPDATE users SET 
    full_name = 'Đặng Văn Giang',
    username = 'dangvangiang',
    email = 'dangvangiang@gmail.com',
    phone = '0901234573',
    address = '147 Đường Nam Kỳ Khởi Nghĩa, Quận 3, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 7;

-- User 8: Host
UPDATE users SET 
    full_name = 'Bùi Thị Hoa',
    username = 'buithihoa',
    email = 'buithihoa@gmail.com',
    phone = '0901234574',
    address = '258 Đường Cách Mạng Tháng 8, Quận 10, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 8;

-- User 9: User
UPDATE users SET 
    full_name = 'Lý Văn Inh',
    username = 'lyvaninh',
    email = 'lyvaninh@gmail.com',
    phone = '0901234575',
    address = '369 Đường 3/2, Quận 10, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 9;

-- User 10: Host
UPDATE users SET 
    full_name = 'Trịnh Thị Kim',
    username = 'trinhthikim',
    email = 'trinhthikim@gmail.com',
    phone = '0901234576',
    address = '741 Đường Sư Vạn Hạnh, Quận 10, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 10;

-- User 11: User
UPDATE users SET 
    full_name = 'Ngô Văn Lâm',
    username = 'ngovanlam',
    email = 'ngovanlam@gmail.com',
    phone = '0901234577',
    address = '852 Đường Nguyễn Tri Phương, Quận 10, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 11;

-- User 12: Host
UPDATE users SET 
    full_name = 'Đỗ Thị Mai',
    username = 'dothimai',
    email = 'dothimai@gmail.com',
    phone = '0901234578',
    address = '963 Đường Lý Thường Kiệt, Quận 10, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 12;

-- User 13: User
UPDATE users SET 
    full_name = 'Vũ Văn Nam',
    username = 'vuvannam',
    email = 'vuvannam@gmail.com',
    phone = '0901234579',
    address = '159 Đường Trần Hưng Đạo, Quận 5, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 13;

-- User 14: Host
UPDATE users SET 
    full_name = 'Lê Thị Oanh',
    username = 'lethioanh',
    email = 'lethioanh@gmail.com',
    phone = '0901234580',
    address = '357 Đường Nguyễn Trãi, Quận 5, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 14;

-- User 15: User
UPDATE users SET 
    full_name = 'Phan Văn Phúc',
    username = 'phanvanphuc',
    email = 'phanvanphuc@gmail.com',
    phone = '0901234581',
    address = '753 Đường Trần Phú, Quận 5, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 15;

-- User 16: Host
UPDATE users SET 
    full_name = 'Hoàng Thị Quỳnh',
    username = 'hoangthiquynh',
    email = 'hoangthiquynh@gmail.com',
    phone = '0901234582',
    address = '951 Đường Nguyễn Văn Linh, Quận 7, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 16;

-- User 17: User
UPDATE users SET 
    full_name = 'Trần Văn Sơn',
    username = 'tranvanson',
    email = 'tranvanson@gmail.com',
    phone = '0901234583',
    address = '246 Đường Huỳnh Tấn Phát, Quận 7, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 17;

-- User 18: Host
UPDATE users SET 
    full_name = 'Nguyễn Thị Thảo',
    username = 'nguyenthiuthao',
    email = 'nguyenthiuthao@gmail.com',
    phone = '0901234584',
    address = '468 Đường Mai Chí Thọ, Quận 2, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 18;

-- User 19: User
UPDATE users SET 
    full_name = 'Lý Văn Thành',
    username = 'lyvanthanh',
    email = 'lyvanthanh@gmail.com',
    phone = '0901234585',
    address = '579 Đường Võ Văn Ngân, Quận Thủ Đức, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 19;

-- User 20: Host
UPDATE users SET 
    full_name = 'Đinh Thị Uyên',
    username = 'dinhthiuyen',
    email = 'dinhthiuyen@gmail.com',
    phone = '0901234586',
    address = '681 Đường Lê Văn Việt, Quận 9, TP.HCM',
    password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE id = 20;

-- Cập nhật ảnh avatar cho tất cả users
UPDATE users SET img = '/static/uploads/avatar/avatar1.png' WHERE id = 1;
UPDATE users SET img = '/static/uploads/avatar/avatar2.png' WHERE id = 2;
UPDATE users SET img = '/static/uploads/avatar/avatar3.png' WHERE id = 3;
UPDATE users SET img = '/static/uploads/avatar/avatar4.png' WHERE id = 4;
UPDATE users SET img = '/static/uploads/avatar/avatar5.png' WHERE id = 5;
UPDATE users SET img = '/static/uploads/avatar/avatar6.png' WHERE id = 6;
UPDATE users SET img = '/static/uploads/avatar/avatar7.png' WHERE id = 7;
UPDATE users SET img = '/static/uploads/avatar/avatar8.png' WHERE id = 8;
UPDATE users SET img = '/static/uploads/avatar/avatar9.png' WHERE id = 9;
UPDATE users SET img = '/static/uploads/avatar/avatar10.png' WHERE id = 10;
UPDATE users SET img = '/static/uploads/avatar/avatar11.png' WHERE id = 11;
UPDATE users SET img = '/static/uploads/avatar/avatar12.png' WHERE id = 12;
UPDATE users SET img = '/static/uploads/avatar/avatar13.png' WHERE id = 13;
UPDATE users SET img = '/static/uploads/avatar/avatar14.png' WHERE id = 14;
UPDATE users SET img = '/static/uploads/avatar/avatar15.png' WHERE id = 15;
UPDATE users SET img = '/static/uploads/avatar/avatar16.png' WHERE id = 16;
UPDATE users SET img = '/static/uploads/avatar/avatar17.png' WHERE id = 17;
UPDATE users SET img = '/static/uploads/avatar/avatar18.png' WHERE id = 18;
UPDATE users SET img = '/static/uploads/avatar/avatar19.png' WHERE id = 19;
UPDATE users SET img = '/static/uploads/avatar/avatar20.png' WHERE id = 20;

-- Cập nhật thông tin hosts với CCCD/CMT thực tế
UPDATE hosts SET 
    national_id = '123456789012',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto1.jpg'
WHERE id = 1;

UPDATE hosts SET 
    national_id = '234567890123',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto2.jpg'
WHERE id = 2;

UPDATE hosts SET 
    national_id = '345678901234',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto3.jpg'
WHERE id = 3;

UPDATE hosts SET 
    national_id = '456789012345',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto4.jpg'
WHERE id = 4;

UPDATE hosts SET 
    national_id = '567890123456',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto5.jpg'
WHERE id = 5;

UPDATE hosts SET 
    national_id = '678901234567',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto6.jpg'
WHERE id = 6;

UPDATE hosts SET 
    national_id = '789012345678',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto7.jpg'
WHERE id = 7;

UPDATE hosts SET 
    national_id = '890123456789',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto8.jpg'
WHERE id = 8;

UPDATE hosts SET 
    national_id = '901234567890',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto9.jpg'
WHERE id = 9;

UPDATE hosts SET 
    national_id = '012345678901',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto10.jpg'
WHERE id = 10;
