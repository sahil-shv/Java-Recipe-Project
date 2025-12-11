// Footer Component
function loadFooter() {
    const container = document.getElementById('footer-container');
    if (!container) return;
    
    container.innerHTML = `
        <footer class="bg-dark text-light py-4 mt-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-6">
                        <h5>🍳 RecipeShare</h5>
                        <p class="text-muted mb-0">Share your culinary creations with the world.</p>
                    </div>
                    <div class="col-md-6 text-md-end">
                        <p class="text-muted mb-0">
                            <small>&copy; 2024 RecipeShare. All rights reserved.</small>
                        </p>
                    </div>
                </div>
            </div>
        </footer>
    `;
}

// Load footer when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', loadFooter);
} else {
    loadFooter();
}

