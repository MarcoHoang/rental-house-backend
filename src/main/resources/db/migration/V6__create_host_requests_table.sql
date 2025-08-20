-- Migration để tạo bảng host_requests
CREATE TABLE IF NOT EXISTS host_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    national_id VARCHAR(50),
    proof_of_ownership_url VARCHAR(500),
    id_front_photo_url VARCHAR(500),
    id_back_photo_url VARCHAR(500),
    reason VARCHAR(500),
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_request_date (request_date)
);

-- Thêm comment cho bảng
ALTER TABLE host_requests COMMENT = 'Bảng lưu trữ đơn đăng ký làm chủ nhà';
