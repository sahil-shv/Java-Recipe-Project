<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Recipe - Recipe Sharing Platform</title>
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
                        <a class="nav-link active" href="add-recipe">
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
                            <i class="fas fa-user me-1"></i>${sessionScope.user.name}
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
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title mb-0">
                            <i class="fas fa-plus-circle me-2"></i>Add New Recipe
                        </h3>
                    </div>
                    
                    <div class="card-body">
                        <!-- Display messages -->
                        <c:if test="${error != null}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="add-recipe" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                            <div class="mb-3">
                                <label for="title" class="form-label">Recipe Title *</label>
                                <input type="text" class="form-control" id="title" name="title" 
                                       placeholder="Enter recipe title" required maxlength="200">
                                <div class="invalid-feedback">
                                    Please provide a recipe title (3-200 characters).
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="ingredients" class="form-label">Ingredients *</label>
                                <textarea class="form-control" id="ingredients" name="ingredients" rows="4" 
                                          placeholder="List all ingredients with quantities..." required 
                                          data-max-length="1000"></textarea>
                                <div class="form-text">
                                    List all ingredients with their quantities (e.g., 2 cups flour, 1 tsp salt)
                                </div>
                                <div class="invalid-feedback">
                                    Please provide ingredients (minimum 10 characters).
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="description" class="form-label">Cooking Instructions *</label>
                                <textarea class="form-control" id="description" name="description" rows="6" 
                                          placeholder="Provide detailed step-by-step cooking instructions..." required 
                                          data-max-length="2000"></textarea>
                                <div class="form-text">
                                    Provide detailed step-by-step cooking instructions
                                </div>
                                <div class="invalid-feedback">
                                    Please provide cooking instructions (minimum 20 characters).
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="image" class="form-label">Recipe Image (Optional)</label>
                                <input type="file" class="form-control" id="image" name="image" 
                                       accept="image/jpeg,image/jpg,image/png,image/gif">
                                <div class="form-text">
                                    Upload an image of your recipe (JPG, PNG, GIF - Max 10MB)
                                </div>
                                
                                <!-- Image Preview -->
                                <div class="mt-3">
                                    <img id="imagePreview" src="#" alt="Recipe Preview" 
                                         class="img-thumbnail" style="display: none; max-width: 300px; max-height: 200px;">
                                </div>
                            </div>
                            
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Note:</strong> Your recipe will be reviewed by our admin team before being published to the community.
                            </div>
                            
                            <div class="d-flex justify-content-between">
                                <a href="dashboard" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Submit Recipe
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../js/main.js"></script>
    <script>
        // Additional validation for recipe form
        document.getElementById('title').addEventListener('input', function() {
            if (this.value.length < 3) {
                this.setCustomValidity('Title must be at least 3 characters long');
            } else {
                this.setCustomValidity('');
            }
        });
        
        document.getElementById('ingredients').addEventListener('input', function() {
            if (this.value.length < 10) {
                this.setCustomValidity('Ingredients must be at least 10 characters long');
            } else {
                this.setCustomValidity('');
            }
        });
        
        document.getElementById('description').addEventListener('input', function() {
            if (this.value.length < 20) {
                this.setCustomValidity('Instructions must be at least 20 characters long');
            } else {
                this.setCustomValidity('');
            }
        });
    </script>
</body>
</html>