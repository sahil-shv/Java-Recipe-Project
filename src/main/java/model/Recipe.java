package model;

import java.sql.Timestamp;

/**
 * Recipe model class representing a recipe in the system
 */
public class Recipe {
    private int recipeId;
    private int userId;
    private String title;
    private String ingredients;
    private String description;
    private String imagePath;
    private String status;
    private Timestamp createdAt;
    
    // Additional fields for joined queries
    private String userName;
    private int likeCount;
    private int commentCount;
    private boolean likedByCurrentUser;
    
    // Default constructor
    public Recipe() {}
    
    // Constructor with essential fields
    public Recipe(int userId, String title, String ingredients, String description, String imagePath) {
        this.userId = userId;
        this.title = title;
        this.ingredients = ingredients;
        this.description = description;
        this.imagePath = imagePath;
        this.status = "PENDING";
    }
    
    // Constructor with all fields
    public Recipe(int recipeId, int userId, String title, String ingredients, String description, 
                  String imagePath, String status, Timestamp createdAt) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.title = title;
        this.ingredients = ingredients;
        this.description = description;
        this.imagePath = imagePath;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public int getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    
    public int getCommentCount() {
        return commentCount;
    }
    
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    
    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }
    
    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }
    
    // Utility methods
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }
    
    public boolean isRejected() {
        return "REJECTED".equals(status);
    }
    
    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}