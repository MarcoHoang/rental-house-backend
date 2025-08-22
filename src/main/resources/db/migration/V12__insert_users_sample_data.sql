-- V12__insert_users_sample_data.sql
-- Insert sample data for users table only
-- Password for all users: 123456 (BCrypt hash)

-- Step 1: Insert Admin Users
INSERT INTO users (username, email, phone, password, active, img, full_name, address, birth_date, role_id, created_at, updated_at) VALUES
('admin1', 'admin1@rentalhouse.com', '0901234567', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Nguyễn Văn Admin', '123 Đường ABC, Quận 1, TP.HCM', '1985-03-15', 1, NOW(), NOW()),
('admin2', 'admin2@rentalhouse.com', '0901234568', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Trần Thị Quản Lý', '456 Đường DEF, Quận 2, TP.HCM', '1990-07-22', 1, NOW(), NOW()),
('admin3', 'admin3@rentalhouse.com', '0901234569', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Lê Văn Quản Lý', '789 Đường GHI, Quận 3, TP.HCM', '1988-11-08', 1, NOW(), NOW());

-- Step 2: Insert Host Users
INSERT INTO users (username, email, phone, password, active, img, full_name, address, birth_date, role_id, created_at, updated_at) VALUES
('host1', 'host1@rentalhouse.com', '0901234570', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Phạm Văn Chủ Nhà', '321 Đường JKL, Quận 1, TP.HCM', '1982-05-20', 2, NOW(), NOW()),
('host2', 'host2@rentalhouse.com', '0901234571', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Hoàng Thị Chủ Nhà', '654 Đường MNO, Quận 2, TP.HCM', '1987-09-12', 2, NOW(), NOW()),
('host3', 'host3@rentalhouse.com', '0901234572', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Vũ Văn Chủ Nhà', '987 Đường PQR, Quận 3, TP.HCM', '1984-12-03', 2, NOW(), NOW()),
('host4', 'host4@rentalhouse.com', '0901234573', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Đặng Thị Chủ Nhà', '147 Đường STU, Quận 4, TP.HCM', '1989-01-25', 2, NOW(), NOW()),
('host5', 'host5@rentalhouse.com', '0901234574', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Bùi Văn Chủ Nhà', '258 Đường VWX, Quận 5, TP.HCM', '1986-06-18', 2, NOW(), NOW()),
('host6', 'host6@rentalhouse.com', '0901234575', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Lý Thị Chủ Nhà', '369 Đường YZA, Quận 6, TP.HCM', '1991-04-30', 2, NOW(), NOW()),
('host7', 'host7@rentalhouse.com', '0901234576', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Trịnh Văn Chủ Nhà', '741 Đường BCD, Quận 7, TP.HCM', '1983-08-14', 2, NOW(), NOW()),
('host8', 'host8@rentalhouse.com', '0901234577', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Hồ Thị Chủ Nhà', '852 Đường EFG, Quận 8, TP.HCM', '1988-10-07', 2, NOW(), NOW()),
('host9', 'host9@rentalhouse.com', '0901234578', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Dương Văn Chủ Nhà', '963 Đường HIJ, Quận 9, TP.HCM', '1985-02-28', 2, NOW(), NOW()),
('host10', 'host10@rentalhouse.com', '0901234579', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Ngô Thị Chủ Nhà', '159 Đường KLM, Quận 10, TP.HCM', '1990-12-11', 2, NOW(), NOW());

-- Step 3: Insert Regular Users (Renters)
INSERT INTO users (username, email, phone, password, active, img, full_name, address, birth_date, role_id, created_at, updated_at) VALUES
('user1', 'user1@rentalhouse.com', '0901234580', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Lê Văn Khách', '357 Đường NOP, Quận 11, TP.HCM', '1995-03-25', 3, NOW(), NOW()),
('user2', 'user2@rentalhouse.com', '0901234581', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Phan Thị Khách', '468 Đường QRS, Quận 12, TP.HCM', '1993-07-16', 3, NOW(), NOW()),
('user3', 'user3@rentalhouse.com', '0901234582', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Võ Văn Khách', '579 Đường TUV, Quận Bình Tân, TP.HCM', '1997-11-09', 3, NOW(), NOW()),
('user4', 'user4@rentalhouse.com', '0901234583', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Trần Thị Khách', '680 Đường WXY, Quận Tân Bình, TP.HCM', '1994-05-22', 3, NOW(), NOW()),
('user5', 'user5@rentalhouse.com', '0901234584', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Nguyễn Văn Khách', '791 Đường ZAB, Quận Bình Thạnh, TP.HCM', '1996-09-03', 3, NOW(), NOW()),
('user6', 'user6@rentalhouse.com', '0901234585', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Hoàng Thị Khách', '802 Đường CDE, Quận Gò Vấp, TP.HCM', '1992-01-18', 3, NOW(), NOW()),
('user7', 'user7@rentalhouse.com', '0901234586', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Đặng Văn Khách', '913 Đường FGH, Quận Phú Nhuận, TP.HCM', '1998-04-12', 3, NOW(), NOW()),
('user8', 'user8@rentalhouse.com', '0901234587', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Bùi Thị Khách', '024 Đường IJK, Quận 1, TP.HCM', '1991-08-27', 3, NOW(), NOW()),
('user9', 'user9@rentalhouse.com', '0901234588', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Lý Văn Khách', '135 Đường LMN, Quận 2, TP.HCM', '1995-12-05', 3, NOW(), NOW()),
('user10', 'user10@rentalhouse.com', '0901234589', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Trịnh Thị Khách', '246 Đường OPQ, Quận 3, TP.HCM', '1993-06-19', 3, NOW(), NOW()),
('user11', 'user11@rentalhouse.com', '0901234590', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Hồ Văn Khách', '357 Đường RST, Quận 4, TP.HCM', '1997-02-14', 3, NOW(), NOW()),
('user12', 'user12@rentalhouse.com', '0901234591', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Dương Thị Khách', '468 Đường UVW, Quận 5, TP.HCM', '1994-10-31', 3, NOW(), NOW()),
('user13', 'user13@rentalhouse.com', '0901234592', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Ngô Văn Khách', '579 Đường XYZ, Quận 6, TP.HCM', '1996-07-08', 3, NOW(), NOW()),
('user14', 'user14@rentalhouse.com', '0901234593', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Lê Thị Khách', '680 Đường ABC, Quận 7, TP.HCM', '1992-11-23', 3, NOW(), NOW()),
('user15', 'user15@rentalhouse.com', '0901234594', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Phan Văn Khách', '791 Đường DEF, Quận 8, TP.HCM', '1998-03-17', 3, NOW(), NOW()),
('user16', 'user16@rentalhouse.com', '0901234595', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Võ Thị Khách', '802 Đường GHI, Quận 9, TP.HCM', '1991-09-29', 3, NOW(), NOW()),
('user17', 'user17@rentalhouse.com', '0901234596', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Trần Văn Khách', '913 Đường JKL, Quận 10, TP.HCM', '1995-01-07', 3, NOW(), NOW()),
('user18', 'user18@rentalhouse.com', '0901234597', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Nguyễn Thị Khách', '024 Đường MNO, Quận 11, TP.HCM', '1993-12-13', 3, NOW(), NOW()),
('user19', 'user19@rentalhouse.com', '0901234598', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Hoàng Văn Khách', '135 Đường PQR, Quận 12, TP.HCM', '1997-05-26', 3, NOW(), NOW()),
('user20', 'user20@rentalhouse.com', '0901234599', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Đặng Thị Khách', '246 Đường STU, Quận Bình Tân, TP.HCM', '1994-08-04', 3, NOW(), NOW());

-- Step 4: Insert additional users for host applications (pending hosts)
INSERT INTO users (username, email, phone, password, active, img, full_name, address, birth_date, role_id, created_at, updated_at) VALUES
('pending_host1', 'pending_host1@rentalhouse.com', '0901234600', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Bùi Văn Chờ Duyệt', '357 Đường VWX, Quận Tân Bình, TP.HCM', '1988-06-15', 3, NOW(), NOW()),
('pending_host2', 'pending_host2@rentalhouse.com', '0901234601', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Lý Thị Chờ Duyệt', '468 Đường YZA, Quận Bình Thạnh, TP.HCM', '1990-04-21', 3, NOW(), NOW()),
('rejected_host1', 'rejected_host1@rentalhouse.com', '0901234602', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Trịnh Văn Bị Từ Chối', '579 Đường BCD, Quận Gò Vấp, TP.HCM', '1985-11-30', 3, NOW(), NOW()),
('rejected_host2', 'rejected_host2@rentalhouse.com', '0901234603', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', true, '/images/default-avatar.png', 'Hồ Thị Bị Từ Chối', '680 Đường EFG, Quận Phú Nhuận, TP.HCM', '1987-02-08', 3, NOW(), NOW());


