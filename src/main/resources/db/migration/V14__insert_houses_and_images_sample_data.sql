-- V14__insert_houses_and_images_sample_data.sql
-- Insert sample data for houses and house_images tables

-- Step 1: Insert Houses for each host (host_id 4-13 from V13)
INSERT INTO houses (host_id, title, description, address, price, area, latitude, longitude, status, house_type, created_at, updated_at) VALUES
-- Host 1 (user_id = 4) - Premium Apartments
(4, 'Căn hộ cao cấp Quận 1', 'Căn hộ 2 phòng ngủ, view thành phố, tiện nghi đầy đủ, gần trung tâm', '123 Nguyễn Huệ, Quận 1, TP.HCM', 15000000, 80, 10.7769, 106.7009, 'AVAILABLE', 'APARTMENT', NOW(), NOW()),
(4, 'Studio Quận 3', 'Studio 1 phòng, thiết kế hiện đại, phù hợp cho sinh viên hoặc người đi làm', '456 Võ Văn Tần, Quận 3, TP.HCM', 8000000, 45, 10.7826, 106.6872, 'AVAILABLE', 'APARTMENT', NOW(), NOW()),

-- Host 2 (user_id = 5) - Luxury Villas
(5, 'Villa Quận 2', 'Villa 4 phòng ngủ, hồ bơi riêng, sân vườn rộng, view sông', '789 Thảo Điền, Quận 2, TP.HCM', 35000000, 250, 10.7870, 106.7498, 'AVAILABLE', 'VILLA', NOW(), NOW()),
(5, 'Villa Quận 9', 'Villa 5 phòng ngủ, hồ bơi, gara 3 xe, tiện nghi 5 sao', '321 Phú Nhuận, Quận 9, TP.HCM', 45000000, 300, 10.7951, 106.7219, 'AVAILABLE', 'VILLA', NOW(), NOW()),

-- Host 3 (user_id = 6) - Townhouses
(6, 'Townhouse Quận 5', 'Townhouse 3 tầng, 3 phòng ngủ, có thang máy, gần chợ', '654 Trần Hưng Đạo, Quận 5, TP.HCM', 18000000, 150, 10.7540, 106.6625, 'AVAILABLE', 'TOWNHOUSE', NOW(), NOW()),
(6, 'Townhouse Quận 7', 'Townhouse 4 tầng, 4 phòng ngủ, gần Crescent Mall', '987 Nguyễn Thị Thập, Quận 7, TP.HCM', 22000000, 180, 10.7326, 106.7227, 'AVAILABLE', 'TOWNHOUSE', NOW(), NOW()),

-- Host 4 (user_id = 7) - Boarding Houses
(7, 'Nhà trọ sinh viên Quận 10', 'Nhà trọ 1 phòng, có điều hòa, wifi, bếp riêng, gần trường đại học', '147 Sư Vạn Hạnh, Quận 10, TP.HCM', 3500000, 25, 10.7626, 106.6602, 'AVAILABLE', 'BOARDING_HOUSE', NOW(), NOW()),
(7, 'Nhà trọ cao cấp Quận 3', 'Nhà trọ 1 phòng, tiện nghi đầy đủ, an ninh 24/7', '258 Võ Văn Tần, Quận 3, TP.HCM', 5000000, 30, 10.7826, 106.6872, 'AVAILABLE', 'BOARDING_HOUSE', NOW(), NOW()),

-- Host 5 (user_id = 8) - Whole Houses
(8, 'Nhà phố Quận 1', 'Nhà phố 3 tầng, 5 phòng ngủ, có gara, gần trung tâm thương mại', '369 Đồng Khởi, Quận 1, TP.HCM', 30000000, 200, 10.7769, 106.7009, 'AVAILABLE', 'WHOLE_HOUSE', NOW(), NOW()),
(8, 'Nhà phố Quận 5', 'Nhà phố 2 tầng, 3 phòng ngủ, gần chợ Bến Thành', '741 Trần Hưng Đạo, Quận 5, TP.HCM', 25000000, 150, 10.7540, 106.6625, 'AVAILABLE', 'WHOLE_HOUSE', NOW(), NOW()),

