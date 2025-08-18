-- Tạo bảng favorites để lưu trữ danh sách yêu thích của người dùng
CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    house_id BIGINT NOT NULL,
    unique_constraint VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE,
    
    INDEX idx_user_id (user_id),
    INDEX idx_house_id (house_id),
    INDEX idx_unique_constraint (unique_constraint)
);
