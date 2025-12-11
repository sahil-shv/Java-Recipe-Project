package service;

import dao.CommentDAO;
import model.Comment;
import utils.Logger;

public class CommentService {
    private final CommentDAO commentDAO = new CommentDAO();

    public boolean addComment(Comment comment) {
        if (comment.getCommentText() == null || comment.getCommentText().trim().isEmpty()) {
            Logger.warning("Comment addition failed: Comment text is required");
            return false;
        }
        if (comment.getRecipeId() <= 0) {
            Logger.warning("Comment addition failed: Invalid recipe ID");
            return false;
        }
        if (comment.getUserId() <= 0) {
            Logger.warning("Comment addition failed: Invalid user ID");
            return false;
        }

        return commentDAO.addComment(comment);
    }
}

