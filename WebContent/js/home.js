// Home Page Script
async function loadHomeContent() {
    try {
        const userResponse = await authAPI.getCurrentUser();
        const user = userResponse?.user;
        
        const greetingContainer = document.getElementById('user-greeting');
        if (!greetingContainer) return;
        
        if (user) {
            greetingContainer.innerHTML = `
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-body p-5">
                        <h3 class="card-title mb-3">Hello, ${escapeHtml(user.name)}!</h3>
                        <p class="card-text text-muted">Ready to share your culinary creations with the world?</p>
                        <a class="btn btn-primary btn-lg" href="addRecipe.html">
                            <i class="bi bi-plus-circle"></i> Share a Recipe
                        </a>
                    </div>
                </div>
            `;
        } else {
            greetingContainer.innerHTML = `
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-body p-5">
                        <h3 class="card-title mb-3">Join Our Community</h3>
                        <p class="card-text text-muted mb-4">Create a free account to start sharing your recipes with the community.</p>
                        <a class="btn btn-primary btn-lg me-3" href="register.html">
                            <i class="bi bi-person-plus"></i> Join Now
                        </a>
                        <a class="btn btn-outline-primary btn-lg" href="recipes.html">
                            <i class="bi bi-book"></i> Browse Recipes
                        </a>
                    </div>
                </div>
            `;
        }
    } catch (error) {
        console.error('Error loading home content:', error);
    }
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Load content when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', loadHomeContent);
} else {
    loadHomeContent();
}

