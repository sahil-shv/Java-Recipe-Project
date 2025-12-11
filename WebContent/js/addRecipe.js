// Add Recipe Page Script
document.addEventListener('DOMContentLoaded', async function() {
    // Check if user is logged in
    try {
        const userResponse = await authAPI.getCurrentUser();
        if (!userResponse || !userResponse.user) {
            window.location.href = 'login.html?redirect=addRecipe';
            return;
        }
    } catch (error) {
        window.location.href = 'login.html?redirect=addRecipe';
        return;
    }
    
    const form = document.getElementById('recipe-form');
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const title = document.getElementById('title').value;
        const ingredients = document.getElementById('ingredients').value;
        const steps = document.getElementById('steps').value;
        const category = document.getElementById('category').value;
        const imageUrl = document.getElementById('image').value;
        const imageFile = document.getElementById('imageFile').files[0];
        
        const submitBtn = form.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Posting...';
        
        try {
            const recipeData = {
                title,
                ingredients,
                steps,
                category: category || null,
                image: imageUrl || null,
                imageFile: imageFile || null
            };
            
            const response = await recipeAPI.add(recipeData);
            
            if (response.success) {
                showAlert('Recipe added successfully! Redirecting...', 'success');
                setTimeout(() => {
                    window.location.href = 'recipes.html';
                }, 1500);
            }
        } catch (error) {
            showAlert(error.message || 'Failed to add recipe. Please try again.', 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalText;
        }
    });
});

function showAlert(message, type) {
    const alertContainer = document.getElementById('alert-container');
    const alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
    const icon = type === 'success' ? 'bi-check-circle' : 'bi-exclamation-triangle';
    
    alertContainer.innerHTML = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            <i class="bi ${icon}"></i> ${escapeHtml(message)}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

