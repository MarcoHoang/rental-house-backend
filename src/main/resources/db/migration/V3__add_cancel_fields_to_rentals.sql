ALTER TABLE rentals 
ADD COLUMN cancel_reason VARCHAR(500),
ADD COLUMN canceled_at TIMESTAMP;
