-- V18__insert_notifications_sample_data.sql
-- Insert sample data for notifications table with diverse types and read status

-- Step 1: Insert Rental Request Notifications (for hosts)
INSERT INTO notifications (receiver_id, type, content, rental_id, house_id, is_read, created_at, updated_at) VALUES
-- Host 1 (user_id 4) - rental requests for house 1-2
(4, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho căn hộ Quận 1 từ Lê Văn Khách', 25, 1, false, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(4, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho studio Quận 1 từ Võ Văn Khách', 26, 2, true, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),

-- Host 2 (user_id 5) - rental requests for house 3-4
(5, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho villa Quận 2 từ Trần Thị Khách', 27, 3, false, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(5, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho villa cao cấp Quận 2 từ Nguyễn Văn Khách', 28, 4, true, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),

-- Host 3 (user_id 6) - rental requests for house 5-6
(6, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho townhouse Quận 3 từ Hoàng Thị Khách', 29, 5, false, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(6, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho townhouse Crescent từ Hồ Văn Khách', 30, 6, true, NOW(), NOW()),

-- Host 4 (user_id 7) - rental requests for house 7-8
(7, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho nhà trọ sinh viên từ Đặng Văn Khách', 31, 7, false, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(7, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho nhà trọ cao cấp từ Bùi Thị Khách', 32, 8, true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW());

-- Step 2: Insert Rental Approved Notifications (for renters)
INSERT INTO notifications (receiver_id, type, content, rental_id, house_id, is_read, created_at, updated_at) VALUES
-- User 25 - approved rental for house 11
(25, 'RENTAL_APPROVED', 'Yêu cầu thuê căn hộ Landmark 81 đã được chủ nhà chấp nhận', 11, 11, true, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),

-- User 26 - approved rental for house 12
(26, 'RENTAL_APPROVED', 'Yêu cầu thuê căn hộ The Manor đã được chủ nhà chấp nhận', 12, 12, true, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),

-- User 27 - approved rental for house 13
(27, 'RENTAL_APPROVED', 'Yêu cầu thuê villa Phú Nhuận đã được chủ nhà chấp nhận', 13, 13, false, DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),

-- User 28 - approved rental for house 14
(28, 'RENTAL_APPROVED', 'Yêu cầu thuê villa Thảo Điền đã được chủ nhà chấp nhận', 14, 14, false, DATE_SUB(NOW(), INTERVAL 6 DAY), NOW()),

-- User 29 - approved rental for house 15
(29, 'RENTAL_APPROVED', 'Yêu cầu thuê townhouse Quận 7 đã được chủ nhà chấp nhận', 15, 15, true, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),

-- User 30 - approved rental for house 16
(30, 'RENTAL_APPROVED', 'Yêu cầu thuê townhouse Crescent đã được chủ nhà chấp nhận', 16, 16, true, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),

-- User 31 - approved rental for house 17
(31, 'RENTAL_APPROVED', 'Yêu cầu thuê nhà trọ cao cấp đã được chủ nhà chấp nhận', 17, 17, false, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),

-- User 32 - approved rental for house 18
(32, 'RENTAL_APPROVED', 'Yêu cầu thuê nhà trọ sinh viên đã được chủ nhà chấp nhận', 18, 18, false, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),

-- User 33 - approved rental for house 19
(33, 'RENTAL_APPROVED', 'Yêu cầu thuê nhà phố Quận 1 đã được chủ nhà chấp nhận', 19, 19, true, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),

-- User 14 - approved rental for house 20
(14, 'RENTAL_APPROVED', 'Yêu cầu thuê nhà phố Quận 5 đã được chủ nhà chấp nhận', 20, 20, false, NOW(), NOW());

-- Step 3: Insert Rental Rejected Notifications (for renters)
INSERT INTO notifications (receiver_id, type, content, rental_id, house_id, is_read, created_at, updated_at) VALUES
-- User 19 - rejected rental for house 5
(19, 'RENTAL_REJECTED', 'Yêu cầu thuê townhouse Quận 3 đã bị từ chối: Không phù hợp với yêu cầu', 29, 5, true, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),

-- User 20 - rejected rental for house 6
(20, 'RENTAL_REJECTED', 'Yêu cầu thuê townhouse Crescent đã bị từ chối: Đã có người thuê trước', 30, 6, true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),

-- User 21 - rejected rental for house 7
(21, 'RENTAL_REJECTED', 'Yêu cầu thuê nhà trọ sinh viên đã bị từ chối: Không đủ điều kiện', 31, 7, false, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),

-- User 22 - rejected rental for house 8
(22, 'RENTAL_REJECTED', 'Yêu cầu thuê nhà trọ cao cấp đã bị từ chối: Thông tin không chính xác', 32, 8, false, NOW(), NOW());

-- Step 4: Insert Rental Canceled Notifications (for hosts)
INSERT INTO notifications (receiver_id, type, content, rental_id, house_id, is_read, created_at, updated_at) VALUES
-- Host 5 (user_id 8) - canceled rentals for house 9-10
(8, 'RENTAL_CANCELED', 'Khách hàng đã hủy thuê nhà phố Quận 1: Thay đổi kế hoạch', 33, 9, true, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),
(8, 'RENTAL_CANCELED', 'Khách hàng đã hủy thuê nhà phố Quận 5: Có việc đột xuất', 34, 10, true, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),

