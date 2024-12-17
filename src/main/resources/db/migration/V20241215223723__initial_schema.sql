-- Create User Table
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       status VARCHAR(20) NOT NULL DEFAULT 'INACTIVE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       version INT DEFAULT 0 NOT NULL
);

-- Create Task Table
CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       description TEXT,
                       status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                       user_id UUID NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create Permissions Table
CREATE TABLE user_permissions (
                                  user_id UUID NOT NULL,
                                  permission VARCHAR(50) NOT NULL,
                                  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- Add the CHECK constraint separately for status (H2 Compatibility)
ALTER TABLE tasks
    ADD CONSTRAINT chk_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED'));

ALTER TABLE tasks ALTER COLUMN status SET DEFAULT 'PENDING';
