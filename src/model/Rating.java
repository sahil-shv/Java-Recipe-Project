package model;

import java.time.LocalDateTime;

public class Rating {
    private int ratingId;
    private int recipeId;
    private int userId;
    private int rating; // 1-5
    private LocalDateTime createdAt;

    public Rating() {
    }

    public Rating(int recipeId, int userId, int rating) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.rating = rating;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

