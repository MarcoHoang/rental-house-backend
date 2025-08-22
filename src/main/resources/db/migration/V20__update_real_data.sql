-- Cập nhật tên người dùng thành tên đẹp, thật tế hơn
UPDATE users SET 
    full_name = 'Nguyễn Văn An',
    username = 'nguyenvanan',
    email = 'nguyenvanan@gmail.com',
    phone = '0901234567',
    address = '123 Đường Nguyễn Huệ, Quận 1, TP.HCM',
    img = '/static/uploads/avatar/avatar1.png'
WHERE id = 1;

UPDATE users SET 
    full_name = 'Trần Thị Bình',
    username = 'tranthibinh',
    email = 'tranthibinh@gmail.com',
    phone = '0901234568',
    address = '456 Đường Lê Lợi, Quận 3, TP.HCM',
    img = '/static/uploads/avatar/avatar2.png'
WHERE id = 2;

UPDATE users SET 
    full_name = 'Lê Văn Cường',
    username = 'levancuong',
    email = 'levancuong@gmail.com',
    phone = '0901234569',
    address = '789 Đường Pasteur, Quận 1, TP.HCM',
    img = '/static/uploads/avatar/avatar3.png'
WHERE id = 3;

UPDATE users SET 
    full_name = 'Phạm Thị Dung',
    username = 'phamthidung',
    email = 'phamthidung@gmail.com',
    phone = '0901234570',
    address = '321 Đường Hai Bà Trưng, Quận 1, TP.HCM',
    img = '/static/uploads/avatar/avartar4.png'
WHERE id = 4;

UPDATE users SET 
    full_name = 'Hoàng Văn Em',
    username = 'hoangvanem',
    email = 'hoangvanem@gmail.com',
    phone = '0901234571',
    address = '654 Đường Đồng Khởi, Quận 1, TP.HCM',
    img = '/static/uploads/avatar/avatar5.png'
WHERE id = 5;

UPDATE users SET 
    full_name = 'Võ Thị Phương',
    username = 'vothiphuong',
    email = 'vothiphuong@gmail.com',
    phone = '0901234572',
    address = '987 Đường Võ Văn Tần, Quận 3, TP.HCM',
    img = '/static/uploads/avatar/avatar6.png'
WHERE id = 6;

UPDATE users SET 
    full_name = 'Đặng Văn Giang',
    username = 'dangvangiang',
    email = 'dangvangiang@gmail.com',
    phone = '0901234573',
    address = '147 Đường Nam Kỳ Khởi Nghĩa, Quận 3, TP.HCM',
    img = '/static/uploads/avatar/avartar7.png'
WHERE id = 7;

UPDATE users SET 
    full_name = 'Bùi Thị Hoa',
    username = 'buithihoa',
    email = 'buithihoa@gmail.com',
    phone = '0901234574',
    address = '258 Đường Cách Mạng Tháng 8, Quận 10, TP.HCM',
    img = '/static/uploads/avatar/avatar8.png'
WHERE id = 8;

UPDATE users SET 
    full_name = 'Lý Văn Inh',
    username = 'lyvaninh',
    email = 'lyvaninh@gmail.com',
    phone = '0901234575',
    address = '369 Đường 3/2, Quận 10, TP.HCM',
    img = '/static/uploads/avatar/avatar9.png'
WHERE id = 9;

UPDATE users SET 
    full_name = 'Trịnh Thị Kim',
    username = 'trinhthikim',
    email = 'trinhthikim@gmail.com',
    phone = '0901234576',
    address = '741 Đường Sư Vạn Hạnh, Quận 10, TP.HCM',
    img = '/static/uploads/avatar/avatar10.png'
WHERE id = 10;

-- Cập nhật ảnh nhà: sử dụng anhnha (ảnh ngoại thất) làm ảnh chính
-- Lưu ý: Ảnh nhà được lưu trong bảng house_images, không có main_image_url trong bảng houses

-- Cập nhật ảnh cho house_images: anhnha làm ảnh chính, anhnoithat làm ảnh phụ
-- House 1: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha1.png'
WHERE id = 1;

-- House 1: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat1.jpeg'
WHERE id = 2;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat2.jpeg'
WHERE id = 3;

-- House 2: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha2.jpg'
WHERE id = 4;

-- House 2: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat3.jpeg'
WHERE id = 5;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat4.jpeg'
WHERE id = 6;

-- House 3: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha3.jpg'
WHERE id = 7;

-- House 3: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat5.jpeg'
WHERE id = 8;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat6.jpeg'
WHERE id = 9;

-- House 4: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha4.jpg'
WHERE id = 10;

-- House 4: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat7.jpeg'
WHERE id = 11;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat8.jpeg'
WHERE id = 12;

-- House 5: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha5.jpeg'
WHERE id = 13;

-- House 5: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat9.jpeg'
WHERE id = 14;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat10.jpeg'
WHERE id = 15;

-- House 6: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha6.jpeg'
WHERE id = 16;

-- House 6: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat11.jpeg'
WHERE id = 17;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat12.jpeg'
WHERE id = 18;

-- House 7: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha7.jpg'
WHERE id = 19;

-- House 7: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat13.jpeg'
WHERE id = 20;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat14.jpeg'
WHERE id = 21;

-- House 8: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha8.jpeg'
WHERE id = 22;

-- House 8: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat15.jpeg'
WHERE id = 23;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat16.jpeg'
WHERE id = 24;

-- House 9: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha9.jpeg'
WHERE id = 25;

-- House 9: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat17.jpeg'
WHERE id = 26;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat18.jpeg'
WHERE id = 27;

-- House 10: Ảnh chính (anhnha)
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnha10.jpeg'
WHERE id = 28;

-- House 10: Ảnh nội thất
UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat19.jpeg'
WHERE id = 29;

UPDATE house_images SET 
    image_url = '/static/uploads/house-image/anhnoithat20.jpeg'
WHERE id = 30;

-- Cập nhật ảnh cho hosts (sử dụng ảnh thực tế nếu có)
UPDATE hosts SET 
    national_id = '/static/uploads/national-id/cccd1.jpg',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto1.jpg'
WHERE id = 1;

UPDATE hosts SET 
    national_id = '/static/uploads/national-id/cccd2.jpg',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto2.jpg'
WHERE id = 2;

UPDATE hosts SET 
    national_id = '/static/uploads/national-id/cccd3.jpg',
    proof_of_ownership_url = '/static/uploads/proof-of-ownership/giayto3.jpg'
WHERE id = 3;
