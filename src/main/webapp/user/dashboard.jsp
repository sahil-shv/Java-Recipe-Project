<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard - Recipe Sharing Platform</title>
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
                        <a class="nav-link active" href="dashboard">
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
        <!-- Welcome Section -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title">Welcome back, ${user.name}!</h2>
                        <p class="card-text text-muted">Discover and share amazing recipes with our community.</p>
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
        
        <!-- Quick Actions -->
        <div class="row mb-4">
            <div class="col-md-4 mb-3">
                <a href="add-recipe" class="text-decoration-none">
                    <div class="card text-center h-100 border-primary">
                        <div class="card-body">
                            <i class="fas fa-plus-circle fa-3x text-primary mb-3"></i>
                            <h5 class="card-title">Add New Recipe</h5>
                            <p class="card-text">Share your favorite recipe with the community</p>
                        </div>
                    </div>
                </a>
            </div>
            
            <div class="col-md-4 mb-3">
                <a href="ai-recipe" class="text-decoration-none">
                    <div class="card text-center h-100 border-info">
                        <div class="card-body">
                            <i class="fas fa-robot fa-3x text-info mb-3"></i>
                            <h5 class="card-title">AI Recipe Generator</h5>
                            <p class="card-text">Get recipe suggestions based on your ingredients</p>
                        </div>
                    </div>
                </a>
            </div>
            
            <div class="col-md-4 mb-3">
                <div class="card text-center h-100 border-success">
                    <div class="card-body">
                        <i class="fas fa-chart-line fa-3x text-success mb-3"></i>
                        <h5 class="card-title">Your Stats</h5>
                        <p class="card-text">
                            <strong>${userRecipes.size()}</strong> recipes shared<br>
                            <c:set var="totalLikes" value="0" />
                            <c:forEach var="recipe" items="${userRecipes}">
                                <c:set var="totalLikes" value="${totalLikes + recipe.likeCount}" />
                            </c:forEach>
                            <strong>${totalLikes}</strong> total likes received
                        </p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Your Recipes Section -->
        <div class="row mb-4">
            <div class="col-12">
                <h3 class="mb-3">
                    <i class="fas fa-book me-2"></i>Your Recipes
                    <span class="badge bg-primary">${userRecipes.size()}</span>
                </h3>
                
                <c:choose>
                    <c:when test="${empty userRecipes}">
                        <div class="card">
                            <div class="card-body text-center py-5">
                                <i class="fas fa-utensils fa-3x text-muted mb-3"></i>
                                <h5 class="text-muted">No recipes yet</h5>
                                <p class="text-muted">Start sharing your favorite recipes with the community!</p>
                                <a href="add-recipe" class="btn btn-primary">
                                    <i class="fas fa-plus me-2"></i>Add Your First Recipe
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row">
                            <c:forEach var="recipe" items="${userRecipes}">
                                <div class="col-md-6 col-lg-4 mb-4">
                                    <div class="card recipe-card">
                                        <c:if test="${not empty recipe.imagePath}">
                                            <img src="../${recipe.imagePath}" class="card-img-top" alt="${recipe.title}">
                                        </c:if>
                                        <c:if test="${empty recipe.imagePath}">
                                            <div class="card-img-top bg-light d-flex align-items-center justify-content-center" style="height: 200px;">
                                                <i class="fas fa-image fa-3x text-muted"></i>
                                            </div>
                                        </c:if>
                                        
                                        <div class="card-body">
                                            <h5 class="card-title">${recipe.title}</h5>
                                            <p class="card-text">
                                                ${recipe.description.length() > 100 ? recipe.description.substring(0, 100).concat('...') : recipe.description}
                                            </p>
                                            
                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                <span class="badge status-${recipe.status.toLowerCase()}">${recipe.status}</span>
                                                <small class="text-muted">
                                                    <fmt:formatDate value="${recipe.createdAt}" pattern="MMM dd, yyyy" />
                                                </small>
                                            </div>
                                            
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div>
                                                    <span class="text-muted me-3">
                                                        <i class="fas fa-heart me-1"></i>${recipe.likeCount}
                                                    </span>
                                                    <span class="text-muted">
                                                        <i class="fas fa-comment me-1"></i>${recipe.commentCount}
                                                    </span>
                                                </div>
                                                <a href="recipe-detail?id=${recipe.recipeId}" class="btn btn-sm btn-outline-primary">
                                                    View Details
                                                </a>
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
        
        <!-- Approved Recipes Section -->
        <div class="row">
            <div class="col-12">
                <h3 class="mb-3">
                    <i class="fas fa-star me-2"></i>Community Recipes
                    <span class="badge bg-success">${approvedRecipes.size()}</span>
                </h3>
                
                <c:choose>
                    <c:when test="${empty approvedRecipes}">
                        <div class="card">
                            <div class="card-body text-center py-5">
                                <i class="fas fa-search fa-3x text-muted mb-3"></i>
                                <h5 class="text-muted">No approved recipes yet</h5>
                                <p class="text-muted">Be the first to share a recipe!</p>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row">
                            <c:forEach var="recipe" items="${approvedRecipes}">
                                <div class="col-md-6 col-lg-4 mb-4">
                                    <div class="card recipe-card">
                                        <c:if test="${not empty recipe.imagePath}">
                                            <img src="../${recipe.imagePath}" class="card-img-top" alt="${recipe.title}">
                                        </c:if>
                                        <c:if test="${empty recipe.imagePath}">
                                            <div class="card-img-top bg-light d-flex align-items-center justify-content-center" style="height: 200px;">
                                                <i class="fas fa-image fa-3x text-muted"></i>
                                            </div>
                                        </c:if>
                                        
                                        <div class="card-body">
                                            <h5 class="card-title">${recipe.title}</h5>
                                            <p class="card-text">
                                                ${recipe.description.length() > 100 ? recipe.description.substring(0, 100).concat('...') : recipe.description}
                                            </p>
                                            
                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                <small class="text-muted">By ${recipe.userName}</small>
                                                <small class="text-muted">
                                                    <fmt:formatDate value="${recipe.createdAt}" pattern="MMM dd, yyyy" />
                                                </small>
                                            </div>
                                            
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div>
                                                    <form method="post" action="like-recipe" class="d-inline">
                                                        <input type="hidden" name="recipeId" value="${recipe.recipeId}">
                                                        <input type="hidden" name="action" value="${recipe.likedByCurrentUser ? 'unlike' : 'like'}">
                                                        <button type="submit" class="btn btn-link p-0 like-btn ${recipe.likedByCurrentUser ? 'liked' : ''}" 
                                                                data-recipe-id="${recipe.recipeId}">
                                                            <i class="fas fa-heart me-1"></i>${recipe.likeCount}
                                                        </button>
                                                    </form>
                                                    <span class="text-muted ms-3">
                                                        <i class="fas fa-comment me-1"></i>${recipe.commentCount}
                                                    </span>
                                                </div>
                                                <a href="recipe-detail?id=${recipe.recipeId}" class="btn btn-sm btn-outline-primary">
                                                    View Details
                                                </a>
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
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../js/main.js"></script>
</body>
</html>