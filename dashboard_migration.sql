-- Migration để thêm các column cho dashboard stats
USE rental_house;

-- Thêm column rental_count và total_revenue vào bảng houses
ALTER TABLE houses 
ADD COLUMN rental_count BIGINT DEFAULT 0,
ADD COLUMN total_revenue DECIMAL(15,2) DEFAULT 0.00;

-- Thêm column MAINTENANCE vào enum status nếu chưa có
-- (MySQL không hỗ trợ ALTER ENUM trực tiếp, cần tạo lại column)
ALTER TABLE houses 
MODIFY COLUMN status ENUM('AVAILABLE', 'RENTED', 'INACTIVE', 'MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE';

-- Cập nhật dữ liệu mẫu cho rental_count và total_revenue
UPDATE houses h 
SET 
    rental_count = (
        SELECT COUNT(*) 
        FROM rentals r 
        WHERE r.house_id = h.id
    ),
    total_revenue = (
        SELECT COALESCE(SUM(r.total_price), 0) 
        FROM rentals r 
        WHERE r.house_id = h.id
    );

-- Tạo index để tối ưu query
CREATE INDEX idx_houses_rental_count ON houses(rental_count DESC);
CREATE INDEX idx_houses_total_revenue ON houses(total_revenue DESC);
CREATE INDEX idx_houses_status ON houses(status);
CREATE INDEX idx_houses_created_at ON houses(created_at);
CREATE INDEX idx_rentals_created_at ON rentals(created_at);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_role_name ON users(role_id); 