-- Migration để thêm các cột liên quan cho notifications
-- Thêm cột house_id và review_id vào bảng notifications

ALTER TABLE notifications 
ADD COLUMN house_id BIGINT,
ADD COLUMN review_id BIGINT;

-- Thêm foreign key constraints
ALTER TABLE notifications 
ADD CONSTRAINT fk_notifications_house 
FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE;

ALTER TABLE notifications 
ADD CONSTRAINT fk_notifications_review 
FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE;

-- Thêm index để tối ưu hiệu suất truy vấn
CREATE INDEX idx_notifications_house_id ON notifications(house_id);
CREATE INDEX idx_notifications_review_id ON notifications(review_id);
