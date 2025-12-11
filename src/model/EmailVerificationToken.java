package model;

import java.time.LocalDateTime;

public class EmailVerificationToken {
    private int id;
    private int userId;
    private String token;
    private LocalDateTime expiry;
    private LocalDateTime createdAt;

    public EmailVerificationToken() {
    }

    public EmailVerificationToken(int userId, String token, LocalDateTime expiry) {
        this.userId = userId;
        this.token = token;
        this.expiry = expiry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

