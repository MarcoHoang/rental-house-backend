-- V24__distribute_houses_nationwide.sql
-- Phân bố các căn nhà trên khắp các tỉnh thành trên cả nước

-- Cập nhật vị trí các căn nhà để phân bố trên toàn quốc

-- 1. TP.HCM - Giữ lại một số nhà ở trung tâm
UPDATE houses SET 
    address = '123 Nguyễn Huệ, Quận 1, TP.HCM',
    latitude = 10.7769,
    longitude = 106.7009
WHERE id = 1;

UPDATE houses SET 
    address = '456 Võ Văn Tần, Quận 3, TP.HCM',
    latitude = 10.7826,
    longitude = 106.6872
WHERE id = 2;

-- 2. Hà Nội
UPDATE houses SET 
    address = '789 Tràng Tiền, Hoàn Kiếm, Hà Nội',
    latitude = 21.0285,
    longitude = 105.8542,
    title = 'Căn hộ cao cấp Hoàn Kiếm',
    description = 'Căn hộ 2 phòng ngủ, view hồ Hoàn Kiếm, tiện nghi đầy đủ, gần trung tâm'
WHERE id = 3;

UPDATE houses SET 
    address = '321 Đường Láng, Đống Đa, Hà Nội',
    latitude = 21.0278,
    longitude = 105.8342,
    title = 'Villa Đống Đa',
    description = 'Villa 4 phòng ngủ, hồ bơi riêng, sân vườn rộng, gần trường đại học'
WHERE id = 4;

-- 3. Đà Nẵng
UPDATE houses SET 
    address = '654 Nguyễn Văn Linh, Hải Châu, Đà Nẵng',
    latitude = 16.0544,
    longitude = 108.2022,
    title = 'Townhouse Hải Châu',
    description = 'Townhouse 3 tầng, 3 phòng ngủ, có thang máy, gần biển Mỹ Khê'
WHERE id = 5;

UPDATE houses SET 
    address = '987 Trần Phú, Hải Châu, Đà Nẵng',
    latitude = 16.0479,
    longitude = 108.2064,
    title = 'Căn hộ biển Đà Nẵng',
    description = 'Căn hộ 2 phòng ngủ, view biển, gần bãi biển Mỹ Khê'
WHERE id = 6;

-- 4. Cần Thơ
UPDATE houses SET 
    address = '147 Nguyễn Văn Cừ, Ninh Kiều, Cần Thơ',
    latitude = 10.0452,
    longitude = 105.7469,
    title = 'Nhà trọ sinh viên Cần Thơ',
    description = 'Nhà trọ 1 phòng, có điều hòa, wifi, bếp riêng, gần trường đại học'
WHERE id = 7;

-- 5. Bình Dương
UPDATE houses SET 
    address = '258 Đại lộ Bình Dương, Thủ Dầu Một, Bình Dương',
    latitude = 11.0041,
    longitude = 106.6358,
    title = 'Nhà trọ cao cấp Bình Dương',
    description = 'Nhà trọ 1 phòng, tiện nghi đầy đủ, an ninh 24/7, gần khu công nghiệp'
WHERE id = 8;

-- 6. Khánh Hòa (Nha Trang)
UPDATE houses SET 
    address = '369 Trần Phú, Nha Trang, Khánh Hòa',
    latitude = 12.2388,
    longitude = 109.1967,
    title = 'Nhà phố Nha Trang',
    description = 'Nhà phố 3 tầng, 5 phòng ngủ, có gara, gần biển Nha Trang'
WHERE id = 9;

UPDATE houses SET 
    address = '741 Nguyễn Thị Minh Khai, Nha Trang, Khánh Hòa',
    latitude = 12.2494,
    longitude = 109.1903,
    title = 'Villa biển Nha Trang',
    description = 'Villa 4 phòng ngủ, view biển, sân vườn rộng, gần Vinpearl'
WHERE id = 10;

-- 7. Lâm Đồng (Đà Lạt)
UPDATE houses SET 
    address = '123 Nguyễn Văn Trỗi, Đà Lạt, Lâm Đồng',
    latitude = 11.9404,
    longitude = 108.4583,
    title = 'Căn hộ Đà Lạt',
    description = 'Căn hộ 2 phòng ngủ, view núi, khí hậu mát mẻ, gần chợ Đà Lạt'
WHERE id = 11;

UPDATE houses SET 
    address = '456 Trần Hưng Đạo, Đà Lạt, Lâm Đồng',
    latitude = 11.9465,
    longitude = 108.4419,
    title = 'Villa Đà Lạt',
    description = 'Villa 3 phòng ngủ, view hồ Xuân Hương, vườn hoa, khí hậu mát mẻ'
WHERE id = 12;

