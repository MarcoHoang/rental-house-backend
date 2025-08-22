-- Cập nhật tên người dùng từ ID 21 đến 35 thành tên đẹp và chuyên nghiệp
-- Đồng thời cập nhật mật khẩu với hash BCrypt mới

-- User 21: User
UPDATE users SET 
    full_name = 'Nguyễn Thị Vân Anh',
    username = 'nguyenthiuvan',
    email = 'nguyenthiuvan@gmail.com',
    phone = '0901234587',
    address = '123 Đường Nguyễn Thị Minh Khai, Quận 1, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 21;

-- User 22: Host
UPDATE users SET 
    full_name = 'Trần Văn Bảo',
    username = 'tranvanbao',
    email = 'tranvanbao@gmail.com',
    phone = '0901234588',
    address = '456 Đường Điện Biên Phủ, Quận Bình Thạnh, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 22;

-- User 23: User
UPDATE users SET 
    full_name = 'Lê Thị Cẩm Tú',
    username = 'lethicamtu',
    email = 'lethicamtu@gmail.com',
    phone = '0901234589',
    address = '789 Đường Võ Thị Sáu, Quận 3, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 23;

-- User 24: Host
UPDATE users SET 
    full_name = 'Phạm Văn Đức',
    username = 'phamvanduc',
    email = 'phamvanduc@gmail.com',
    phone = '0901234590',
    address = '321 Đường Lý Tự Trọng, Quận 1, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 24;

-- User 25: User
UPDATE users SET 
    full_name = 'Hoàng Thị Hương Giang',
    username = 'hoangthihuong',
    email = 'hoangthihuong@gmail.com',
    phone = '0901234591',
    address = '654 Đường Trần Quý Cáp, Quận 11, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 25;

-- User 26: Host
UPDATE users SET 
    full_name = 'Võ Văn Hùng',
    username = 'vovanhung',
    email = 'vovanhung@gmail.com',
    phone = '0901234592',
    address = '987 Đường Lý Thường Kiệt, Quận Tân Bình, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 26;

-- User 27: User
UPDATE users SET 
    full_name = 'Đặng Thị Kim Liên',
    username = 'dangthikimlien',
    email = 'dangthikimlien@gmail.com',
    phone = '0901234593',
    address = '147 Đường Cộng Hòa, Quận Tân Bình, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 27;

-- User 28: Host
UPDATE users SET 
    full_name = 'Bùi Văn Minh',
    username = 'buivanminh',
    email = 'buivanminh@gmail.com',
    phone = '0901234594',
    address = '258 Đường Nguyễn Văn Quỳ, Quận 7, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 28;

-- User 29: User
UPDATE users SET 
    full_name = 'Lý Thị Ngọc',
    username = 'lythingoc',
    email = 'lythingoc@gmail.com',
    phone = '0901234595',
    address = '369 Đường Nguyễn Hữu Thọ, Quận 7, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 29;

-- User 30: Host
UPDATE users SET 
    full_name = 'Trịnh Văn Phương',
    username = 'trinhvanphuong',
    email = 'trinhvanphuong@gmail.com',
    phone = '0901234596',
    address = '741 Đường Nguyễn Thị Thập, Quận 7, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 30;

-- User 31: User
UPDATE users SET 
    full_name = 'Ngô Thị Quỳnh Anh',
    username = 'ngothiquynh',
    email = 'ngothiquynh@gmail.com',
    phone = '0901234597',
    address = '852 Đường Nguyễn Văn Linh, Quận 7, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 31;

-- User 32: Host
UPDATE users SET 
    full_name = 'Đỗ Văn Sơn',
    username = 'dovanson',
    email = 'dovanson@gmail.com',
    phone = '0901234598',
    address = '963 Đường Huỳnh Tấn Phát, Quận 7, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 32;

-- User 33: User
UPDATE users SET 
    full_name = 'Vũ Thị Thanh Thảo',
    username = 'vuthithanh',
    email = 'vuthithanh@gmail.com',
    phone = '0901234599',
    address = '159 Đường Mai Chí Thọ, Quận 2, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 33;

-- User 34: Host
UPDATE users SET 
    full_name = 'Lê Văn Trung',
    username = 'levantrung',
    email = 'levantrung@gmail.com',
    phone = '0901234600',
    address = '357 Đường Võ Văn Ngân, Quận Thủ Đức, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 34;

-- User 35: User
UPDATE users SET 
    full_name = 'Phan Thị Uyên Nhi',
    username = 'phanthiuyen',
    email = 'phanthiuyen@gmail.com',
    phone = '0901234601',
    address = '753 Đường Lê Văn Việt, Quận 9, TP.HCM',
    password = '$2a$10$eFovDWPWx5HQ4urOEVbmkORfxz5S4GOpaZTZAyBo5yVGTM2Sr97wy'
WHERE id = 35;

-- Cập nhật ảnh avatar cho users từ 21-35
UPDATE users SET img = '/static/uploads/avatar/avatar21.png' WHERE id = 21;
UPDATE users SET img = '/static/uploads/avatar/avatar22.png' WHERE id = 22;
UPDATE users SET img = '/static/uploads/avatar/avatar23.png' WHERE id = 23;
UPDATE users SET img = '/static/uploads/avatar/avatar24.png' WHERE id = 24;
UPDATE users SET img = '/static/uploads/avatar/avatar25.png' WHERE id = 25;
UPDATE users SET img = '/static/uploads/avatar/avatar26.png' WHERE id = 26;
UPDATE users SET img = '/static/uploads/avatar/avatar27.png' WHERE id = 27;
UPDATE users SET img = '/static/uploads/avatar/avatar28.png' WHERE id = 28;
UPDATE users SET img = '/static/uploads/avatar/avatar29.png' WHERE id = 29;
UPDATE users SET img = '/static/uploads/avatar/avatar30.png' WHERE id = 30;
UPDATE users SET img = '/static/uploads/avatar/avatar31.png' WHERE id = 31;
UPDATE users SET img = '/static/uploads/avatar/avatar32.png' WHERE id = 32;
UPDATE users SET img = '/static/uploads/avatar/avatar33.png' WHERE id = 33;
UPDATE users SET img = '/static/uploads/avatar/avatar34.png' WHERE id = 34;
UPDATE users SET img = '/static/uploads/avatar/avatar35.png' WHERE id = 35;

-- Cập nhật thông tin hosts với CCCD/CMT thực tế (nếu có hosts từ ID 11-15)
UPDATE hosts SET 
    national_id = '112233445566',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto11.jpg'
WHERE id = 11;

UPDATE hosts SET 
    national_id = '223344556677',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto12.jpg'
WHERE id = 12;

UPDATE hosts SET 
    national_id = '334455667788',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto13.jpg'
WHERE id = 13;

UPDATE hosts SET 
    national_id = '445566778899',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto14.jpg'
WHERE id = 14;

UPDATE hosts SET 
    national_id = '556677889900',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto15.jpg'
WHERE id = 15;
