-- Migration: Create reviews table
-- Version: V2
-- Description: Create reviews table for storing user reviews of houses

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    house_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    is_visible BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (house_id) REFERENCES houses(id) ON DELETE CASCADE,
    
    -- Unique constraint: one review per user per house
    UNIQUE KEY unique_user_house_review (user_id, house_id),
    
    -- Indexes for performance
    INDEX idx_house_id (house_id),
    INDEX idx_user_id (user_id),
    INDEX idx_rating (rating),
    INDEX idx_created_at (created_at)
);

-- Insert sample data for testing (optional)
INSERT INTO reviews (user_id, house_id, rating, comment, is_visible) VALUES
(1, 1, 5, 'Nhà rất đẹp và sạch sẽ!', TRUE),
(2, 1, 4, 'Vị trí thuận tiện, giá cả hợp lý', TRUE),
(1, 2, 5, 'Chủ nhà rất thân thiện và nhiệt tình', TRUE);
