// View Recipe Page Script
let currentRecipeId = null;
let currentUser = null;

document.addEventListener('DOMContentLoaded', async function() {
    const urlParams = new URLSearchParams(window.location.search);
    currentRecipeId = urlParams.get('id');
    
    if (!currentRecipeId) {
        showError('Recipe ID is required');
        return;
    }
    
    try {
        currentUser = await authAPI.getCurrentUser();
        currentUser = currentUser?.user || null;
    } catch (e) {
        // User not logged in
    }
    
    await loadRecipe();
    setupRatingModal();
});

async function loadRecipe() {
    const container = document.getElementById('recipe-container');
    
    try {
        const response = await recipeAPI.getById(currentRecipeId);
        const recipe = response.recipe;
        const comments = response.comments || [];
        const userRating = response.userRating;
        
        if (!recipe) {
            showError('Recipe not found');
            return;
        }
        
        const avgRating = recipe.averageRating || 0;
        const totalRatings = recipe.totalRatings || 0;
        
        let starsHTML = '';
        if (avgRating > 0) {
            for (let i = 1; i <= 5; i++) {
                const starClass = i <= avgRating ? 'bi-star-fill text-warning' : 'bi-star-fill text-muted';
                starsHTML += `<i class="bi ${starClass}" style="font-size: 1.5rem;"></i>`;
            }
            starsHTML += `<span class="ms-2 fs-5 fw-bold">${avgRating.toFixed(1)}</span>`;
        } else {
            starsHTML = '<span class="text-muted">No ratings yet. Be the first to rate!</span>';
        }
        
        const createdAt = recipe.createdAt ? new Date(recipe.createdAt).toLocaleDateString() : '';
        
        container.innerHTML = `
            <div class="mb-4">
                <a href="recipes.html" class="btn btn-outline-secondary mb-3">
                    <i class="bi bi-arrow-left"></i> Back to Recipes
                </a>
            </div>
            
            <div id="alert-container"></div>
            
            <div class="card shadow-lg border-0 mb-4">
                ${recipe.image ? `<img src="${escapeHtml(recipe.image)}" class="card-img-top" alt="${escapeHtml(recipe.title)}" style="max-height: 500px; object-fit: cover;">` : ''}
                <div class="card-body p-5">
                    <h1 class="card-title display-4 mb-3">${escapeHtml(recipe.title)}</h1>
                    
                    <div class="d-flex flex-wrap align-items-center gap-3 mb-4">
                        <div>
                            <i class="bi bi-person-circle text-primary"></i>
                            <strong>${escapeHtml(recipe.author ? recipe.author.name : 'Unknown')}</strong>
                        </div>
                        ${recipe.category ? `<span class="badge bg-info fs-6"><i class="bi bi-tag"></i> ${escapeHtml(recipe.category)}</span>` : ''}
                        ${createdAt ? `<span class="text-muted"><i class="bi bi-calendar"></i> ${createdAt}</span>` : ''}
                    </div>
                    
                    <div class="mb-4 p-3 bg-light rounded">
                        <div class="d-flex align-items-center gap-3">
                            <div>
                                ${avgRating > 0 ? `
                                    <div class="d-flex align-items-center">
                                        ${starsHTML}
                                    </div>
                                    <small class="text-muted">${totalRatings} rating${totalRatings !== 1 ? 's' : ''}</small>
                                ` : starsHTML}
                            </div>
                            ${currentUser ? `
                                <div class="ms-auto">
                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#ratingModal">
                                        <i class="bi bi-star"></i> Rate Recipe
                                    </button>
                                </div>
                            ` : ''}
                        </div>
                    </div>
                    
                    <div class="mb-4">
                        <h3 class="mb-3"><i class="bi bi-list-ul text-primary"></i> Ingredients</h3>
                        <div class="bg-light p-4 rounded">
                            <pre class="mb-0" style="white-space: pre-wrap; font-family: inherit; font-size: 1rem;">${escapeHtml(recipe.ingredients || '')}</pre>
                        </div>
                    </div>
                    
                    <div class="mb-4">
                        <h3 class="mb-3"><i class="bi bi-list-check text-primary"></i> Instructions</h3>
                        <div class="bg-light p-4 rounded">
                            <pre class="mb-0" style="white-space: pre-wrap; font-family: inherit; font-size: 1rem;">${escapeHtml(recipe.steps || '')}</pre>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="card shadow-sm border-0">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0"><i class="bi bi-chat-dots"></i> Comments (${comments.length})</h4>
                </div>
                <div class="card-body">
                    ${comments.length === 0 ? `
                        <p class="text-muted text-center py-4">No comments yet. Be the first to comment!</p>
                    ` : `
                        <div class="list-group list-group-flush">
                            ${comments.map(comment => `
                                <div class="list-group-item border-0 px-0 py-3">
                                    <div class="d-flex">
                                        <div class="flex-grow-1">
                                            <p class="mb-2">${escapeHtml(comment.commentText || '')}</p>
                                            <small class="text-muted">
                                                <i class="bi bi-person"></i> ${escapeHtml(comment.commenter ? comment.commenter.name : 'Unknown')}
                                                ${comment.createdAt ? `<i class="bi bi-clock ms-2"></i> ${new Date(comment.createdAt).toLocaleString()}` : ''}
                                            </small>
                                        </div>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    `}
                    
                    ${currentUser ? `
                        <div class="mt-4 pt-4 border-top">
                            <h5 class="mb-3">Add a Comment</h5>
                            <form id="comment-form">
                                <div class="mb-3">
                                    <textarea name="comment" id="comment-text" class="form-control" rows="4" placeholder="Share your thoughts about this recipe..." required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-send"></i> Post Comment
                                </button>
                            </form>
                        </div>
                    ` : `
                        <div class="mt-4 pt-4 border-top text-center">
                            <p class="text-muted">
                                <a href="login.html" class="btn btn-outline-primary">
                                    <i class="bi bi-box-arrow-in-right"></i> Log in to comment
                                </a>
                            </p>
                        </div>
                    `}
                </div>
            </div>
        `;
        
        // Setup comment form if user is logged in
        if (currentUser) {
            document.getElementById('comment-form').addEventListener('submit', async function(e) {
                e.preventDefault();
                const commentText = document.getElementById('comment-text').value;
                await submitComment(commentText);
            });
        }
        
    } catch (error) {
        showError('Error loading recipe: ' + error.message);
    }
}

async function submitComment(commentText) {
    try {
        await commentAPI.add(currentRecipeId, commentText);
        showAlert('Comment added successfully!', 'success');
        document.getElementById('comment-text').value = '';
        // Reload recipe to show new comment
        await loadRecipe();
    } catch (error) {
        showAlert('Failed to add comment: ' + error.message, 'error');
    }
}

function setupRatingModal() {
    const ratingForm = document.getElementById('rating-form');
    const stars = document.querySelectorAll('.rating-stars .bi-star');
    const ratingValue = document.getElementById('ratingValue');
    const ratingText = document.getElementById('ratingText');
    
    const ratingMessages = {
        1: 'Poor',
        2: 'Fair',
        3: 'Good',
        4: 'Very Good',
        5: 'Excellent'
    };
    
    let currentRating = 0;
    
    stars.forEach(star => {
        star.addEventListener('mouseenter', function() {
            const rating = parseInt(this.getAttribute('data-rating'));
            highlightStars(rating);
            ratingText.textContent = ratingMessages[rating];
        });
        
        star.addEventListener('click', function() {
            currentRating = parseInt(this.getAttribute('data-rating'));
            ratingValue.value = currentRating;
            highlightStars(currentRating);
            ratingText.textContent = ratingMessages[currentRating];
        });
    });
    
    document.querySelector('.rating-stars').addEventListener('mouseleave', function() {
        highlightStars(currentRating);
        ratingText.textContent = currentRating > 0 ? ratingMessages[currentRating] : '';
    });
    
    function highlightStars(rating) {
        stars.forEach((star, index) => {
            if (index < rating) {
                star.classList.remove('bi-star');
                star.classList.add('bi-star-fill');
                star.style.color = '#ffc107';
            } else {
                star.classList.remove('bi-star-fill');
                star.classList.add('bi-star');
                star.style.color = '#ccc';
            }
        });
    }
    
    ratingForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const rating = parseInt(ratingValue.value);
        
        if (!rating || rating < 1 || rating > 5) {
            showAlert('Please select a rating', 'error');
            return;
        }
        
        try {
            await recipeAPI.rate(currentRecipeId, rating);
            showAlert('Rating saved successfully!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('ratingModal')).hide();
            await loadRecipe();
        } catch (error) {
            showAlert('Failed to save rating: ' + error.message, 'error');
        }
    });
    
    // Set recipe ID when modal opens
    document.getElementById('ratingModal').addEventListener('show.bs.modal', function() {
        document.getElementById('ratingRecipeId').value = currentRecipeId;
    });
}

function showError(message) {
    const container = document.getElementById('recipe-container');
    container.innerHTML = `
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-triangle"></i> ${escapeHtml(message)}
        </div>
        <a href="recipes.html" class="btn btn-primary">
            <i class="bi bi-arrow-left"></i> Back to Recipes
        </a>
    `;
}

function showAlert(message, type) {
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) return;
    
    const alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
    const icon = type === 'success' ? 'bi-check-circle' : 'bi-exclamation-triangle';
    
    alertContainer.innerHTML = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            <i class="bi ${icon}"></i> ${escapeHtml(message)}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    setTimeout(() => {
        const alert = alertContainer.querySelector('.alert');
        if (alert) {
            bootstrap.Alert.getOrCreateInstance(alert).close();
        }
    }, 5000);
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

