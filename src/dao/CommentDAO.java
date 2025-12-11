package dao;

import model.Comment;
import model.User;
import util.DBConnection;
import utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    public boolean addComment(Comment comment) {
        String sql = "INSERT INTO comments (recipe_id, user_id, comment_text, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, comment.getRecipeId());
            stmt.setInt(2, comment.getUserId());
            stmt.setString(3, comment.getCommentText());
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                Logger.info("Comment added for recipe " + comment.getRecipeId() + " by user " + comment.getUserId());
            }
            return result;
        } catch (SQLException e) {
            Logger.error("Error adding comment", e);
            return false;
        }
    }

    public List<Comment> getCommentsByRecipeId(int recipeId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.id, c.recipe_id, c.user_id, c.comment_text, c.created_at, u.name, u.email FROM comments c JOIN users u ON c.user_id = u.id WHERE c.recipe_id = ? ORDER BY c.created_at ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setId(rs.getInt("id"));
                    comment.setRecipeId(rs.getInt("recipe_id"));
                    comment.setUserId(rs.getInt("user_id"));
                    comment.setCommentText(rs.getString("comment_text"));
                    if (rs.getTimestamp("created_at") != null) {
                        comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }

                    User commenter = new User();
                    commenter.setId(rs.getInt("user_id"));
                    commenter.setName(rs.getString("name"));
                    commenter.setEmail(rs.getString("email"));
                    comment.setCommenter(commenter);
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            Logger.error("Error getting comments for recipe ID: " + recipeId, e);
        }
        return comments;
    }
}
