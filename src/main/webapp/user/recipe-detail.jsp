<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${recipe.title} - Recipe Sharing Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="../css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary-custom">
        <div class="container">
            <a class="navbar-brand" href="dashboard">
                <i class="fas fa-utensils me-2"></i>Recipe Sharing
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="dashboard">
                            <i class="fas fa-home me-1"></i>Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="add-recipe">
                            <i class="fas fa-plus me-1"></i>Add Recipe
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="ai-recipe">
                            <i class="fas fa-robot me-1"></i>AI Recipe
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user me-1"></i>${user.name}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="../logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Logout
                            </a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    
    <div class="container mt-4">
        <!-- Display messages -->
        <c:if test="${param.error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>
                ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${param.success != null}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                ${param.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <div class="row">
            <!-- Recipe Details -->
            <div class="col-lg-8">
                <div class="card">
                    <c:if test="${not empty recipe.imagePath}">
                        <img src="../${recipe.imagePath}" class="card-img-top" alt="${recipe.title}" style="height: 400px; object-fit: cover;">
                    </c:if>
                    
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <div>
                                <h1 class="card-title">${recipe.title}</h1>
                                <p class="text-muted mb-2">
                                    By <strong>${recipe.userName}</strong> • 
                                    <fmt:formatDate value="${recipe.createdAt}" pattern="MMMM dd, yyyy" />
                                </p>
                                <div class="mb-3">
                                    <span class="badge status-${recipe.status.toLowerCase()} me-2">${recipe.status}</span>
                                    <c:if test="${recipe.userId != user.userId && recipe.status == 'APPROVED'}">
                                        <form method="post" action="like-recipe" class="d-inline">
                                            <input type="hidden" name="recipeId" value="${recipe.recipeId}">
                                            <input type="hidden" name="action" value="${recipe.likedByCurrentUser ? 'unlike' : 'like'}">
                                            <button type="submit" class="btn btn-link p-0 like-btn ${recipe.likedByCurrentUser ? 'liked' : ''}" 
                                                    data-recipe-id="${recipe.recipeId}">
                                                <i class="fas fa-heart me-1"></i>${recipe.likeCount} Likes
                                            </button>
                                        </form>
                                    </c:if>
                                    <c:if test="${recipe.userId == user.userId || recipe.status != 'APPROVED'}">
                                        <span class="text-muted">
                                            <i class="fas fa-heart me-1"></i>${recipe.likeCount} Likes
                                        </span>
                                    </c:if>
                                    <span class="text-muted ms-3">
                                        <i class="fas fa-comment me-1"></i>${recipe.commentCount} Comments
                                    </span>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <h4 class="mb-3">
                                    <i class="fas fa-list-ul me-2"></i>Ingredients
                                </h4>
                                <div class="bg-light p-3 rounded">
                                    <pre class="mb-0" style="white-space: pre-wrap; font-family: inherit;">${recipe.ingredients}</pre>
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <h4 class="mb-3">
                                    <i class="fas fa-utensils me-2"></i>Instructions
                                </h4>
                                <div class="bg-light p-3 rounded">
                                    <pre class="mb-0" style="white-space: pre-wrap; font-family: inherit;">${recipe.description}</pre>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Comments Section -->
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-comments me-2"></i>Comments (${comments.size()})
                        </h5>
                    </div>
                    
                    <div class="card-body">
                        <!-- Add Comment Form (only for approved recipes and not own recipe) -->
                        <c:if test="${recipe.status == 'APPROVED' && recipe.userId != user.userId}">
                            <form action="add-comment" method="post" class="mb-4">
                                <input type="hidden" name="recipeId" value="${recipe.recipeId}">
                                <div class="mb-3">
                                    <textarea class="form-control" name="comment" rows="3" 
                                              placeholder="Share your thoughts about this recipe..." 
                                              required maxlength="500"></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm">
                                    <i class="fas fa-paper-plane me-1"></i>Post Comment
                                </button>
                            </form>
                            <hr>
                        </c:if>
                        
                        <!-- Comments List -->
                        <div class="comments-list" style="max-height: 500px; overflow-y: auto;">
                            <c:choose>
                                <c:when test="${empty comments}">
                                    <div class="text-center text-muted py-4">
                                        <i class="fas fa-comment-slash fa-2x mb-2"></i>
                                        <p>No comments yet.</p>
                                        <c:if test="${recipe.status == 'APPROVED' && recipe.userId != user.userId}">
                                            <small>Be the first to comment!</small>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="comment" items="${comments}">
                                        <div class="comment-item">
                                            <div class="comment-author">${comment.userName}</div>
                                            <div class="comment-date">
                                                <fmt:formatDate value="${comment.createdAt}" pattern="MMM dd, yyyy 'at' HH:mm" />
                                            </div>
                                            <div class="comment-text mt-2">${comment.comment}</div>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                
                <!-- Recipe Status Info (for own recipes) -->
                <c:if test="${recipe.userId == user.userId}">
                    <div class="card mt-3">
                        <div class="card-header">
                            <h6 class="card-title mb-0">
                                <i class="fas fa-info-circle me-2"></i>Recipe Status
                            </h6>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${recipe.status == 'PENDING'}">
                                    <div class="alert alert-warning mb-0">
                                        <i class="fas fa-clock me-2"></i>
                                        Your recipe is pending admin approval.
                                    </div>
                                </c:when>
                                <c:when test="${recipe.status == 'APPROVED'}">
                                    <div class="alert alert-success mb-0">
                                        <i class="fas fa-check-circle me-2"></i>
                                        Your recipe has been approved and is visible to the community!
                                    </div>
                                </c:when>
                                <c:when test="${recipe.status == 'REJECTED'}">
                                    <div class="alert alert-danger mb-0">
                                        <i class="fas fa-times-circle me-2"></i>
                                        Your recipe was not approved. Please review and resubmit.
                                    </div>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        
        <!-- Back Button -->
        <div class="row mt-4">
            <div class="col-12">
                <a href="dashboard" class="btn btn-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
                </a>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../js/main.js"></script>
</body>
</html>