-- Host 6 (user_id = 9) - Premium Apartments
(9, 'Căn hộ Landmark 81', 'Căn hộ 3 phòng ngủ, view toàn cảnh thành phố, tiện nghi 5 sao', '123 Vinhomes Central Park, Quận 1, TP.HCM', 25000000, 120, 10.7951, 106.7219, 'AVAILABLE', 'APARTMENT', NOW(), NOW()),
(9, 'Căn hộ The Manor', 'Căn hộ 2 phòng ngủ, gần trung tâm thương mại, an ninh 24/7', '456 Mỹ Đình, Quận 2, TP.HCM', 18000000, 85, 10.7870, 106.7498, 'AVAILABLE', 'APARTMENT', NOW(), NOW()),

-- Host 7 (user_id = 10) - Luxury Villas
(10, 'Villa Phú Nhuận', 'Villa 5 phòng ngủ, hồ bơi riêng, vườn cảnh, gara 3 xe', '789 Phú Nhuận, Quận 1, TP.HCM', 45000000, 300, 10.7951, 106.7219, 'AVAILABLE', 'VILLA', NOW(), NOW()),
(10, 'Villa Thảo Điền', 'Villa 4 phòng ngủ, view sông, sân vườn rộng', '321 Thảo Điền, Quận 2, TP.HCM', 38000000, 250, 10.7870, 106.7498, 'AVAILABLE', 'VILLA', NOW(), NOW()),

-- Host 8 (user_id = 11) - Townhouses
(11, 'Townhouse Quận 7', 'Townhouse 4 tầng, 4 phòng ngủ, có thang máy', '654 Nguyễn Thị Thập, Quận 7, TP.HCM', 22000000, 180, 10.7326, 106.7227, 'AVAILABLE', 'TOWNHOUSE', NOW(), NOW()),
(11, 'Townhouse Crescent', 'Townhouse 3 tầng, 3 phòng ngủ, gần Crescent Mall', '987 Nguyễn Văn Linh, Quận 7, TP.HCM', 20000000, 150, 10.7326, 106.7227, 'AVAILABLE', 'TOWNHOUSE', NOW(), NOW()),

-- Host 9 (user_id = 12) - Boarding Houses
(12, 'Nhà trọ cao cấp Quận 3', 'Nhà trọ 1 phòng, có điều hòa, wifi, bếp riêng', '147 Võ Văn Tần, Quận 3, TP.HCM', 5000000, 30, 10.7826, 106.6872, 'AVAILABLE', 'BOARDING_HOUSE', NOW(), NOW()),
(12, 'Nhà trọ sinh viên Quận 10', 'Nhà trọ 1 phòng, gần trường đại học, giá sinh viên', '258 Sư Vạn Hạnh, Quận 10, TP.HCM', 3500000, 25, 10.7626, 106.6602, 'AVAILABLE', 'BOARDING_HOUSE', NOW(), NOW()),

-- Host 10 (user_id = 13) - Whole Houses
(13, 'Nhà phố Quận 1', 'Nhà phố 3 tầng, 5 phòng ngủ, có gara, gần trung tâm', '963 Đồng Khởi, Quận 1, TP.HCM', 30000000, 200, 10.7769, 106.7009, 'AVAILABLE', 'WHOLE_HOUSE', NOW(), NOW()),
(13, 'Nhà phố Quận 5', 'Nhà phố 2 tầng, 3 phòng ngủ, gần chợ Bến Thành', '741 Trần Hưng Đạo, Quận 5, TP.HCM', 25000000, 150, 10.7540, 106.6625, 'AVAILABLE', 'WHOLE_HOUSE', NOW(), NOW());

-- Step 2: Insert House Images for each house
INSERT INTO house_images (house_id, image_url, sort_order, created_at, updated_at) VALUES
-- House 1 (Căn hộ cao cấp Quận 1)
(1, '/uploads/houses/house1_main.jpg', 1, NOW(), NOW()),
(1, '/uploads/houses/house1_living.jpg', 2, NOW(), NOW()),
(1, '/uploads/houses/house1_bedroom.jpg', 3, NOW(), NOW()),
(1, '/uploads/houses/house1_bathroom.jpg', 4, NOW(), NOW()),