-- 8. Bà Rịa - Vũng Tàu
UPDATE houses SET 
    address = '789 Trần Phú, Vũng Tàu, Bà Rịa - Vũng Tàu',
    latitude = 10.3459,
    longitude = 107.0842,
    title = 'Villa biển Vũng Tàu',
    description = 'Villa 5 phòng ngủ, hồ bơi riêng, view biển, gần bãi Sau'
WHERE id = 13;

UPDATE houses SET 
    address = '321 Thống Nhất, Vũng Tàu, Bà Rịa - Vũng Tàu',
    latitude = 10.3564,
    longitude = 107.0842,
    title = 'Căn hộ biển Vũng Tàu',
    description = 'Căn hộ 3 phòng ngủ, view toàn cảnh biển, tiện nghi 5 sao'
WHERE id = 14;

-- 9. Đồng Nai
UPDATE houses SET 
    address = '654 Nguyễn Văn Trị, Biên Hòa, Đồng Nai',
    latitude = 10.9574,
    longitude = 106.8426,
    title = 'Townhouse Biên Hòa',
    description = 'Townhouse 4 tầng, 4 phòng ngủ, có thang máy, gần khu công nghiệp'
WHERE id = 15;

UPDATE houses SET 
    address = '987 Quốc lộ 1A, Biên Hòa, Đồng Nai',
    latitude = 10.9513,
    longitude = 106.8243,
    title = 'Nhà phố Biên Hòa',
    description = 'Nhà phố 3 tầng, 3 phòng ngủ, gần trung tâm thương mại'
WHERE id = 16;

-- 10. Long An
UPDATE houses SET 
    address = '147 Nguyễn Văn Tạo, Tân An, Long An',
    latitude = 10.5333,
    longitude = 106.4167,
    title = 'Nhà trọ Long An',
    description = 'Nhà trọ 1 phòng, có điều hòa, wifi, bếp riêng, gần chợ Tân An'
WHERE id = 17;

-- 11. Tiền Giang
UPDATE houses SET 
    address = '258 Trưng Trắc, Mỹ Tho, Tiền Giang',
    latitude = 10.3623,
    longitude = 106.3621,
    title = 'Nhà trọ Mỹ Tho',
    description = 'Nhà trọ 1 phòng, gần trường đại học, giá sinh viên, gần chợ Mỹ Tho'
WHERE id = 18;

-- 12. An Giang
UPDATE houses SET 
    address = '963 Nguyễn Văn Thoại, Long Xuyên, An Giang',
    latitude = 10.3865,
    longitude = 105.4352,
    title = 'Nhà phố Long Xuyên',
    description = 'Nhà phố 3 tầng, 5 phòng ngủ, có gara, gần trung tâm Long Xuyên'
WHERE id = 19;

UPDATE houses SET 
    address = '741 Trần Hưng Đạo, Châu Đốc, An Giang',
    latitude = 10.7041,
    longitude = 105.1168,
    title = 'Nhà phố Châu Đốc',
    description = 'Nhà phố 2 tầng, 3 phòng ngủ, gần chợ Châu Đốc, gần núi Sam'
WHERE id = 20;

-- Cập nhật giá thuê phù hợp với từng địa phương
UPDATE houses SET price = 12000000 WHERE id = 3; -- Hà Nội
UPDATE houses SET price = 28000000 WHERE id = 4; -- Hà Nội
UPDATE houses SET price = 15000000 WHERE id = 5; -- Đà Nẵng
UPDATE houses SET price = 12000000 WHERE id = 6; -- Đà Nẵng
UPDATE houses SET price = 3000000 WHERE id = 7; -- Cần Thơ
UPDATE houses SET price = 4000000 WHERE id = 8; -- Bình Dương
UPDATE houses SET price = 25000000 WHERE id = 9; -- Nha Trang
UPDATE houses SET price = 35000000 WHERE id = 10; -- Nha Trang
UPDATE houses SET price = 10000000 WHERE id = 11; -- Đà Lạt
UPDATE houses SET price = 25000000 WHERE id = 12; -- Đà Lạt
UPDATE houses SET price = 40000000 WHERE id = 13; -- Vũng Tàu
UPDATE houses SET price = 20000000 WHERE id = 14; -- Vũng Tàu
UPDATE houses SET price = 18000000 WHERE id = 15; -- Đồng Nai
UPDATE houses SET price = 20000000 WHERE id = 16; -- Đồng Nai
UPDATE houses SET price = 2500000 WHERE id = 17; -- Long An
UPDATE houses SET price = 2000000 WHERE id = 18; -- Tiền Giang
UPDATE houses SET price = 18000000 WHERE id = 19; -- An Giang
UPDATE houses SET price = 15000000 WHERE id = 20; -- An Giang
