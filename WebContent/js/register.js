// Register Page Script
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('register-form');
    const alertContainer = document.getElementById('alert-container');
    
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        
        const submitBtn = form.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Registering...';
        
        try {
            const response = await authAPI.register(name, email, password);
            
            if (response.success) {
                showAlert(response.message || 'Registration successful! Please check your email for verification.', 'success');
                setTimeout(() => {
                    window.location.href = 'login.html?registered=true';
                }, 2000);
            }
        } catch (error) {
            showAlert(error.message || 'Registration failed. Please try again.', 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalText;
        }
    });
    
    function showAlert(message, type) {
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
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
});

