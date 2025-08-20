-- Database Migration Script for Enhanced Rental System
-- Run this script to update your database for the new rental features

-- 0. Fix notifications table type column (CRITICAL FIX)
-- This fixes the "Data truncated for column 'type'" error
ALTER TABLE notifications 
MODIFY COLUMN type VARCHAR(50) NOT NULL;

-- 1. Add new columns to rentals table
ALTER TABLE rentals 
ADD COLUMN price_per_day DOUBLE,
ADD COLUMN number_of_days INT,
ADD COLUMN deposit_amount DOUBLE,
ADD COLUMN payment_status ENUM('PENDING', 'PARTIAL', 'COMPLETED', 'REFUNDED', 'FAILED') DEFAULT 'PENDING',
ADD COLUMN special_requests TEXT,
ADD COLUMN cancellation_reason VARCHAR(500),
ADD COLUMN cancelled_at DATETIME,
ADD COLUMN cancelled_by_id BIGINT,
ADD CONSTRAINT fk_rentals_cancelled_by FOREIGN KEY (cancelled_by_id) REFERENCES users(id);

-- 2. Update existing rentals with calculated values
UPDATE rentals 
SET 
    price_per_day = CASE 
        WHEN total_price IS NOT NULL AND DATEDIFF(end_date, start_date) > 0 
        THEN total_price / DATEDIFF(end_date, start_date)
        ELSE NULL 
    END,
    number_of_days = CASE 
        WHEN start_date IS NOT NULL AND end_date IS NOT NULL 
        THEN DATEDIFF(end_date, start_date)
        ELSE NULL 
    END
WHERE price_per_day IS NULL OR number_of_days IS NULL;

-- 3. Add new status values to rentals table
-- Note: If your database doesn't support modifying ENUM, you may need to recreate the table
-- For MySQL 8.0+, you can use:
ALTER TABLE rentals 
MODIFY COLUMN status ENUM('SCHEDULED', 'PENDING', 'APPROVED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELED', 'REJECTED') NOT NULL;

-- 4. Add indexes for better performance
CREATE INDEX idx_rentals_status ON rentals(status);
CREATE INDEX idx_rentals_payment_status ON rentals(payment_status);
CREATE INDEX idx_rentals_start_date ON rentals(start_date);
CREATE INDEX idx_rentals_end_date ON rentals(end_date);
CREATE INDEX idx_rentals_house_dates ON rentals(house_id, start_date, end_date);
CREATE INDEX idx_rentals_renter_status ON rentals(renter_id, status);
CREATE INDEX idx_rentals_host_status ON rentals(house_id, status);

-- 5. Add new columns to houses table for daily pricing
ALTER TABLE houses 
ADD COLUMN price_per_day DOUBLE;

-- 6. Update houses with calculated daily prices
UPDATE houses 
SET price_per_day = CASE 
    WHEN price IS NOT NULL THEN price / 30.0
    ELSE NULL 
END
WHERE price_per_day IS NULL AND price IS NOT NULL;

-- 7. Add index for house daily pricing
CREATE INDEX idx_houses_price_per_day ON houses(price_per_day);

-- 8. Create view for rental statistics
CREATE VIEW rental_statistics AS
SELECT 
    h.id as house_id,
    h.title as house_title,
    h.host_id,
    u.full_name as host_name,
    COUNT(r.id) as total_rentals,
    SUM(CASE WHEN r.status = 'CHECKED_OUT' THEN r.total_price ELSE 0 END) as total_income,
    AVG(r.number_of_days) as avg_rental_days,
    COUNT(CASE WHEN r.status = 'CANCELED' THEN 1 END) as canceled_rentals
FROM houses h
LEFT JOIN rentals r ON h.id = r.house_id
LEFT JOIN users u ON h.host_id = u.id
GROUP BY h.id, h.title, h.host_id, u.full_name;

-- 9. Create view for user rental history
CREATE VIEW user_rental_history AS
SELECT 
    r.id as rental_id,
    r.renter_id,
    u.full_name as renter_name,
    h.id as house_id,
    h.title as house_title,
    r.start_date,
    r.end_date,
    r.number_of_days,
    r.total_price,
    r.price_per_day,
    r.status,
    r.payment_status,
    r.created_at
FROM rentals r
JOIN users u ON r.renter_id = u.id
JOIN houses h ON r.house_id = h.id
ORDER BY r.created_at DESC;

-- 10. Insert sample data for testing (optional)
-- INSERT INTO rentals (house_id, renter_id, start_date, end_date, status, total_price, price_per_day, number_of_days, payment_status, created_at, updated_at)
-- VALUES 
-- (1, 2, '2024-01-15 00:00:00', '2024-01-17 23:59:59', 'SCHEDULED', 600000, 300000, 2, 'PENDING', NOW(), NOW()),
-- (1, 3, '2024-01-20 00:00:00', '2024-01-25 23:59:59', 'APPROVED', 1500000, 300000, 5, 'COMPLETED', NOW(), NOW());

-- 11. Update application.properties or application.yaml
-- Add these properties to your application configuration:
/*
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
        show_sql: false
*/

-- 12. Verify the migration
SELECT 
    'rentals' as table_name,
    COUNT(*) as total_records,
    COUNT(price_per_day) as records_with_daily_price,
    COUNT(number_of_days) as records_with_days_count
FROM rentals
UNION ALL
SELECT 
    'houses' as table_name,
    COUNT(*) as total_records,
    COUNT(price_per_day) as records_with_daily_price,
    NULL as records_with_days_count
FROM houses;

-- Migration completed successfully!
-- Remember to restart your Spring Boot application after running this script. 