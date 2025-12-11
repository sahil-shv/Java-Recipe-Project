// Navbar Component
async function loadNavbar() {
    const container = document.getElementById('navbar-container');
    if (!container) return;
    
    try {
        const user = await authAPI.getCurrentUser();
        
        const navbarHTML = `
            <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
                <div class="container-fluid">
                    <a class="navbar-brand fw-bold" href="index.html">🍳 RecipeShare</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <ul class="navbar-nav me-auto">
                            <li class="nav-item">
                                <a class="nav-link" href="index.html">
                                    <i class="bi bi-house"></i> Home
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="recipes.html">
                                    <i class="bi bi-search"></i> Search
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="recipes.html">
                                    <i class="bi bi-book"></i> All Recipes
                                </a>
                            </li>
                            ${user && user.user ? `
                                <li class="nav-item">
                                    <a class="nav-link" href="recipes.html?myRecipes=true">
                                        <i class="bi bi-collection"></i> My Recipes
                                    </a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="profile.html">
                                        <i class="bi bi-person-circle"></i> Profile
                                    </a>
                                </li>
                            ` : ''}
                        </ul>
                        <ul class="navbar-nav">
                            ${user && user.user ? `
                                <li class="nav-item">
                                    <a class="nav-link btn btn-outline-light btn-sm me-2" href="addRecipe.html">Add Recipe</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="#" onclick="handleLogout(); return false;">Logout</a>
                                </li>
                            ` : `
                                <li class="nav-item">
                                    <a class="nav-link" href="login.html">Login</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link btn btn-outline-light btn-sm" href="register.html">Register</a>
                                </li>
                            `}
                        </ul>
                    </div>
                </div>
            </nav>
            
            <!-- Toast Container -->
            <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 11;">
                <div id="toast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="toast-header">
                        <strong class="me-auto" id="toast-title">Notification</strong>
                        <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
                    </div>
                    <div class="toast-body" id="toast-body"></div>
                </div>
            </div>
        `;
        
        container.innerHTML = navbarHTML;
    } catch (error) {
        console.error('Error loading navbar:', error);
        // Fallback navbar
        container.innerHTML = `
            <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
                <div class="container-fluid">
                    <a class="navbar-brand fw-bold" href="index.html">🍳 RecipeShare</a>
                    <div class="navbar-nav ms-auto">
                        <a class="nav-link" href="login.html">Login</a>
                        <a class="nav-link btn btn-outline-light btn-sm" href="register.html">Register</a>
                    </div>
                </div>
            </nav>
        `;
    }
}

async function handleLogout() {
    try {
        await authAPI.logout();
        showToast('Logged out successfully', 'success');
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1000);
    } catch (error) {
        showToast('Error logging out', 'error');
    }
}

// Load navbar when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', loadNavbar);
} else {
    loadNavbar();
}

