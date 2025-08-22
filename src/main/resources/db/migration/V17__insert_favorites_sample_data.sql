-- V17__insert_favorites_sample_data.sql
-- Insert sample data for favorites table with diverse user preferences

-- Step 1: Insert Favorites for Regular Users (Renters - user_id 14-33)
INSERT INTO favorites (user_id, house_id, unique_constraint, created_at, updated_at) VALUES
-- User 14 (Lê Văn Khách) - likes luxury apartments and villas
(14, 1, '14_1', DATE_SUB(NOW(), INTERVAL 60 DAY), NOW()),
(14, 3, '14_3', DATE_SUB(NOW(), INTERVAL 55 DAY), NOW()),
(14, 11, '14_11', DATE_SUB(NOW(), INTERVAL 50 DAY), NOW()),
(14, 13, '14_13', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

-- User 15 (Phan Thị Khách) - likes affordable options
(15, 2, '15_2', DATE_SUB(NOW(), INTERVAL 58 DAY), NOW()),
(15, 7, '15_7', DATE_SUB(NOW(), INTERVAL 52 DAY), NOW()),
(15, 18, '15_18', DATE_SUB(NOW(), INTERVAL 48 DAY), NOW()),

-- User 16 (Võ Văn Khách) - likes studios and small apartments
(16, 2, '16_2', DATE_SUB(NOW(), INTERVAL 56 DAY), NOW()),
(16, 12, '16_12', DATE_SUB(NOW(), INTERVAL 51 DAY), NOW()),
(16, 17, '16_17', DATE_SUB(NOW(), INTERVAL 47 DAY), NOW()),

-- User 17 (Trần Thị Khách) - likes villas and luxury houses
(17, 3, '17_3', DATE_SUB(NOW(), INTERVAL 54 DAY), NOW()),
(17, 4, '17_4', DATE_SUB(NOW(), INTERVAL 49 DAY), NOW()),
(17, 13, '17_13', DATE_SUB(NOW(), INTERVAL 44 DAY), NOW()),
(17, 14, '17_14', DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),

-- User 18 (Nguyễn Văn Khách) - likes townhouses
(18, 5, '18_5', DATE_SUB(NOW(), INTERVAL 53 DAY), NOW()),
(18, 6, '18_6', DATE_SUB(NOW(), INTERVAL 48 DAY), NOW()),
(18, 15, '18_15', DATE_SUB(NOW(), INTERVAL 43 DAY), NOW()),
(18, 16, '18_16', DATE_SUB(NOW(), INTERVAL 38 DAY), NOW()),

-- User 19 (Hoàng Thị Khách) - likes family houses
(19, 9, '19_9', DATE_SUB(NOW(), INTERVAL 52 DAY), NOW()),
(19, 10, '19_10', DATE_SUB(NOW(), INTERVAL 47 DAY), NOW()),
(19, 19, '19_19', DATE_SUB(NOW(), INTERVAL 42 DAY), NOW()),
(19, 20, '19_20', DATE_SUB(NOW(), INTERVAL 37 DAY), NOW()),

-- User 20 (Đặng Văn Khách) - likes budget options
(20, 7, '20_7', DATE_SUB(NOW(), INTERVAL 51 DAY), NOW()),
(20, 8, '20_8', DATE_SUB(NOW(), INTERVAL 46 DAY), NOW()),
(20, 17, '20_17', DATE_SUB(NOW(), INTERVAL 41 DAY), NOW()),
(20, 18, '20_18', DATE_SUB(NOW(), INTERVAL 36 DAY), NOW()),

-- User 21 (Bùi Thị Khách) - likes modern apartments
(21, 1, '21_1', DATE_SUB(NOW(), INTERVAL 50 DAY), NOW()),
(21, 11, '21_11', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(21, 12, '21_12', DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),

-- User 22 (Lý Văn Khách) - likes premium options
(22, 3, '22_3', DATE_SUB(NOW(), INTERVAL 49 DAY), NOW()),
(22, 4, '22_4', DATE_SUB(NOW(), INTERVAL 44 DAY), NOW()),
(22, 13, '22_13', DATE_SUB(NOW(), INTERVAL 39 DAY), NOW()),
(22, 14, '22_14', DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),

-- User 23 (Trịnh Thị Khách) - likes spacious houses
(23, 5, '23_5', DATE_SUB(NOW(), INTERVAL 48 DAY), NOW()),
(23, 9, '23_9', DATE_SUB(NOW(), INTERVAL 43 DAY), NOW()),
(23, 15, '23_15', DATE_SUB(NOW(), INTERVAL 38 DAY), NOW()),
(23, 19, '23_19', DATE_SUB(NOW(), INTERVAL 33 DAY), NOW()),

-- User 24 (Hồ Văn Khách) - likes convenient locations
(24, 6, '24_6', DATE_SUB(NOW(), INTERVAL 47 DAY), NOW()),
(24, 10, '24_10', DATE_SUB(NOW(), INTERVAL 42 DAY), NOW()),
(24, 16, '24_16', DATE_SUB(NOW(), INTERVAL 37 DAY), NOW()),
(24, 20, '24_20', DATE_SUB(NOW(), INTERVAL 32 DAY), NOW()),

-- User 25 (Dương Thị Khách) - likes luxury apartments
(25, 1, '25_1', DATE_SUB(NOW(), INTERVAL 46 DAY), NOW()),
(25, 11, '25_11', DATE_SUB(NOW(), INTERVAL 41 DAY), NOW()),
(25, 12, '25_12', DATE_SUB(NOW(), INTERVAL 36 DAY), NOW()),

-- User 26 (Ngô Văn Khách) - likes affordable apartments
(26, 2, '26_2', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(26, 7, '26_7', DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(26, 8, '26_8', DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(26, 17, '26_17', DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),

-- User 27 (Lê Thị Khách) - likes family villas
(27, 3, '27_3', DATE_SUB(NOW(), INTERVAL 44 DAY), NOW()),
(27, 4, '27_4', DATE_SUB(NOW(), INTERVAL 39 DAY), NOW()),
(27, 13, '27_13', DATE_SUB(NOW(), INTERVAL 34 DAY), NOW()),
(27, 14, '27_14', DATE_SUB(NOW(), INTERVAL 29 DAY), NOW()),

-- User 28 (Phan Văn Khách) - likes townhouses
(28, 5, '28_5', DATE_SUB(NOW(), INTERVAL 43 DAY), NOW()),
(28, 6, '28_6', DATE_SUB(NOW(), INTERVAL 38 DAY), NOW()),
(28, 15, '28_15', DATE_SUB(NOW(), INTERVAL 33 DAY), NOW()),
(28, 16, '28_16', DATE_SUB(NOW(), INTERVAL 28 DAY), NOW()),

-- User 29 (Võ Thị Khách) - likes modern houses
(29, 9, '29_9', DATE_SUB(NOW(), INTERVAL 42 DAY), NOW()),
(29, 10, '29_10', DATE_SUB(NOW(), INTERVAL 37 DAY), NOW()),
(29, 19, '29_19', DATE_SUB(NOW(), INTERVAL 32 DAY), NOW()),
(29, 20, '29_20', DATE_SUB(NOW(), INTERVAL 27 DAY), NOW()),

-- User 30 (Trần Văn Khách) - likes budget-friendly options
(30, 7, '30_7', DATE_SUB(NOW(), INTERVAL 41 DAY), NOW()),
(30, 8, '30_8', DATE_SUB(NOW(), INTERVAL 36 DAY), NOW()),
(30, 17, '30_17', DATE_SUB(NOW(), INTERVAL 31 DAY), NOW()),
(30, 18, '30_18', DATE_SUB(NOW(), INTERVAL 26 DAY), NOW()),

-- User 31 (Nguyễn Thị Khách) - likes premium apartments
(31, 1, '31_1', DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(31, 11, '31_11', DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(31, 12, '31_12', DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),

-- User 32 (Hoàng Văn Khách) - likes spacious options
(32, 3, '32_3', DATE_SUB(NOW(), INTERVAL 39 DAY), NOW()),
(32, 5, '32_5', DATE_SUB(NOW(), INTERVAL 34 DAY), NOW()),
(32, 9, '32_9', DATE_SUB(NOW(), INTERVAL 29 DAY), NOW()),
(32, 13, '32_13', DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),

-- User 33 (Đặng Thị Khách) - likes convenient locations
(33, 6, '33_6', DATE_SUB(NOW(), INTERVAL 38 DAY), NOW()),
(33, 10, '33_10', DATE_SUB(NOW(), INTERVAL 33 DAY), NOW()),
(33, 16, '33_16', DATE_SUB(NOW(), INTERVAL 28 DAY), NOW()),
(33, 20, '33_20', DATE_SUB(NOW(), INTERVAL 23 DAY), NOW());

-- Step 2: Insert Favorites for Host Users (user_id 4-13) - hosts can also have favorites
INSERT INTO favorites (user_id, house_id, unique_constraint, created_at, updated_at) VALUES
-- Host 1 (Phạm Văn Chủ Nhà) - likes other luxury properties
(4, 3, '4_3', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(4, 11, '4_11', DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(4, 13, '4_13', DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),

-- Host 2 (Hoàng Thị Chủ Nhà) - likes modern designs
(5, 1, '5_1', DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(5, 12, '5_12', DATE_SUB(NOW(), INTERVAL 19 DAY), NOW()),
(5, 15, '5_15', DATE_SUB(NOW(), INTERVAL 14 DAY), NOW()),

-- Host 3 (Vũ Văn Chủ Nhà) - likes spacious properties
(6, 9, '6_9', DATE_SUB(NOW(), INTERVAL 23 DAY), NOW()),
(6, 19, '6_19', DATE_SUB(NOW(), INTERVAL 18 DAY), NOW()),
(6, 20, '6_20', DATE_SUB(NOW(), INTERVAL 13 DAY), NOW()),

-- Host 4 (Đặng Thị Chủ Nhà) - likes affordable options
(7, 2, '7_2', DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(7, 17, '7_17', DATE_SUB(NOW(), INTERVAL 17 DAY), NOW()),
(7, 18, '7_18', DATE_SUB(NOW(), INTERVAL 12 DAY), NOW()),

-- Host 5 (Bùi Văn Chủ Nhà) - likes premium properties
(8, 3, '8_3', DATE_SUB(NOW(), INTERVAL 21 DAY), NOW()),
(8, 4, '8_4', DATE_SUB(NOW(), INTERVAL 16 DAY), NOW()),
(8, 14, '8_14', DATE_SUB(NOW(), INTERVAL 11 DAY), NOW()),

-- Host 6 (Lý Thị Chủ Nhà) - likes modern apartments
(9, 1, '9_1', DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(9, 11, '9_11', DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
(9, 12, '9_12', DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),

-- Host 7 (Trịnh Văn Chủ Nhà) - likes family houses
(10, 5, '10_5', DATE_SUB(NOW(), INTERVAL 19 DAY), NOW()),
(10, 9, '10_9', DATE_SUB(NOW(), INTERVAL 14 DAY), NOW()),
(10, 15, '10_15', DATE_SUB(NOW(), INTERVAL 9 DAY), NOW()),

-- Host 8 (Hồ Thị Chủ Nhà) - likes convenient locations
(11, 6, '11_6', DATE_SUB(NOW(), INTERVAL 18 DAY), NOW()),
(11, 10, '11_10', DATE_SUB(NOW(), INTERVAL 13 DAY), NOW()),
(11, 16, '11_16', DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),

-- Host 9 (Dương Văn Chủ Nhà) - likes budget options
(12, 7, '12_7', DATE_SUB(NOW(), INTERVAL 17 DAY), NOW()),
(12, 8, '12_8', DATE_SUB(NOW(), INTERVAL 12 DAY), NOW()),
(12, 17, '12_17', DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),

-- Host 10 (Ngô Thị Chủ Nhà) - likes luxury properties
(13, 3, '13_3', DATE_SUB(NOW(), INTERVAL 16 DAY), NOW()),
(13, 4, '13_4', DATE_SUB(NOW(), INTERVAL 11 DAY), NOW()),
(13, 13, '13_13', DATE_SUB(NOW(), INTERVAL 6 DAY), NOW()),
(13, 14, '13_14', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW());

-- Step 3: Insert Recent Favorites (created in the last few days)
INSERT INTO favorites (user_id, house_id, unique_constraint, created_at, updated_at) VALUES
-- Recent favorites from regular users (avoiding duplicates with Step 1)
(14, 19, '14_19', DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
(15, 20, '15_20', DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(16, 3, '16_3', DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(17, 12, '17_12', DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(18, 14, '18_14', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(19, 15, '19_15', NOW(), NOW()),
(20, 18, '20_18', NOW(), NOW()),

-- Recent favorites from hosts (avoiding duplicates with Step 2)
(4, 20, '4_20', DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(5, 19, '5_19', DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(6, 18, '6_18', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(7, 16, '7_16', NOW(), NOW()),
(8, 15, '8_15', NOW(), NOW());
