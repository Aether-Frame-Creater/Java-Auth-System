-- Sample SQL schema for the authentication system
-- JPA will auto-create tables, but this is provided as reference

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    reset_token VARCHAR(255)
);

-- Sample test users (passwords are BCrypt-encoded)
-- Password for both test users: "password123"
INSERT INTO users (username, email, password) VALUES
    ('john_doe', 'john@example.com', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQkfAjkMBcGm5y7GqKX5VqyV0gqOZm'),
    ('jane_smith', 'jane@example.com', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQkfAjkMBcGm5y7GqKX5VqyV0gqOZm')
ON CONFLICT (username) DO NOTHING;
