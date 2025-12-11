// API Utility Functions
const API_BASE = window.location.origin + window.location.pathname.replace(/\/[^/]*$/, '');

// Helper function to make API calls
async function apiCall(endpoint, options = {}) {
    const url = API_BASE + endpoint;
    const defaultOptions = {
        credentials: 'include', // Include cookies for session
        headers: {
            'Content-Type': 'application/json',
        }
    };

    const config = {
        ...defaultOptions,
        ...options,
        headers: {
            ...defaultOptions.headers,
            ...options.headers,
        }
    };

    try {
        const response = await fetch(url, config);
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Request failed');
        }
        
        return data;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// Authentication APIs
const authAPI = {
    login: async (email, password) => {
        const formData = new URLSearchParams();
        formData.append('email', email);
        formData.append('password', password);
        
        return apiCall('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString()
        });
    },
    
    register: async (name, email, password) => {
        const formData = new URLSearchParams();
        formData.append('name', name);
        formData.append('email', email);
        formData.append('password', password);
        
        return apiCall('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString()
        });
    },
    
    logout: async () => {
        return apiCall('/api/logout', {
            method: 'POST'
        });
    },
    
    getCurrentUser: async () => {
        return apiCall('/api/user/current');
    }
};

// Recipe APIs
const recipeAPI = {
    getAll: async (page = 1, search = null, myRecipes = false) => {
        const params = new URLSearchParams();
        params.append('page', page);
        if (search) params.append('search', search);
        if (myRecipes) params.append('myRecipes', 'true');
        
        return apiCall('/api/recipes?' + params.toString());
    },
    
    getById: async (id) => {
        return apiCall('/api/recipes/' + id);
    },
    
    add: async (recipeData) => {
        const formData = new FormData();
        formData.append('title', recipeData.title);
        formData.append('ingredients', recipeData.ingredients);
        formData.append('steps', recipeData.steps);
        formData.append('category', recipeData.category || '');
        if (recipeData.image) formData.append('image', recipeData.image);
        if (recipeData.imageFile) formData.append('imageFile', recipeData.imageFile);
        
        return apiCall('/api/recipes/add', {
            method: 'POST',
            headers: {},
            body: formData
        });
    },
    
    rate: async (recipeId, rating) => {
        const formData = new URLSearchParams();
        formData.append('recipeId', recipeId);
        formData.append('rating', rating);
        
        return apiCall('/api/rate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString()
        });
    }
};

// Comment APIs
const commentAPI = {
    add: async (recipeId, comment) => {
        const formData = new URLSearchParams();
        formData.append('recipeId', recipeId);
        formData.append('comment', comment);
        
        return apiCall('/api/comments', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString()
        });
    }
};

// Profile APIs
const profileAPI = {
    get: async () => {
        return apiCall('/api/profile');
    }
};

// Utility function to show toast notifications
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    const toastBody = document.getElementById('toast-body');
    const toastTitle = document.getElementById('toast-title');
    
    if (!toast) {
        console.error('Toast element not found');
        return;
    }
    
    toastBody.textContent = message;
    toast.className = 'toast';
    
    if (type === 'success') {
        toastTitle.textContent = 'Success';
        toast.classList.add('text-bg-success');
    } else if (type === 'error') {
        toastTitle.textContent = 'Error';
        toast.classList.add('text-bg-danger');
    } else {
        toastTitle.textContent = 'Info';
        toast.classList.add('text-bg-primary');
    }
    
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
}

// Export for use in other scripts
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { authAPI, recipeAPI, commentAPI, profileAPI, showToast };
}