-- Host 6 (user_id 9) - canceled rentals for house 11-12
(9, 'RENTAL_CANCELED', 'Khách hàng đã hủy thuê căn hộ Landmark: Thay đổi địa điểm', 35, 11, false, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
(9, 'RENTAL_CANCELED', 'Khách hàng đã hủy thuê căn hộ The Manor: Hủy do lý do cá nhân', 36, 12, false, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW());

-- Step 5: Insert Review One Star Notifications (for hosts)
INSERT INTO notifications (receiver_id, type, content, review_id, house_id, is_read, created_at, updated_at) VALUES
-- Host 1 (user_id 4) - one star review for house 1
(4, 'REVIEW_ONE_STAR', 'Bạn có đánh giá 1 sao cho căn hộ Quận 1 từ khách hàng', 31, 1, false, DATE_SUB(NOW(), INTERVAL 50 DAY), NOW()),

-- Host 2 (user_id 5) - one star review for house 3
(5, 'REVIEW_ONE_STAR', 'Bạn có đánh giá 1 sao cho villa Quận 2 từ khách hàng', 32, 3, true, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

-- Host 4 (user_id 7) - one star review for house 7
(7, 'REVIEW_ONE_STAR', 'Bạn có đánh giá 1 sao cho nhà trọ sinh viên từ khách hàng', 33, 7, false, DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),

-- Host 6 (user_id 9) - one star review for house 11
(9, 'REVIEW_ONE_STAR', 'Bạn có đánh giá 1 sao cho căn hộ Landmark từ khách hàng', 34, 11, true, DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),

-- Host 9 (user_id 12) - one star review for house 17
(12, 'REVIEW_ONE_STAR', 'Bạn có đánh giá 1 sao cho nhà trọ cao cấp từ khách hàng', 35, 17, false, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW());

-- Step 6: Insert General Notifications
INSERT INTO notifications (receiver_id, type, content, is_read, created_at, updated_at) VALUES
-- General notifications for regular users
(14, 'GENERAL', 'Chào mừng bạn đến với RentalHouse! Hãy khám phá các căn hộ tuyệt vời.', true, DATE_SUB(NOW(), INTERVAL 60 DAY), NOW()),
(15, 'GENERAL', 'Có 5 căn hộ mới phù hợp với sở thích của bạn. Hãy xem ngay!', false, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(16, 'GENERAL', 'Giảm giá 10% cho các căn hộ cao cấp trong tháng này.', true, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(17, 'GENERAL', 'Cập nhật chính sách bảo mật mới. Vui lòng xem chi tiết.', false, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(18, 'GENERAL', 'Hệ thống sẽ bảo trì từ 2h-4h sáng ngày mai.', true, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),

-- General notifications for hosts
(4, 'GENERAL', 'Chào mừng bạn trở thành chủ nhà! Hãy đăng nhà đầu tiên của bạn.', true, DATE_SUB(NOW(), INTERVAL 65 DAY), NOW()),
(5, 'GENERAL', 'Có 3 yêu cầu thuê mới cho các nhà của bạn. Hãy kiểm tra ngay!', false, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),
(6, 'GENERAL', 'Cập nhật tính năng quản lý nhà mới. Vui lòng xem hướng dẫn.', true, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),
(7, 'GENERAL', 'Tăng cường bảo mật cho tài khoản chủ nhà.', false, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
(8, 'GENERAL', 'Thống kê doanh thu tháng này đã có sẵn.', true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),

-- General notifications for admins
(1, 'GENERAL', 'Có 5 đơn xin đăng ký chủ nhà mới cần duyệt.', false, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(2, 'GENERAL', 'Báo cáo hệ thống tháng này đã sẵn sàng.', true, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(3, 'GENERAL', 'Cập nhật hệ thống mới sẽ được triển khai vào tuần tới.', false, NOW(), NOW());

-- Step 7: Insert Recent Notifications (last few days)
INSERT INTO notifications (receiver_id, type, content, rental_id, house_id, is_read, created_at, updated_at) VALUES
-- Recent rental requests
(9, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho căn hộ Landmark từ Dương Thị Khách', 37, 11, false, DATE_SUB(NOW(), INTERVAL 12 HOUR), NOW()),
(10, 'RENTAL_REQUEST', 'Bạn có yêu cầu thuê mới cho villa Phú Nhuận từ Ngô Văn Khách', 38, 13, false, DATE_SUB(NOW(), INTERVAL 6 HOUR), NOW()),

-- Recent rental approvals
(31, 'RENTAL_APPROVED', 'Yêu cầu thuê căn hộ Landmark đã được chủ nhà chấp nhận', 37, 11, false, DATE_SUB(NOW(), INTERVAL 4 HOUR), NOW()),
(32, 'RENTAL_APPROVED', 'Yêu cầu thuê villa Phú Nhuận đã được chủ nhà chấp nhận', 38, 13, false, DATE_SUB(NOW(), INTERVAL 2 HOUR), NOW()),

-- Recent rental cancellations
(9, 'RENTAL_CANCELED', 'Khách hàng đã hủy thuê căn hộ Landmark: Lý do cá nhân', 39, 11, false, DATE_SUB(NOW(), INTERVAL 1 HOUR), NOW()),
(10, 'RENTAL_CANCELED', 'Khách hàng đã hủy thuê villa Phú Nhuận: Thay đổi kế hoạch', 40, 13, false, NOW(), NOW());


