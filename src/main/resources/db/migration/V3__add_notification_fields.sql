-- Migration để thêm các trường mới cho bảng notifications
ALTER TABLE notifications 
ADD COLUMN house_id BIGINT,
ADD COLUMN review_id BIGINT,
ADD CONSTRAINT fk_notifications_house FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE,
ADD CONSTRAINT fk_notifications_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE;

-- Cập nhật enum Type để thêm các loại thông báo mới
-- Lưu ý: MySQL không hỗ trợ ALTER TYPE, nên cần tạo lại bảng hoặc sử dụng ENUM mới
-- Trong trường hợp này, các giá trị mới sẽ được tự động hỗ trợ khi ứng dụng khởi động
