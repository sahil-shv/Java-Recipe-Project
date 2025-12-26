package model;

import java.sql.Timestamp;

/**
 * Like model class representing a like on a recipe
 */
public class Like {
    private int likeId;
    private int userId;
    private int recipeId;
    private Timestamp createdAt;
    
    // Default constructor
    public Like() {}
    
    // Constructor with essential fields
    public Like(int userId, int recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
    }
    
    // Constructor with all fields
    public Like(int likeId, int userId, int recipeId, Timestamp createdAt) {
        this.likeId = likeId;
        this.userId = userId;
        this.recipeId = recipeId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getLikeId() {
        return likeId;
    }
    
    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getRecipeId() {
        return recipeId;
    }
    
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", userId=" + userId +
                ", recipeId=" + recipeId +
                ", createdAt=" + createdAt +
                '}';
    }
}