-- House 2 (Studio Quận 3)
(2, '/uploads/houses/house2_main.jpg', 1, NOW(), NOW()),
(2, '/uploads/houses/house2_interior.jpg', 2, NOW(), NOW()),
(2, '/uploads/houses/house2_bathroom.jpg', 3, NOW(), NOW()),

-- House 3 (Villa Quận 2)
(3, '/uploads/houses/house3_main.jpg', 1, NOW(), NOW()),
(3, '/uploads/houses/house3_pool.jpg', 2, NOW(), NOW()),
(3, '/uploads/houses/house3_garden.jpg', 3, NOW(), NOW()),
(3, '/uploads/houses/house3_living.jpg', 4, NOW(), NOW()),
(3, '/uploads/houses/house3_bedroom.jpg', 5, NOW(), NOW()),

-- House 4 (Villa Quận 9)
(4, '/uploads/houses/house4_main.jpg', 1, NOW(), NOW()),
(4, '/uploads/houses/house4_pool.jpg', 2, NOW(), NOW()),
(4, '/uploads/houses/house4_garden.jpg', 3, NOW(), NOW()),
(4, '/uploads/houses/house4_garage.jpg', 4, NOW(), NOW()),

-- House 5 (Townhouse Quận 5)
(5, '/uploads/houses/house5_main.jpg', 1, NOW(), NOW()),
(5, '/uploads/houses/house5_living.jpg', 2, NOW(), NOW()),
(5, '/uploads/houses/house5_bedroom.jpg', 3, NOW(), NOW()),
(5, '/uploads/houses/house5_elevator.jpg', 4, NOW(), NOW()),

-- House 6 (Townhouse Quận 7)
(6, '/uploads/houses/house6_main.jpg', 1, NOW(), NOW()),
(6, '/uploads/houses/house6_living.jpg', 2, NOW(), NOW()),
(6, '/uploads/houses/house6_bedroom.jpg', 3, NOW(), NOW()),
(6, '/uploads/houses/house6_balcony.jpg', 4, NOW(), NOW()),

-- House 7 (Nhà trọ sinh viên Quận 10)
(7, '/uploads/houses/house7_main.jpg', 1, NOW(), NOW()),
(7, '/uploads/houses/house7_room.jpg', 2, NOW(), NOW()),
(7, '/uploads/houses/house7_kitchen.jpg', 3, NOW(), NOW()),

-- House 8 (Nhà trọ cao cấp Quận 3)
(8, '/uploads/houses/house8_main.jpg', 1, NOW(), NOW()),
(8, '/uploads/houses/house8_room.jpg', 2, NOW(), NOW()),
(8, '/uploads/houses/house8_bathroom.jpg', 3, NOW(), NOW()),
(8, '/uploads/houses/house8_security.jpg', 4, NOW(), NOW()),

-- House 9 (Nhà phố Quận 1)
(9, '/uploads/houses/house9_main.jpg', 1, NOW(), NOW()),
(9, '/uploads/houses/house9_living.jpg', 2, NOW(), NOW()),
(9, '/uploads/houses/house9_bedroom.jpg', 3, NOW(), NOW()),
(9, '/uploads/houses/house9_garage.jpg', 4, NOW(), NOW()),

-- House 10 (Nhà phố Quận 5)
(10, '/uploads/houses/house10_main.jpg', 1, NOW(), NOW()),
(10, '/uploads/houses/house10_living.jpg', 2, NOW(), NOW()),
(10, '/uploads/houses/house10_bedroom.jpg', 3, NOW(), NOW()),
(10, '/uploads/houses/house10_kitchen.jpg', 4, NOW(), NOW()),

