// Profile Page Script
document.addEventListener('DOMContentLoaded', async function() {
    // Check if user is logged in
    try {
        const userResponse = await authAPI.getCurrentUser();
        if (!userResponse || !userResponse.user) {
            window.location.href = 'login.html';
            return;
        }
    } catch (error) {
        window.location.href = 'login.html';
        return;
    }
    
    await loadProfile();
});

async function loadProfile() {
    const container = document.getElementById('profile-container');
    
    try {
        const response = await profileAPI.get();
        const user = response.user;
        const recipes = response.recipes || [];
        const recipeCount = response.recipeCount || 0;
        const avgRating = response.avgRating || 0;
        
        if (!user) {
            showError('User not found');
            return;
        }
        
        const profileImageHTML = user.profileImage 
            ? `<img src="${escapeHtml(user.profileImage)}" alt="Profile" class="rounded-circle" style="width: 150px; height: 150px; object-fit: cover; border: 4px solid #dee2e6;">`
            : `<div class="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center" style="width: 150px; height: 150px; font-size: 3rem;">${user.name ? user.name.charAt(0).toUpperCase() : 'U'}</div>`;
        
        const avgRatingHTML = avgRating > 0 
            ? `${avgRating.toFixed(1)} <i class="bi bi-star-fill"></i>`
            : 'N/A';
        
        container.innerHTML = `
            <div id="alert-container"></div>
            
            <div class="row mb-4">
                <div class="col-12">
                    <div class="card shadow-sm border-0">
                        <div class="card-body p-4">
                            <div class="row align-items-center">
                                <div class="col-md-3 text-center mb-3 mb-md-0">
                                    ${profileImageHTML}
                                </div>
                                <div class="col-md-9">
                                    <h2 class="mb-2">${escapeHtml(user.name || 'User')}</h2>
                                    <p class="text-muted mb-2">
                                        <i class="bi bi-envelope"></i> ${escapeHtml(user.email || '')}
                                    </p>
                                    <p class="mb-2">
                                        ${user.verified 
                                            ? '<span class="badge bg-success"><i class="bi bi-check-circle"></i> Verified</span>'
                                            : '<span class="badge bg-warning"><i class="bi bi-exclamation-triangle"></i> Not Verified</span>'}
                                    </p>
                                    <div class="row mt-3">
                                        <div class="col-md-4">
                                            <div class="text-center p-3 bg-light rounded">
                                                <h4 class="mb-0 text-primary">${recipeCount}</h4>
                                                <small class="text-muted">Recipes</small>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="text-center p-3 bg-light rounded">
                                                <h4 class="mb-0 text-warning">${avgRatingHTML}</h4>
                                                <small class="text-muted">Avg Rating</small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-4">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><i class="bi bi-image"></i> Update Profile Image</h5>
                        </div>
                        <div class="card-body">
                            <form id="profile-image-form" enctype="multipart/form-data">
                                <div class="mb-3">
                                    <label for="profileImage" class="form-label">Choose Image</label>
                                    <input type="file" class="form-control" id="profileImage" name="profileImage" accept="image/*" required>
                                    <small class="form-text text-muted">Max 2MB. Image will be compressed automatically.</small>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-upload"></i> Upload Image
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 mb-4">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-header bg-warning text-dark">
                            <h5 class="mb-0"><i class="bi bi-lock"></i> Change Password</h5>
                        </div>
                        <div class="card-body">
                            <form id="password-form">
                                <div class="mb-3">
                                    <label for="oldPassword" class="form-label">Current Password</label>
                                    <input type="password" class="form-control" id="oldPassword" name="oldPassword" required>
                                </div>
                                <div class="mb-3">
                                    <label for="newPassword" class="form-label">New Password</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword" required minlength="6">
                                    <small class="form-text text-muted">Minimum 6 characters</small>
                                </div>
                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Confirm New Password</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required minlength="6">
                                </div>
                                <button type="submit" class="btn btn-warning">
                                    <i class="bi bi-key"></i> Update Password
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-12">
                    <div class="card shadow-sm border-0">
                        <div class="card-header bg-info text-white">
                            <h5 class="mb-0"><i class="bi bi-book"></i> My Recipes (${recipeCount})</h5>
                        </div>
                        <div class="card-body">
                            ${recipes.length === 0 ? `
                                <p class="text-muted text-center py-4">You haven't shared any recipes yet. <a href="addRecipe.html">Create your first recipe!</a></p>
                            ` : `
                                <div class="row">
                                    ${recipes.map(recipe => `
                                        <div class="col-md-6 col-lg-4 mb-3">
                                            <div class="card h-100 border-0 shadow-sm">
                                                ${recipe.image ? `<img src="${escapeHtml(recipe.image)}" class="card-img-top" alt="${escapeHtml(recipe.title)}" style="height: 150px; object-fit: cover;">` : ''}
                                                <div class="card-body">
                                                    <h6 class="card-title">${escapeHtml(recipe.title)}</h6>
                                                    ${recipe.category ? `<span class="badge bg-secondary">${escapeHtml(recipe.category)}</span>` : ''}
                                                    <div class="mt-2">
                                                        ${recipe.averageRating > 0 ? `
                                                            ${Array.from({length: 5}, (_, i) => 
                                                                `<i class="bi bi-star${i < recipe.averageRating ? '-fill text-warning' : ''}"></i>`
                                                            ).join('')}
                                                            <small class="text-muted ms-1">${recipe.averageRating.toFixed(1)}</small>
                                                        ` : ''}
                                                    </div>
                                                    <a href="viewRecipe.html?id=${recipe.id}" class="btn btn-sm btn-primary mt-2">
                                                        <i class="bi bi-eye"></i> View
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    `).join('')}
                                </div>
                            `}
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        // Setup form handlers
        setupForms();
    } catch (error) {
        showError('Error loading profile: ' + error.message);
    }
}

function setupForms() {
    // Profile image form
    document.getElementById('profile-image-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        showAlert('Profile image upload not yet implemented via API. Please use the JSP version temporarily.', 'info');
        // TODO: Implement profile image upload API endpoint
    });
    
    // Password form
    document.getElementById('password-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        showAlert('Password update not yet implemented via API. Please use the JSP version temporarily.', 'info');
        // TODO: Implement password update API endpoint
    });
}

function showError(message) {
    const container = document.getElementById('profile-container');
    container.innerHTML = `
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-triangle"></i> ${escapeHtml(message)}
        </div>
    `;
}

function showAlert(message, type) {
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) return;
    
    const alertClass = type === 'success' ? 'alert-success' : (type === 'info' ? 'alert-info' : 'alert-danger');
    const icon = type === 'success' ? 'bi-check-circle' : (type === 'info' ? 'bi-info-circle' : 'bi-exclamation-triangle');
    
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

