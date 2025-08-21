-- V13__insert_hosts_and_requests_sample_data.sql
-- Insert sample data for hosts and host_requests tables

-- Step 1: Insert Host entities for approved host users (user_id 4-13 from V12)
-- These are users with role_id = 2 (HOST)
INSERT INTO hosts (user_id, national_id, proof_of_ownership_url, address, approved_date, created_at, updated_at) VALUES
-- Host 1 (user_id = 4)
(4, '123456789012', '/uploads/proofs/proof_host1.jpg', '321 Đường JKL, Quận 1, TP.HCM', DATE_SUB(NOW(), INTERVAL 60 DAY), NOW(), NOW()),

-- Host 2 (user_id = 5)
(5, '234567890123', '/uploads/proofs/proof_host2.jpg', '654 Đường MNO, Quận 2, TP.HCM', DATE_SUB(NOW(), INTERVAL 55 DAY), NOW(), NOW()),

-- Host 3 (user_id = 6)
(6, '345678901234', '/uploads/proofs/proof_host3.jpg', '987 Đường PQR, Quận 3, TP.HCM', DATE_SUB(NOW(), INTERVAL 50 DAY), NOW(), NOW()),

-- Host 4 (user_id = 7)
(7, '456789012345', '/uploads/proofs/proof_host4.jpg', '147 Đường STU, Quận 4, TP.HCM', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW(), NOW()),

-- Host 5 (user_id = 8)
(8, '567890123456', '/uploads/proofs/proof_host5.jpg', '258 Đường VWX, Quận 5, TP.HCM', DATE_SUB(NOW(), INTERVAL 40 DAY), NOW(), NOW()),

-- Host 6 (user_id = 9)
(9, '678901234567', '/uploads/proofs/proof_host6.jpg', '369 Đường YZA, Quận 6, TP.HCM', DATE_SUB(NOW(), INTERVAL 35 DAY), NOW(), NOW()),

-- Host 7 (user_id = 10)
(10, '789012345678', '/uploads/proofs/proof_host7.jpg', '741 Đường BCD, Quận 7, TP.HCM', DATE_SUB(NOW(), INTERVAL 30 DAY), NOW(), NOW()),

-- Host 8 (user_id = 11)
(11, '890123456789', '/uploads/proofs/proof_host8.jpg', '852 Đường EFG, Quận 8, TP.HCM', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW(), NOW()),

-- Host 9 (user_id = 12)
(12, '901234567890', '/uploads/proofs/proof_host9.jpg', '963 Đường HIJ, Quận 9, TP.HCM', DATE_SUB(NOW(), INTERVAL 20 DAY), NOW(), NOW()),

-- Host 10 (user_id = 13)
(13, '012345678901', '/uploads/proofs/proof_host10.jpg', '159 Đường KLM, Quận 10, TP.HCM', DATE_SUB(NOW(), INTERVAL 15 DAY), NOW(), NOW());

-- Step 2: Insert Host Requests for various statuses
-- Pending requests (for users who want to become hosts)
INSERT INTO host_requests (user_id, status, national_id, proof_of_ownership_url, id_front_photo_url, id_back_photo_url, reason, request_date, processed_date, created_at, updated_at) VALUES
-- Pending requests (user_id 34-35 from V12 - pending_host1, pending_host2)
(34, 'PENDING', '111111111111', '/uploads/proofs/proof_pending1.jpg', '/uploads/ids/id_pending1_front.jpg', '/uploads/ids/id_pending1_back.jpg', 'Có căn hộ trống muốn cho thuê để tăng thu nhập', NOW(), NULL, NOW(), NOW()),
(35, 'PENDING', '222222222222', '/uploads/proofs/proof_pending2.jpg', '/uploads/ids/id_pending2_front.jpg', '/uploads/ids/id_pending2_back.jpg', 'Đăng ký làm chủ nhà để quản lý tài sản', NOW(), NULL, NOW(), NOW()),