-- House 11 (Căn hộ Landmark 81)
(11, '/uploads/houses/house11_main.jpg', 1, NOW(), NOW()),
(11, '/uploads/houses/house11_view.jpg', 2, NOW(), NOW()),
(11, '/uploads/houses/house11_living.jpg', 3, NOW(), NOW()),
(11, '/uploads/houses/house11_bedroom.jpg', 4, NOW(), NOW()),
(11, '/uploads/houses/house11_amenities.jpg', 5, NOW(), NOW()),

-- House 12 (Căn hộ The Manor)
(12, '/uploads/houses/house12_main.jpg', 1, NOW(), NOW()),
(12, '/uploads/houses/house12_living.jpg', 2, NOW(), NOW()),
(12, '/uploads/houses/house12_bedroom.jpg', 3, NOW(), NOW()),
(12, '/uploads/houses/house12_security.jpg', 4, NOW(), NOW()),

-- House 13 (Villa Phú Nhuận)
(13, '/uploads/houses/house13_main.jpg', 1, NOW(), NOW()),
(13, '/uploads/houses/house13_pool.jpg', 2, NOW(), NOW()),
(13, '/uploads/houses/house13_garden.jpg', 3, NOW(), NOW()),
(13, '/uploads/houses/house13_garage.jpg', 4, NOW(), NOW()),
(13, '/uploads/houses/house13_living.jpg', 5, NOW(), NOW()),

-- House 14 (Villa Thảo Điền)
(14, '/uploads/houses/house14_main.jpg', 1, NOW(), NOW()),
(14, '/uploads/houses/house14_river_view.jpg', 2, NOW(), NOW()),
(14, '/uploads/houses/house14_garden.jpg', 3, NOW(), NOW()),
(14, '/uploads/houses/house14_living.jpg', 4, NOW(), NOW()),

-- House 15 (Townhouse Quận 7)
(15, '/uploads/houses/house15_main.jpg', 1, NOW(), NOW()),
(15, '/uploads/houses/house15_living.jpg', 2, NOW(), NOW()),
(15, '/uploads/houses/house15_bedroom.jpg', 3, NOW(), NOW()),
(15, '/uploads/houses/house15_elevator.jpg', 4, NOW(), NOW()),

-- House 16 (Townhouse Crescent)
(16, '/uploads/houses/house16_main.jpg', 1, NOW(), NOW()),
(16, '/uploads/houses/house16_living.jpg', 2, NOW(), NOW()),
(16, '/uploads/houses/house16_bedroom.jpg', 3, NOW(), NOW()),
(16, '/uploads/houses/house16_mall_view.jpg', 4, NOW(), NOW()),

-- House 17 (Nhà trọ cao cấp Quận 3)
(17, '/uploads/houses/house17_main.jpg', 1, NOW(), NOW()),
(17, '/uploads/houses/house17_room.jpg', 2, NOW(), NOW()),
(17, '/uploads/houses/house17_kitchen.jpg', 3, NOW(), NOW()),
(17, '/uploads/houses/house17_ac.jpg', 4, NOW(), NOW()),

-- House 18 (Nhà trọ sinh viên Quận 10)
(18, '/uploads/houses/house18_main.jpg', 1, NOW(), NOW()),
(18, '/uploads/houses/house18_room.jpg', 2, NOW(), NOW()),
(18, '/uploads/houses/house18_kitchen.jpg', 3, NOW(), NOW()),

-- House 19 (Nhà phố Quận 1)
(19, '/uploads/houses/house19_main.jpg', 1, NOW(), NOW()),
(19, '/uploads/houses/house19_living.jpg', 2, NOW(), NOW()),
(19, '/uploads/houses/house19_bedroom.jpg', 3, NOW(), NOW()),
(19, '/uploads/houses/house19_garage.jpg', 4, NOW(), NOW()),

-- House 20 (Nhà phố Quận 5)
(20, '/uploads/houses/house20_main.jpg', 1, NOW(), NOW()),
(20, '/uploads/houses/house20_living.jpg', 2, NOW(), NOW()),
(20, '/uploads/houses/house20_bedroom.jpg', 3, NOW(), NOW()),
(20, '/uploads/houses/house20_market_view.jpg', 4, NOW(), NOW());


