// Recipes Page Script
let currentPage = 1;
let currentSearch = '';
let myRecipes = false;

document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    currentPage = parseInt(urlParams.get('page')) || 1;
    currentSearch = urlParams.get('search') || '';
    myRecipes = urlParams.get('myRecipes') === 'true';
    
    if (currentSearch) {
        document.getElementById('search-input').value = currentSearch;
        document.getElementById('clear-search').classList.remove('d-none');
    }
    
    if (myRecipes) {
        document.getElementById('page-title').textContent = 'My Recipes';
    }
    
    loadRecipes();
    
    document.getElementById('search-form').addEventListener('submit', function(e) {
        e.preventDefault();
        currentSearch = document.getElementById('search-input').value;
        currentPage = 1;
        loadRecipes();
    });
    
    document.getElementById('clear-search').addEventListener('click', function() {
        document.getElementById('search-input').value = '';
        currentSearch = '';
        currentPage = 1;
        document.getElementById('clear-search').classList.add('d-none');
        loadRecipes();
    });
});

async function loadRecipes() {
    const container = document.getElementById('recipes-container');
    container.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    `;
    
    try {
        const response = await recipeAPI.getAll(currentPage, currentSearch || null, myRecipes);
        const recipes = response.recipes || [];
        const pagination = response.pagination;
        
        if (recipes.length === 0) {
            container.innerHTML = `
                <div class="alert alert-info">
                    <i class="bi bi-info-circle"></i> No recipes found. 
                    <span id="add-recipe-link"></span>
                </div>
            `;
            // Check if user is logged in to show "add recipe" link
            try {
                const userResponse = await authAPI.getCurrentUser();
                if (userResponse && userResponse.user) {
                    document.getElementById('add-recipe-link').innerHTML = 
                        'Be the first to <a href="addRecipe.html">share a recipe</a>!';
                }
            } catch (e) {
                // User not logged in, ignore
            }
        } else {
            container.innerHTML = '<div class="row"></div>';
            const row = container.querySelector('.row');
            
            recipes.forEach(recipe => {
                const card = createRecipeCard(recipe);
                row.appendChild(card);
            });
        }
        
        renderPagination(pagination);
    } catch (error) {
        container.innerHTML = `
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle"></i> Error loading recipes: ${escapeHtml(error.message)}
            </div>
        `;
    }
}

function createRecipeCard(recipe) {
    const col = document.createElement('div');
    col.className = 'col-md-6 col-lg-4 mb-4';
    
    const avgRating = recipe.averageRating || 0;
    const totalRatings = recipe.totalRatings || 0;
    const ingredients = recipe.ingredients || '';
    const preview = ingredients.length > 100 ? ingredients.substring(0, 100) + '...' : ingredients;
    
    let starsHTML = '';
    if (avgRating > 0) {
        for (let i = 1; i <= 5; i++) {
            const starClass = i <= avgRating ? 'bi-star-fill text-warning' : 'bi-star';
            starsHTML += `<i class="bi ${starClass}"></i>`;
        }
        starsHTML += `<span class="small text-muted ms-1">(${avgRating.toFixed(1)} / 5, ${totalRatings} ratings)</span>`;
    } else {
        starsHTML = '<span class="text-muted small">No ratings yet</span>';
    }
    
    col.innerHTML = `
        <div class="card h-100 shadow-sm border-0">
            ${recipe.image ? `<img src="${escapeHtml(recipe.image)}" class="card-img-top" alt="${escapeHtml(recipe.title)}" style="height: 200px; object-fit: cover;">` : ''}
            <div class="card-body d-flex flex-column">
                <h5 class="card-title">${escapeHtml(recipe.title)}</h5>
                <p class="text-muted small mb-2">
                    <i class="bi bi-person"></i> ${escapeHtml(recipe.author ? recipe.author.name : 'Unknown')}
                    ${recipe.category ? ` | <i class="bi bi-tag"></i> ${escapeHtml(recipe.category)}` : ''}
                </p>
                <div class="mb-2">${starsHTML}</div>
                <p class="card-text flex-grow-1 text-muted small">${escapeHtml(preview)}</p>
                <a href="viewRecipe.html?id=${recipe.id}" class="btn btn-primary mt-auto">
                    <i class="bi bi-eye"></i> View Recipe
                </a>
            </div>
        </div>
    `;
    
    return col;
}

function renderPagination(pagination) {
    const container = document.getElementById('pagination-container');
    if (!pagination || pagination.totalPages <= 1) {
        container.innerHTML = '';
        return;
    }
    
    let paginationHTML = `
        <nav aria-label="Recipe pagination">
            <ul class="pagination justify-content-center">
                <li class="page-item ${pagination.currentPage === 1 ? 'disabled' : ''}">
                    <a class="page-link" href="#" onclick="goToPage(${pagination.currentPage - 1}); return false;">Previous</a>
                </li>
    `;
    
    for (let i = 1; i <= pagination.totalPages; i++) {
        if (i === 1 || i === pagination.totalPages || (i >= pagination.currentPage - 2 && i <= pagination.currentPage + 2)) {
            paginationHTML += `
                <li class="page-item ${i === pagination.currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="goToPage(${i}); return false;">${i}</a>
                </li>
            `;
        } else if (i === pagination.currentPage - 3 || i === pagination.currentPage + 3) {
            paginationHTML += '<li class="page-item disabled"><span class="page-link">...</span></li>';
        }
    }
    
    paginationHTML += `
                <li class="page-item ${pagination.currentPage === pagination.totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="#" onclick="goToPage(${pagination.currentPage + 1}); return false;">Next</a>
                </li>
            </ul>
        </nav>
        <p class="text-center text-muted mt-2">
            Showing page ${pagination.currentPage} of ${pagination.totalPages} (${pagination.totalItems} total recipes)
        </p>
    `;
    
    container.innerHTML = paginationHTML;
}

function goToPage(page) {
    currentPage = page;
    const params = new URLSearchParams();
    if (currentSearch) params.append('search', currentSearch);
    if (myRecipes) params.append('myRecipes', 'true');
    params.append('page', page);
    window.location.href = 'recipes.html?' + params.toString();
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

