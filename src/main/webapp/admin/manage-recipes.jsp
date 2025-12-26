<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Recipes - Recipe Sharing Platform</title>
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
                        <a class="nav-link active" href="manage-recipes">
                            <i class="fas fa-book me-1"></i>Recipes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="manage-comments">
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
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h2 class="card-title mb-0">
                                    <i class="fas fa-book me-2"></i>Manage Recipes
                                </h2>
                                <p class="card-text text-muted">Review, approve, and manage all recipes</p>
                            </div>
                            <div>
                                <a href="manage-recipes" class="btn btn-outline-primary ${viewType != 'pending' ? 'active' : ''}">
                                    All Recipes
                                </a>
                                <a href="manage-recipes?view=pending" class="btn btn-outline-warning ${viewType == 'pending' ? 'active' : ''}">
                                    Pending Only
                                </a>
                            </div>
                        </div>
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
        
        <!-- Recipes Table -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <c:choose>
                                <c:when test="${viewType == 'pending'}">
                                    <i class="fas fa-clock me-2"></i>Pending Recipes (${recipes.size()})
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-list me-2"></i>All Recipes (${recipes.size()})
                                </c:otherwise>
                            </c:choose>
                        </h5>
                    </div>
                    
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty recipes}">
                                <div class="text-center py-5">
                                    <i class="fas fa-search fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No recipes found</h5>
                                    <c:choose>
                                        <c:when test="${viewType == 'pending'}">
                                            <p class="text-muted">All recipes have been reviewed!</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted">No recipes have been submitted yet.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Recipe</th>
                                                <th>Author</th>
                                                <th>Status</th>
                                                <th>Submitted</th>
                                                <th>Engagement</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="recipe" items="${recipes}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <c:choose>
                                                                <c:when test="${not empty recipe.imagePath}">
                                                                    <img src="../${recipe.imagePath}" alt="${recipe.title}" 
                                                                         class="rounded me-3" style="width: 50px; height: 50px; object-fit: cover;">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <div class="bg-light rounded me-3 d-flex align-items-center justify-content-center" 
                                                                         style="width: 50px; height: 50px;">
                                                                        <i class="fas fa-image text-muted"></i>
                                                                    </div>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <div>
                                                                <h6 class="mb-0">${recipe.title}</h6>
                                                                <small class="text-muted">
                                                                    ${recipe.description.length() > 50 ? recipe.description.substring(0, 50).concat('...') : recipe.description}
                                                                </small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <strong>${recipe.userName}</strong>
                                                    </td>
                                                    <td>
                                                        <span class="badge status-${recipe.status.toLowerCase()}">${recipe.status}</span>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${recipe.createdAt}" pattern="MMM dd, yyyy" /><br>
                                                        <small class="text-muted">
                                                            <fmt:formatDate value="${recipe.createdAt}" pattern="HH:mm" />
                                                        </small>
                                                    </td>
                                                    <td>
                                                        <small class="text-muted">
                                                            <i class="fas fa-heart me-1"></i>${recipe.likeCount}<br>
                                                            <i class="fas fa-comment me-1"></i>${recipe.commentCount}
                                                        </small>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group" role="group">
                                                            <c:if test="${recipe.status == 'PENDING'}">
                                                                <form method="post" action="manage-recipes" class="d-inline">
                                                                    <input type="hidden" name="recipeId" value="${recipe.recipeId}">
                                                                    <input type="hidden" name="action" value="approve">
                                                                    <button type="submit" class="btn btn-sm btn-success" 
                                                                            title="Approve Recipe">
                                                                        <i class="fas fa-check"></i>
                                                                    </button>
                                                                </form>
                                                                
                                                                <form method="post" action="manage-recipes" class="d-inline">
                                                                    <input type="hidden" name="recipeId" value="${recipe.recipeId}">
                                                                    <input type="hidden" name="action" value="reject">
                                                                    <button type="submit" class="btn btn-sm btn-warning" 
                                                                            title="Reject Recipe">
                                                                        <i class="fas fa-times"></i>
                                                                    </button>
                                                                </form>
                                                            </c:if>
                                                            
                                                            <a href="../user/recipe-detail?id=${recipe.recipeId}" 
                                                               class="btn btn-sm btn-outline-primary" title="View Recipe">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            
                                                            <form method="post" action="manage-recipes" class="d-inline">
                                                                <input type="hidden" name="recipeId" value="${recipe.recipeId}">
                                                                <input type="hidden" name="action" value="delete">
                                                                <button type="submit" class="btn btn-sm btn-danger btn-delete" 
                                                                        title="Delete Recipe" data-item-type="recipe" 
                                                                        data-item-name="${recipe.title}">
                                                                    <i class="fas fa-trash"></i>
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../js/main.js"></script>
</body>
</html>