-- Create database (run this manually in Render shell)
-- CREATE DATABASE recipeshare;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password CHAR(64) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    profile_image VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Recipes table
CREATE TABLE IF NOT EXISTS recipes (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    ingredients TEXT NOT NULL,
    steps TEXT NOT NULL,
    category VARCHAR(100) NULL,
    image VARCHAR(255) NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recipes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Comments table
CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    recipe_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Ratings table
CREATE TABLE IF NOT EXISTS ratings (
    rating_id SERIAL PRIMARY KEY,
    recipe_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, recipe_id),
    CONSTRAINT fk_ratings_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT fk_ratings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Email verification tokens table
CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_email_tokens_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert demo user
INSERT INTO users (name, email, password, salt, is_verified) VALUES
('Demo Chef', 'chef@example.com', 'd3ad9315b7be5dd53b31a273b3b3aba5defe700808305aa16a3062b76658a791', 'demosalt123', TRUE)
ON CONFLICT (email) DO NOTHING;

-- Insert demo recipe
INSERT INTO recipes (title, ingredients, steps, category, image, user_id)
SELECT 'Classic Pancakes', '2 cups flour
2 eggs
1.5 cups milk', 'Mix ingredients and cook on a skillet.', 'Breakfast', NULL, 1
WHERE EXISTS (SELECT 1 FROM users WHERE id = 1);

-- Insert demo comment
INSERT INTO comments (recipe_id, user_id, comment_text)
SELECT 1, 1, 'These pancakes are delicious!'
WHERE EXISTS (SELECT 1 FROM recipes WHERE id = 1);
