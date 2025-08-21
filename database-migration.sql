-- Migration script to allow multiple host requests per user
-- This script removes the unique constraint on user_id in host_requests table

-- Step 1: Drop the unique constraint on user_id
ALTER TABLE host_requests DROP CONSTRAINT IF EXISTS uk_host_requests_user_id;

-- Step 2: Add a regular index on user_id for performance (optional)
CREATE INDEX IF NOT EXISTS idx_host_requests_user_id ON host_requests(user_id);

-- Step 3: Add a composite index for better query performance
CREATE INDEX IF NOT EXISTS idx_host_requests_user_status ON host_requests(user_id, status);

-- Step 4: Add an index for request date sorting
CREATE INDEX IF NOT EXISTS idx_host_requests_request_date ON host_requests(request_date DESC);

-- Note: After running this migration, the system will allow multiple host requests per user
-- Each user can now have multiple requests with different statuses (PENDING, APPROVED, REJECTED)
