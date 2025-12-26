<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI Recipe Generator - Recipe Sharing Platform</title>
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
                        <a class="nav-link active" href="ai-recipe">
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
                            <i class="fas fa-robot me-2"></i>AI Recipe Generator
                        </h3>
                    </div>
                    
                    <div class="card-body">
                        <div class="alert alert-info">
                            <i class="fas fa-lightbulb me-2"></i>
                            <strong>How it works:</strong> Enter the ingredients you have available, and our AI will suggest a delicious recipe you can make!
                        </div>
                        
                        <!-- Display messages -->
                        <c:if test="${error != null}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="ai-recipe" method="post" class="needs-validation" novalidate>
                            <div class="mb-3">
                                <label for="ingredients" class="form-label">Available Ingredients *</label>
                                <textarea class="form-control" id="ingredients" name="ingredients" rows="4" 
                                          placeholder="Enter ingredient names separated by commas (e.g., tomato, onion, chicken, rice)" 
                                          required>${ingredients}</textarea>
                                <div class="form-text">
                                    <strong>Important:</strong> Enter only ingredient names, separated by commas. 
                                    Example: tomato, onion, paneer, rice, garlic
                                </div>
                                <div class="invalid-feedback">
                                    Please enter at least one ingredient.
                                </div>
                            </div>
                            
                            <div class="d-flex justify-content-between">
                                <a href="dashboard" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
                                </a>
                                <button type="submit" class="btn btn-primary" id="generateBtn">
                                    <i class="fas fa-magic me-2"></i>Generate Recipe
                                </button>
                            </div>
                        </form>
                        
                        <!-- AI Recipe Result -->
                        <c:if test="${success != null && recipeRecommendation != null}">
                            <div class="ai-recipe-result">
                                <h4>
                                    <i class="fas fa-sparkles me-2"></i>AI Recipe Suggestion
                                </h4>
                                <div class="mb-3">
                                    <strong>Based on ingredients:</strong> ${ingredients}
                                </div>
                                <div class="recipe-content">
                                    ${recipeRecommendation}
                                </div>
                                <div class="mt-3 text-center">
                                    <small class="text-light">
                                        <i class="fas fa-info-circle me-1"></i>
                                        Recipe generated by AI. Always verify ingredients and cooking methods for safety.
                                    </small>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
                
                <!-- Tips Card -->
                <div class="card mt-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-tips me-2"></i>Tips for Better Results
                        </h5>
                    </div>
                    <div class="card-body">
                        <ul class="list-unstyled mb-0">
                            <li class="mb-2">
                                <i class="fas fa-check text-success me-2"></i>
                                Use common ingredient names (e.g., "chicken" instead of "chicken breast")
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-check text-success me-2"></i>
                                Include basic ingredients like salt, pepper, oil if you have them
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-check text-success me-2"></i>
                                List 3-8 ingredients for best results
                            </li>
                            <li class="mb-2">
                                <i class="fas fa-check text-success me-2"></i>
                                Try different combinations to discover new recipes
                            </li>
                            <li class="mb-0">
                                <i class="fas fa-check text-success me-2"></i>
                                Save interesting recipes by adding them to your collection
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="../js/main.js"></script>
    <script>
        // Show loading state when generating recipe
        document.querySelector('form').addEventListener('submit', function() {
            const btn = document.getElementById('generateBtn');
            btn.innerHTML = '<span class="loading"></span> Generating Recipe...';
            btn.disabled = true;
        });
        
        // Validate ingredients input
        document.getElementById('ingredients').addEventListener('input', function() {
            const value = this.value.trim();
            if (value.length < 3) {
                this.setCustomValidity('Please enter at least one ingredient');
            } else {
                this.setCustomValidity('');
            }
        });
    </script>
</body>
</html>