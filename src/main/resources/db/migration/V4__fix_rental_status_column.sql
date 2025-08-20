-- Migration: Fix rental status column
-- Version: V4
-- Description: Fix the status column in rentals table to accommodate all enum values

-- Kiểm tra và sửa cột status nếu cần
ALTER TABLE rentals MODIFY COLUMN status VARCHAR(20) NOT NULL;

-- Cập nhật các giá trị status hiện tại nếu cần
UPDATE rentals SET status = 'PENDING' WHERE status = 'pending';
UPDATE rentals SET status = 'APPROVED' WHERE status = 'approved';
UPDATE rentals SET status = 'REJECTED' WHERE status = 'rejected';
UPDATE rentals SET status = 'SCHEDULED' WHERE status = 'scheduled';
UPDATE rentals SET status = 'CHECKED_IN' WHERE status = 'checked_in';
UPDATE rentals SET status = 'CHECKED_OUT' WHERE status = 'checked_out';
UPDATE rentals SET status = 'CANCELED' WHERE status = 'canceled';
