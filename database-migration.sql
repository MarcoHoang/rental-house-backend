-- Migration script to migrate from token-based to OTP-based password reset

-- Step 1: Drop the old password_reset_tokens table if it exists
DROP TABLE IF EXISTS password_reset_tokens;

-- Step 2: Create the new password_reset_otp table
CREATE TABLE IF NOT EXISTS password_reset_otp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    otp VARCHAR(6) NOT NULL,
    expiry_date DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Step 3: Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_password_reset_otp_otp ON password_reset_otp(otp);
CREATE INDEX IF NOT EXISTS idx_password_reset_otp_user_id ON password_reset_otp(user_id);
CREATE INDEX IF NOT EXISTS idx_password_reset_otp_expiry ON password_reset_otp(expiry_date);

-- Note: Migration completed - system now uses OTP instead of tokens for password reset
