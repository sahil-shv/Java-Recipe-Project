package model;

import java.sql.Timestamp;

/**
 * Comment model class representing a comment on a recipe
 */
public class Comment {
    private int commentId;
    private int userId;
    private int recipeId;
    private String comment;
    private Timestamp createdAt;
    
    // Additional field for joined queries
    private String userName;
    
    // Default constructor
    public Comment() {}
    
    // Constructor with essential fields
    public Comment(int userId, int recipeId, String comment) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.comment = comment;
    }
    
    // Constructor with all fields
    public Comment(int commentId, int userId, int recipeId, String comment, Timestamp createdAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.recipeId = recipeId;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }
    
    public void setCommentId(int commentId) {
        this.commentId = commentId;
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
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
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
    
    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", recipeId=" + recipeId +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}