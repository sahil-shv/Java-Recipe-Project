<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Server Error - Recipe Sharing Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="../css/style.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center min-vh-100 align-items-center">
            <div class="col-md-6 text-center">
                <div class="card">
                    <div class="card-body p-5">
                        <i class="fas fa-exclamation-triangle fa-5x text-warning mb-4"></i>
                        <h1 class="display-4 text-danger">500</h1>
                        <h3 class="mb-3">Server Error</h3>
                        <p class="text-muted mb-4">
                            Something went wrong on our end. Please try again later.
                        </p>
                        <div class="d-flex justify-content-center gap-3">
                            <a href="../login.jsp" class="btn btn-primary">
                                <i class="fas fa-home me-2"></i>Go Home
                            </a>
                            <button onclick="location.reload()" class="btn btn-outline-secondary">
                                <i class="fas fa-refresh me-2"></i>Try Again
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>