<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - Recipe Sharing Platform</title>
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
                        <a class="nav-link active" href="manage-users">
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
        <!-- Page Header -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title">
                            <i class="fas fa-users me-2"></i>Manage Users
                        </h2>
                        <p class="card-text text-muted">View and manage all registered users</p>
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
        
        <!-- Users Table -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-list me-2"></i>All Users (${users.size()})
                        </h5>
                    </div>
                    
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty users}">
                                <div class="text-center py-5">
                                    <i class="fas fa-users fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No users found</h5>
                                    <p class="text-muted">No users have registered yet.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>User ID</th>
                                                <th>Name</th>
                                                <th>Email</th>
                                                <th>Role</th>
                                                <th>Joined</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="user" items="${users}">
                                                <tr>
                                                    <td>
                                                        <strong>#${user.userId}</strong>
                                                    </td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="bg-primary rounded-circle d-flex align-items-center justify-content-center me-3" 
                                                                 style="width: 40px; height: 40px;">
                                                                <i class="fas fa-user text-white"></i>
                                                            </div>
                                                            <div>
                                                                <h6 class="mb-0">${user.name}</h6>
                                                                <c:if test="${user.role == 'ADMIN'}">
                                                                    <small class="text-primary">
                                                                        <i class="fas fa-shield-alt me-1"></i>Administrator
                                                                    </small>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <span class="text-muted">${user.email}</span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${user.role == 'ADMIN'}">
                                                                <span class="badge bg-primary">
                                                                    <i class="fas fa-shield-alt me-1"></i>ADMIN
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-success">
                                                                    <i class="fas fa-user me-1"></i>USER
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${user.createdAt}" pattern="MMM dd, yyyy" /><br>
                                                        <small class="text-muted">
                                                            <fmt:formatDate value="${user.createdAt}" pattern="HH:mm" />
                                                        </small>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${user.userId == sessionScope.user.userId}">
                                                                <span class="badge bg-info">Current User</span>
                                                            </c:when>
                                                            <c:when test="${user.role == 'ADMIN'}">
                                                                <span class="badge bg-warning">Protected</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <form method="post" action="manage-users" class="d-inline">
                                                                    <input type="hidden" name="userId" value="${user.userId}">
                                                                    <input type="hidden" name="action" value="delete">
                                                                    <button type="submit" class="btn btn-sm btn-danger btn-delete" 
                                                                            title="Delete User" data-item-type="user" 
                                                                            data-item-name="${user.name}">
                                                                        <i class="fas fa-trash me-1"></i>Delete
                                                                    </button>
                                                                </form>
                                                            </c:otherwise>
                                                        </c:choose>
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
        
        <!-- User Statistics -->
        <div class="row mt-4">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h6 class="card-title mb-0">
                            <i class="fas fa-chart-pie me-2"></i>User Statistics
                        </h6>
                    </div>
                    <div class="card-body">
                        <c:set var="adminCount" value="0" />
                        <c:set var="userCount" value="0" />
                        <c:forEach var="user" items="${users}">
                            <c:choose>
                                <c:when test="${user.role == 'ADMIN'}">
                                    <c:set var="adminCount" value="${adminCount + 1}" />
                                </c:when>
                                <c:otherwise>
                                    <c:set var="userCount" value="${userCount + 1}" />
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        
                        <div class="row text-center">
                            <div class="col-6">
                                <h4 class="text-primary">${adminCount}</h4>
                                <small class="text-muted">Administrators</small>
                            </div>
                            <div class="col-6">
                                <h4 class="text-success">${userCount}</h4>
                                <small class="text-muted">Regular Users</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h6 class="card-title mb-0">
                            <i class="fas fa-info-circle me-2"></i>Management Info
                        </h6>
                    </div>
                    <div class="card-body">
                        <ul class="list-unstyled mb-0">
                            <li class="mb-2">
                                <i class="fas fa-shield-alt text-primary me-2"></i>
                                Admin accounts cannot be deleted
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-user text-info me-2"></i>
                                Your own account is protected
                            </li>
                            <li class="mb-0">
                                <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                                Deleting users will remove their recipes and comments
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../js/main.js"></script>
</body>
</html>