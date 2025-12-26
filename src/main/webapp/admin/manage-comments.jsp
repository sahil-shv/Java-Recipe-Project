<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Comments - Recipe Sharing Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="../css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary-custom">
        <div class="container">
            <a class="navbar-brand" href="dashboard">
                <i class="fas fa-shield-alt me-2"></i>Admin Panel
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="dashboard">
                            <i class="fas fa-tachometer-alt me-1"></i>Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="manage-users">
                            <i class="fas fa-users me-1"></i>Users
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="manage-recipes">
                            <i class="fas fa-book me-1"></i>Recipes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="manage-comments">
                            <i class="fas fa-comments me-1"></i>Comments
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-shield me-1"></i>${sessionScope.user.name}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="../user/dashboard">
                                <i class="fas fa-user me-2"></i>User View
                            </a></li>
                            <li><hr class="dropdown-divider"></li>
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
        <!-- Page Header -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title">
                            <i class="fas fa-comments me-2"></i>Manage Comments
                        </h2>
                        <p class="card-text text-muted">Monitor and moderate user comments</p>
                    </div>
                </div>
            </div>
        </div>
        
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
        
        <!-- Comments List -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-list me-2"></i>All Comments (${comments.size()})
                        </h5>
                    </div>
                    
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty comments}">
                                <div class="text-center py-5">
                                    <i class="fas fa-comment-slash fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No comments found</h5>
                                    <p class="text-muted">No comments have been posted yet.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="row">
                                    <c:forEach var="comment" items="${comments}">
                                        <div class="col-12 mb-3">
                                            <div class="card border-left-primary">
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-md-8">
                                                            <div class="d-flex align-items-start">
                                                                <div class="bg-primary rounded-circle d-flex align-items-center justify-content-center me-3" 
                                                                     style="width: 40px; height: 40px; min-width: 40px;">
                                                                    <i class="fas fa-user text-white"></i>
                                                                </div>
                                                                <div class="flex-grow-1">
                                                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                                                        <div>
                                                                            <h6 class="mb-0">${comment.userName}</h6>
                                                                            <small class="text-muted">
                                                                                <fmt:formatDate value="${comment.createdAt}" pattern="MMM dd, yyyy 'at' HH:mm" />
                                                                            </small>
                                                                        </div>
                                                                        <span class="badge bg-info">Comment #${comment.commentId}</span>
                                                                    </div>
                                                                    <p class="mb-2">${comment.comment}</p>
                                                                    <small class="text-muted">
                                                                        <i class="fas fa-book me-1"></i>
                                                                        Recipe ID: ${comment.recipeId}
                                                                    </small>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4 text-end">
                                                            <div class="btn-group" role="group">
                                                                <a href="../user/recipe-detail?id=${comment.recipeId}" 
                                                                   class="btn btn-sm btn-outline-primary" title="View Recipe">
                                                                    <i class="fas fa-eye me-1"></i>View Recipe
                                                                </a>
                                                                
                                                                <form method="post" action="manage-comments" class="d-inline">
                                                                    <input type="hidden" name="commentId" value="${comment.commentId}">
                                                                    <input type="hidden" name="action" value="delete">
                                                                    <button type="submit" class="btn btn-sm btn-danger btn-delete" 
                                                                            title="Delete Comment" data-item-type="comment">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Moderation Guidelines -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h6 class="card-title mb-0">
                            <i class="fas fa-gavel me-2"></i>Moderation Guidelines
                        </h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6 class="text-danger">Remove comments that contain:</h6>
                                <ul class="list-unstyled">
                                    <li><i class="fas fa-times text-danger me-2"></i>Offensive or inappropriate language</li>
                                    <li><i class="fas fa-times text-danger me-2"></i>Spam or promotional content</li>
                                    <li><i class="fas fa-times text-danger me-2"></i>Personal attacks or harassment</li>
                                    <li><i class="fas fa-times text-danger me-2"></i>Off-topic or irrelevant content</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6 class="text-success">Keep comments that are:</h6>
                                <ul class="list-unstyled">
                                    <li><i class="fas fa-check text-success me-2"></i>Constructive feedback about recipes</li>
                                    <li><i class="fas fa-check text-success me-2"></i>Helpful cooking tips and suggestions</li>
                                    <li><i class="fas fa-check text-success me-2"></i>Positive community interactions</li>
                                    <li><i class="fas fa-check text-success me-2"></i>Questions about ingredients or methods</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../js/main.js"></script>
</body>
</html>