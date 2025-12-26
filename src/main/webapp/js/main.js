// Recipe Sharing Platform - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
    
    // Form validation
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
    
    // Like button functionality
    var likeButtons = document.querySelectorAll('.like-btn');
    likeButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            var recipeId = this.dataset.recipeId;
            var isLiked = this.classList.contains('liked');
            var action = isLiked ? 'unlike' : 'like';
            
            // Show loading state
            var originalContent = this.innerHTML;
            this.innerHTML = '<span class="loading"></span>';
            this.disabled = true;
            
            // Create form and submit
            var form = document.createElement('form');
            form.method = 'POST';
            form.action = 'like-recipe';
            form.style.display = 'none';
            
            var recipeIdInput = document.createElement('input');
            recipeIdInput.type = 'hidden';
            recipeIdInput.name = 'recipeId';
            recipeIdInput.value = recipeId;
            
            var actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = action;
            
            form.appendChild(recipeIdInput);
            form.appendChild(actionInput);
            document.body.appendChild(form);
            
            form.submit();
        });
    });
    
    // Confirm delete actions
    var deleteButtons = document.querySelectorAll('.btn-delete');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            var itemType = this.dataset.itemType || 'item';
            var itemName = this.dataset.itemName || '';
            
            var message = 'Are you sure you want to delete this ' + itemType + '?';
            if (itemName) {
                message = 'Are you sure you want to delete "' + itemName + '"?';
            }
            
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });
    
    // Image preview for file uploads
    var imageInputs = document.querySelectorAll('input[type="file"][accept*="image"]');
    imageInputs.forEach(function(input) {
        input.addEventListener('change', function(e) {
            var file = e.target.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var preview = document.getElementById('imagePreview');
                    if (preview) {
                        preview.src = e.target.result;
                        preview.style.display = 'block';
                    }
                };
                reader.readAsDataURL(file);
            }
        });
    });
    
    // Character counter for textareas
    var textareas = document.querySelectorAll('textarea[data-max-length]');
    textareas.forEach(function(textarea) {
        var maxLength = parseInt(textarea.dataset.maxLength);
        var counter = document.createElement('div');
        counter.className = 'form-text text-end';
        counter.id = textarea.id + '_counter';
        textarea.parentNode.appendChild(counter);
        
        function updateCounter() {
            var remaining = maxLength - textarea.value.length;
            counter.textContent = remaining + ' characters remaining';
            counter.className = 'form-text text-end ' + (remaining < 50 ? 'text-warning' : '');
        }
        
        textarea.addEventListener('input', updateCounter);
        updateCounter();
    });
    
    // Search functionality
    var searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            var searchTerm = this.value.toLowerCase();
            var items = document.querySelectorAll('.searchable-item');
            
            items.forEach(function(item) {
                var text = item.textContent.toLowerCase();
                if (text.includes(searchTerm)) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    }
    
    // Smooth scrolling for anchor links
    var anchorLinks = document.querySelectorAll('a[href^="#"]');
    anchorLinks.forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            var target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // Add fade-in animation to cards
    var cards = document.querySelectorAll('.card');
    cards.forEach(function(card, index) {
        setTimeout(function() {
            card.classList.add('fade-in');
        }, index * 100);
    });
});

// Utility functions
function showLoading(element) {
    element.innerHTML = '<span class="loading"></span> Loading...';
    element.disabled = true;
}

function hideLoading(element, originalText) {
    element.innerHTML = originalText;
    element.disabled = false;
}

function showToast(message, type = 'info') {
    var toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toastContainer';
        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }
    
    var toast = document.createElement('div');
    toast.className = 'toast';
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="toast-header">
            <i class="fas fa-info-circle me-2 text-${type}"></i>
            <strong class="me-auto">Notification</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
        </div>
        <div class="toast-body">
            ${message}
        </div>
    `;
    
    toastContainer.appendChild(toast);
    var bsToast = new bootstrap.Toast(toast);
    bsToast.show();
    
    // Remove toast element after it's hidden
    toast.addEventListener('hidden.bs.toast', function() {
        toast.remove();
    });
}

// Format date function
function formatDate(dateString) {
    var date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}

// Truncate text function
function truncateText(text, maxLength) {
    if (text.length <= maxLength) {
        return text;
    }
    return text.substring(0, maxLength) + '...';
}