-- Approved requests (for users who became hosts - user_id 4-13)
(4, 'APPROVED', '123456789012', '/uploads/proofs/proof_host1.jpg', '/uploads/ids/id_host1_front.jpg', '/uploads/ids/id_host1_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 65 DAY), DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 65 DAY), NOW()),
(5, 'APPROVED', '234567890123', '/uploads/proofs/proof_host2.jpg', '/uploads/ids/id_host2_front.jpg', '/uploads/ids/id_host2_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY), DATE_SUB(NOW(), INTERVAL 60 DAY), NOW()),
(6, 'APPROVED', '345678901234', '/uploads/proofs/proof_host3.jpg', '/uploads/ids/id_host3_front.jpg', '/uploads/ids/id_host3_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 55 DAY), DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY), NOW()),
(7, 'APPROVED', '456789012345', '/uploads/proofs/proof_host4.jpg', '/uploads/ids/id_host4_front.jpg', '/uploads/ids/id_host4_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 50 DAY), NOW()),
(8, 'APPROVED', '567890123456', '/uploads/proofs/proof_host5.jpg', '/uploads/ids/id_host5_front.jpg', '/uploads/ids/id_host5_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(9, 'APPROVED', '678901234567', '/uploads/proofs/proof_host6.jpg', '/uploads/ids/id_host6_front.jpg', '/uploads/ids/id_host6_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(10, 'APPROVED', '789012345678', '/uploads/proofs/proof_host7.jpg', '/uploads/ids/id_host7_front.jpg', '/uploads/ids/id_host7_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(11, 'APPROVED', '890123456789', '/uploads/proofs/proof_host8.jpg', '/uploads/ids/id_host8_front.jpg', '/uploads/ids/id_host8_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(12, 'APPROVED', '901234567890', '/uploads/proofs/proof_host9.jpg', '/uploads/ids/id_host9_front.jpg', '/uploads/ids/id_host9_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(13, 'APPROVED', '012345678901', '/uploads/proofs/proof_host10.jpg', '/uploads/ids/id_host10_front.jpg', '/uploads/ids/id_host10_back.jpg', 'Đã được duyệt', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),

-- Rejected requests (user_id 36-37 from V12 - rejected_host1, rejected_host2)
(36, 'REJECTED', '333333333333', '/uploads/proofs/proof_rejected1.jpg', '/uploads/ids/id_rejected1_front.jpg', '/uploads/ids/id_rejected1_back.jpg', 'Bị từ chối do thiếu giấy tờ hợp lệ', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(37, 'REJECTED', '444444444444', '/uploads/proofs/proof_rejected2.jpg', '/uploads/ids/id_rejected2_front.jpg', '/uploads/ids/id_rejected2_back.jpg', 'Bị từ chối do thông tin không chính xác', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),

-- Additional pending requests (for regular users who want to become hosts)
(14, 'PENDING', '555555555555', '/uploads/proofs/proof_pending3.jpg', '/uploads/ids/id_pending3_front.jpg', '/uploads/ids/id_pending3_back.jpg', 'Có nhà trống muốn cho thuê', NOW(), NULL, NOW(), NOW()),
(15, 'PENDING', '666666666666', '/uploads/proofs/proof_pending4.jpg', '/uploads/ids/id_pending4_front.jpg', '/uploads/ids/id_pending4_back.jpg', 'Đăng ký làm chủ nhà để tăng thu nhập', NOW(), NULL, NOW(), NOW()),
(16, 'PENDING', '777777777777', '/uploads/proofs/proof_pending5.jpg', '/uploads/ids/id_pending5_front.jpg', '/uploads/ids/id_pending5_back.jpg', 'Có căn hộ muốn cho thuê', NOW(), NULL, NOW(), NOW()),

-- Additional rejected requests
(17, 'REJECTED', '888888888888', '/uploads/proofs/proof_rejected3.jpg', '/uploads/ids/id_rejected3_front.jpg', '/uploads/ids/id_rejected3_back.jpg', 'Bị từ chối do không đủ điều kiện', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
(18, 'REJECTED', '999999999999', '/uploads/proofs/proof_rejected4.jpg', '/uploads/ids/id_rejected4_front.jpg', '/uploads/ids/id_rejected4_back.jpg', 'Bị từ chối do hồ sơ không đầy đủ', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NOW());


