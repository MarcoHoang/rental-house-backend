-- Database Schema for Rental House Application
-- Tạo tất cả các bảng cần thiết cho ứng dụng

-- 1. Bảng roles (Vai trò người dùng)
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('ADMIN', 'HOST', 'USER') NOT NULL UNIQUE
);

-- 2. Bảng users (Người dùng)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    facebook_account_id VARCHAR(255),
    google_account_id VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    img VARCHAR(255) DEFAULT '/images/default-avatar.png',
    full_name VARCHAR(255),
    address TEXT,
    birth_date DATE,
    role_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 3. Bảng hosts (Chủ nhà)
CREATE TABLE hosts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    national_id VARCHAR(255),
    proof_of_ownership_url VARCHAR(255),
    address TEXT,
    approved_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 4. Bảng host_requests (Yêu cầu làm chủ nhà)
CREATE TABLE host_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL,
    national_id VARCHAR(255),
    proof_of_ownership_url VARCHAR(255),
    id_front_photo_url VARCHAR(255),
    id_back_photo_url VARCHAR(255),
    reason TEXT,
    request_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    processed_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 5. Bảng houses (Nhà cho thuê)
CREATE TABLE houses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    host_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(255) NOT NULL,
    price DOUBLE,
    area DOUBLE,
    latitude DOUBLE,
    longitude DOUBLE,
    status ENUM('AVAILABLE', 'RENTED', 'INACTIVE') NOT NULL,
    house_type ENUM('APARTMENT', 'VILLA', 'TOWNHOUSE', 'BOARDING_HOUSE', 'WHOLE_HOUSE') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (host_id) REFERENCES users(id)
);

-- 6. Bảng house_images (Hình ảnh nhà)
CREATE TABLE house_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    house_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    sort_order INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE
);

-- 7. Bảng rentals (Đơn thuê nhà)
CREATE TABLE rentals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    house_id BIGINT NOT NULL,
    renter_id BIGINT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'SCHEDULED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELED') NOT NULL,
    total_price DOUBLE,
    guest_count INT,
    message_to_host TEXT,
    reject_reason VARCHAR(500),
    cancel_reason VARCHAR(500),
    canceled_at DATETIME,
    approved_at DATETIME,
    approved_by BIGINT,
    rejected_at DATETIME,
    rejected_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (house_id) REFERENCES houses(id),
    FOREIGN KEY (renter_id) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    FOREIGN KEY (rejected_by) REFERENCES users(id)
);

-- 8. Bảng reviews (Đánh giá)
CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    house_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    is_visible BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (house_id) REFERENCES houses(id)
);

-- 9. Bảng notifications (Thông báo)
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receiver_id BIGINT NOT NULL,
    type ENUM('RENTAL_REQUEST', 'RENTAL_APPROVED', 'RENTAL_REJECTED', 'RENTAL_CANCELED', 'RENTAL_BOOKED', 'REVIEW_LOW_RATING', 'REVIEW_MEDIUM_RATING', 'REVIEW_HIGH_RATING', 'HOST_REQUEST_APPROVED', 'HOST_REQUEST_REJECTED', 'HOUSE_DELETED', 'GENERAL') NOT NULL,
    content TEXT NOT NULL,
    rental_id BIGINT,
    house_id BIGINT,
    review_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (rental_id) REFERENCES rentals(id),
    FOREIGN KEY (house_id) REFERENCES houses(id),
    FOREIGN KEY (review_id) REFERENCES reviews(id)
);

-- Tạo indexes để tối ưu hiệu suất
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_houses_host_id ON houses(host_id);
CREATE INDEX idx_houses_status ON houses(status);
CREATE INDEX idx_rentals_house_id ON rentals(house_id);
CREATE INDEX idx_rentals_renter_id ON rentals(renter_id);
CREATE INDEX idx_rentals_status ON rentals(status);
CREATE INDEX idx_reviews_house_id ON reviews(house_id);
CREATE INDEX idx_notifications_receiver_id ON notifications(receiver_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);

-- Insert dữ liệu mẫu cho roles
INSERT INTO roles (name) VALUES 
('ADMIN'),
('HOST'),
('USER');

-- Insert admin user mặc định (password: admin123)
INSERT INTO users (username, email, phone, password, full_name, role_id, active) VALUES 
('admin', 'admin@rentalhouse.com', '0123456789', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Administrator', 1, TRUE);
