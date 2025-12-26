<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Recipe Sharing Platform</title>
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
                        <a class="nav-link active" href="dashboard">
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
        <!-- Welcome Section -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title">
                            <i class="fas fa-tachometer-alt me-2"></i>Admin Dashboard
                        </h2>
                        <p class="card-text text-muted">Welcome to the Recipe Sharing Platform administration panel.</p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Display messages -->
        <c:if test="${error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-6 col-lg-3 mb-3">
                <div class="stat-card users">
                    <i class="fas fa-users fa-2x mb-2"></i>
                    <h3>${totalUsers}</h3>
                    <p>Total Users</p>
                </div>
            </div>
            
            <div class="col-md-6 col-lg-3 mb-3">
                <div class="stat-card recipes">
                    <i class="fas fa-book fa-2x mb-2"></i>
                    <h3>${totalRecipes}</h3>
                    <p>Total Recipes</p>
                </div>
            </div>
            
            <div class="col-md-6 col-lg-3 mb-3">
                <div class="stat-card pending">
                    <i class="fas fa-clock fa-2x mb-2"></i>
                    <h3>${pendingRecipes}</h3>
                    <p>Pending Approvals</p>
                </div>
            </div>
            
            <div class="col-md-6 col-lg-3 mb-3">
                <div class="stat-card likes">
                    <i class="fas fa-heart fa-2x mb-2"></i>
                    <h3>${totalLikes}</h3>
                    <p>Total Likes</p>
                </div>
            </div>
        </div>
        
        <div class="row mb-4">
            <div class="col-md-6 col-lg-3 mb-3">
                <div class="stat-card comments">
                    <i class="fas fa-comments fa-2x mb-2"></i>
                    <h3>${totalComments}</h3>
                    <p>Total Comments</p>
                </div>
            </div>
        </div>
        
        <!-- Quick Actions -->
        <div class="row mb-4">
            <div class="col-12">
                <h3 class="mb-3">
                    <i class="fas fa-bolt me-2"></i>Quick Actions
                </h3>
            </div>
            
            <div class="col-md-6 col-lg-3 mb-3">
                <a href="manage-recipes?view=pending" class="text-decoration-none">
                    <div class="card text-center h-100 border-warning">
                        <div class="card-body">
                            <i class="fas fa-clock fa-3x text-warning mb-3"></i>
                            <h5 class="card-title">Review Pending</h5>
                            <p class="card-text">
                                <span class="badge bg-warning">${pendingRecipes}</span> recipes waiting for approval
                            </p>
                        </div>
                    </div>
                </a>
            </div>
            
            <div class="col-md-6 col-lg-3 mb-3">
                <a href="manage-users" class="text-decoration-none">
                    <div class="card text-center h-100 border-primary">
                        <div class="card-body">
                            <i class="fas fa-users fa-3x text-primary mb-3"></i>
                            <h5 class="card-title">Manage Users</h5>
                            <p class="card-text">View and manage user accounts</p>
                        </div>
                    </div>
                </a>
            </div>
            
            <div class="col-md-6 col-lg-3 mb-3">
                <a href="manage-recipes" class="text-decoration-none">
                    <div class="card text-center h-100 border-success">
                        <div class="card-body">
                            <i class="fas fa-book fa-3x text-success mb-3"></i>
                            <h5 class="card-title">All Recipes</h5>
                            <p class="card-text">View and manage all recipes</p>
                        </div>
                    </div>
                </a>
            </div>
            
            <div class="col-md-6 col-lg-3 mb-3">
                <a href="manage-comments" class="text-decoration-none">
                    <div class="card text-center h-100 border-info">
                        <div class="card-body">
                            <i class="fas fa-comments fa-3x text-info mb-3"></i>
                            <h5 class="card-title">Moderate Comments</h5>
                            <p class="card-text">Review and manage comments</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>
        
        <!-- System Information -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-info-circle me-2"></i>System Information
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6>Platform Statistics</h6>
                                <ul class="list-unstyled">
                                    <li><strong>Total Users:</strong> ${totalUsers}</li>
                                    <li><strong>Total Recipes:</strong> ${totalRecipes}</li>
                                    <li><strong>Pending Reviews:</strong> ${pendingRecipes}</li>
                                    <li><strong>Community Engagement:</strong> ${totalLikes + totalComments} interactions</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6>Admin Actions</h6>
                                <ul class="list-unstyled">
                                    <li>
                                        <c:choose>
                                            <c:when test="${pendingRecipes > 0}">
                                                <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                                                <strong>${pendingRecipes}</strong> recipes need your attention
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-check-circle text-success me-2"></i>
                                                All recipes are reviewed
                                            </c:otherwise>
                                        </c:choose>
                                    </li>
                                    <li>
                                        <i class="fas fa-users text-info me-2"></i>
                                        <strong>${totalUsers}</strong> registered users
                                    </li>
                                    <li>
                                        <i class="fas fa-comments text-primary me-2"></i>
                                        <strong>${totalComments}</strong> community comments
                                    </li>
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