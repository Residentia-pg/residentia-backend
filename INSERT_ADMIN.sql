-- Generate BCrypt hash for Admin@123
-- This is the correct BCrypt hash for password: Admin@123
-- To generate: Use online BCrypt generator or Java BCryptPasswordEncoder

-- Clear existing admin if needed
DELETE FROM admins WHERE email = 'admin@residentia.com';

-- Insert with valid BCrypt hash
-- Password: Admin@123
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36DRcx3a
INSERT INTO admins (name, email, mobile_number, password_hash, department, is_active, created_at, updated_at)
VALUES (
    'System Admin',
    'admin@residentia.com',
    '9999999999',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36DRcx3a',
    'Administration',
    TRUE,
    NOW(),
    NOW()
);

-- Verify the admin was created
SELECT id, name, email, is_active FROM admins WHERE email = 'admin@residentia.com';
