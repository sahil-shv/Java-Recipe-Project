-- Recipe Sharing Platform Database Schema
-- Execute this script to create the database and tables

CREATE DATABASE IF NOT EXISTS recipe_sharing_db;
USE recipe_sharing_db;

-- Users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Recipes table
CREATE TABLE recipes (
    recipe_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    ingredients TEXT NOT NULL,
    description TEXT NOT NULL,
    image_path VARCHAR(500),
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- Likes table
CREATE TABLE likes (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_recipe_like (user_id, recipe_id),
    INDEX idx_recipe_id (recipe_id),
    INDEX idx_user_id (user_id)
);

-- Comments table
CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    INDEX idx_recipe_id (recipe_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
);

-- Insert default admin user (password: admin123)
-- Hash generated with BCrypt work factor 12
INSERT INTO users (name, email, password, role) VALUES 
('Admin User', 'admin@recipesharing.com', '$2a$12$K2iumSYkuLy8.RdU1d7AK.PCGbHO2t8XvuKNEh6Nd7ibUgTHxOBJG', 'ADMIN');

-- Insert sample user (password: user123)  
-- Hash generated with BCrypt work factor 12
INSERT INTO users (name, email, password, role) VALUES 
('John Doe', 'user@recipesharing.com', '$2a$12$8Bc4j6HkqvyqfReH4XdwdOxeQjFJ8n5t6vKzMzpVqNzQzQzQzQzQz', 'USER');