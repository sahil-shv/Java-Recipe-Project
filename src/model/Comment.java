package model;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int recipeId;
    private int userId;
    private String commentText;
    private LocalDateTime createdAt;
    private User commenter;

    public Comment() {
    }

    public Comment(int id, int recipeId, int userId, String commentText, LocalDateTime createdAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
}
