# 🍳 RecipeShare

![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=java)
![JSP](https://img.shields.io/badge/JSP-3.0-blue?style=flat-square&logo=java)
![Servlet](https://img.shields.io/badge/Servlet-5.0-blue?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple?style=flat-square&logo=bootstrap)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

> A modern, feature-rich recipe sharing platform built with Java Web technologies. Share your culinary creations, discover new recipes, and connect with food enthusiasts around the world.

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Folder Structure](#-folder-structure)
- [Database Schema](#-database-schema)
- [Installation Guide](#-installation-guide)
- [How Email Verification Works](#-how-email-verification-works)
- [How Rating System Works](#-how-rating-system-works)
- [API Endpoints](#-api-endpoints)
- [Screenshots](#-screenshots)
- [Future Enhancements](#-future-enhancements)
- [Contributors](#-contributors)
- [References](#-references)

---

## 🎯 Project Overview

**RecipeShare** is a comprehensive web application designed for recipe enthusiasts to share, discover, and rate recipes. Built using the classic MVC (Model-View-Controller) architecture pattern, the application follows clean code principles with separation of concerns across different layers:

- **Model Layer**: Java Beans (POJOs) representing domain entities
- **DAO Layer**: Data Access Objects handling all database operations
- **Service Layer**: Business logic and validation
- **Controller Layer**: Servlets for request routing and response handling
- **View Layer**: Modern JSP pages with Bootstrap 5 UI

The application emphasizes security, user experience, and maintainability through best practices like prepared statements, password hashing, email verification, and comprehensive error handling.

---

## ✨ Features

### 🔐 Authentication & Security
- **User Registration** with email verification
- **Secure Login** with SHA-256 password hashing + Salt
- **Session-based** authentication
- **Email Verification System** - Users must verify their email before login
- **Password Update** functionality with old password verification

### 📝 Recipe Management
- **Create Recipes** with title, ingredients, steps, category, and images
- **Browse Recipes** with pagination (10 recipes per page)
- **View Recipe Details** with formatted ingredients and instructions
- **Search Recipes** by title, ingredient, or category
- **My Recipes** filter to view only your recipes
- **Image Upload** with automatic compression (max 300KB)

### ⭐ Rating System
- **Rate Recipes** from 1-5 stars
- **Average Rating** displayed on recipe cards
- **User-specific Ratings** - One rating per user per recipe (updates if re-rated)
- **Visual Star Display** with hover effects
- **Rating Statistics** on user profile

### 💬 Comments
- **Post Comments** on recipes
- **View Comments** chronologically
- **Comment Threads** per recipe

### 👤 User Profile
- **Profile Page** with user statistics
- **Profile Image Upload** with compression
- **Update Password** functionality
- **User Statistics**: Total recipes created, average rating of user's recipes
- **Verification Status** indicator

### 🎨 Modern UI/UX
- **Responsive Design** with Bootstrap 5
- **Toast Notifications** for user feedback
- **Modal Dialogs** for rating recipes
- **Loading Animations**
- **Modern Card-based Layout**
- **Search Bar** with sticky positioning
- **Pagination Controls** with page highlighting

---

## 🛠 Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Java | 11+ |
| **Web Framework** | Jakarta Servlet | 5.0 |
| **View Technology** | JSP (JavaServer Pages) | 3.0 |
| **Database** | MySQL | 8.0+ |
| **JDBC Driver** | MySQL Connector/J | 8.0+ |
| **Frontend Framework** | Bootstrap | 5.3.0 |
| **Icons** | Bootstrap Icons | 1.11.0 |
| **Email** | Jakarta Mail API | - |
| **Image Processing** | Java ImageIO | - |
| **Server** | Apache Tomcat | 10.0+ |

---

## 📁 Folder Structure

```
RecipeShare/
├── src/
│   ├── model/                    # Java Beans (POJOs)
│   │   ├── User.java
│   │   ├── Recipe.java
│   │   ├── Comment.java
│   │   ├── Rating.java
│   │   └── EmailVerificationToken.java
│   │
│   ├── dao/                      # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── RecipeDAO.java
│   │   ├── CommentDAO.java
│   │   ├── RatingDAO.java
│   │   └── EmailVerificationTokenDAO.java
│   │
│   ├── service/                  # Business Logic Layer
│   │   ├── UserService.java
│   │   ├── RecipeService.java
│   │   └── CommentService.java
│   │
│   ├── servlet/                  # Controllers (Servlets)
│   │   ├── RegisterServlet.java
│   │   ├── LoginServlet.java
│   │   ├── LogoutServlet.java
│   │   ├── AddRecipeServlet.java
│   │   ├── RecipeListServlet.java
│   │   ├── ViewRecipeServlet.java
│   │   ├── AddCommentServlet.java
│   │   ├── RateRecipeServlet.java
│   │   ├── ProfileServlet.java
│   │   └── VerifyEmailServlet.java
│   │
│   ├── utils/                    # Utility Classes
│   │   ├── DBConnection.java
│   │   ├── PasswordUtil.java
│   │   ├── MailUtil.java
│   │   ├── ImageCompressor.java
│   │   ├── Pagination.java
│   │   └── Logger.java
│   │
│   └── filter/                   # Filters
│       └── ExceptionHandlerFilter.java
│
├── WebContent/
│   ├── css/
│   │   └── styles.css           # Custom styles
│   │
│   ├── js/                      # Frontend scripts for HTML pages
│   ├── uploads/                  # Uploaded files
│   │   ├── recipes/             # Recipe images
│   │   └── profile/             # Profile images
│   │
│   ├── index.html               # Home page
│   ├── login.html               # Login page
│   ├── register.html            # Registration page
│   ├── recipes.html             # Recipe listing page
│   ├── addRecipe.html           # Add recipe page
│   ├── viewRecipe.html          # Recipe details page
│   ├── profile.html             # User profile page
│   ├── verification.html        # Email verification result
│   └── error.html               # Error page
│
├── WEB-INF/
│   ├── web.xml                   # Servlet configuration
│   └── lib/                      # JAR dependencies
│       ├── mysql-connector-java-8.0.x.jar
│       ├── jakarta.mail.jar
│       └── (other runtime JARs)
│
├── database.sql                  # Database schema and seed data
└── README.md                     # This file
```

---

## 🗄 Database Schema

### Tables

#### `users`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | User ID |
| `name` | VARCHAR(100) | NOT NULL | User's full name |
| `email` | VARCHAR(150) | NOT NULL, UNIQUE | User's email address |
| `password` | CHAR(64) | NOT NULL | SHA-256 hashed password |
| `salt` | VARCHAR(64) | NOT NULL | Password salt |
| `is_verified` | BOOLEAN | DEFAULT FALSE | Email verification status |
| `profile_image` | VARCHAR(255) | NULL | Profile image path |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Account creation date |

#### `recipes`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | Recipe ID |
| `title` | VARCHAR(200) | NOT NULL | Recipe title |
| `ingredients` | TEXT | NOT NULL | Recipe ingredients |
| `steps` | TEXT | NOT NULL | Cooking instructions |
| `category` | VARCHAR(100) | NULL | Recipe category |
| `image` | VARCHAR(255) | NULL | Recipe image path/URL |
| `user_id` | INT | NOT NULL, FOREIGN KEY | Author's user ID |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Recipe creation date |

#### `comments`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | Comment ID |
| `recipe_id` | INT | NOT NULL, FOREIGN KEY | Recipe ID |
| `user_id` | INT | NOT NULL, FOREIGN KEY | Commenter's user ID |
| `comment_text` | TEXT | NOT NULL | Comment content |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Comment date |

#### `ratings`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `rating_id` | INT | PRIMARY KEY, AUTO_INCREMENT | Rating ID |
| `recipe_id` | INT | NOT NULL, FOREIGN KEY | Recipe ID |
| `user_id` | INT | NOT NULL, FOREIGN KEY | Rater's user ID |
| `rating` | INT | NOT NULL, CHECK (1-5) | Rating value (1-5 stars) |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Rating date |
| UNIQUE KEY | `(user_id, recipe_id)` | - | Prevents duplicate ratings |

#### `email_verification_tokens`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | Token ID |
| `user_id` | INT | NOT NULL, FOREIGN KEY | User ID |
| `token` | VARCHAR(255) | NOT NULL, UNIQUE | Verification token |
| `expiry` | TIMESTAMP | NOT NULL | Token expiration time |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Token creation date |

### Entity Relationship Diagram
```
users (1) ──< (N) recipes
users (1) ──< (N) comments
users (1) ──< (N) ratings
users (1) ──< (N) email_verification_tokens
recipes (1) ──< (N) comments
recipes (1) ──< (N) ratings
```

---

## 📦 Installation Guide

### Prerequisites

1. **JDK 11 or higher** - [Download](https://www.oracle.com/java/technologies/downloads/)
2. **Apache Tomcat 10+** - [Download](https://tomcat.apache.org/download-10.cgi)
3. **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)
4. **IDE** (Optional but recommended) - Eclipse, IntelliJ IDEA, or VS Code

### Step 1: Database Setup

1. **Create the database:**
   ```bash
   mysql -u root -p
   ```

2. **Run the SQL script:**
   ```sql
   SOURCE database.sql;
   ```
   Or manually execute the SQL commands from `database.sql`.

3. **Verify tables are created:**
   ```sql
   SHOW TABLES;
   USE recipeshare;
   DESCRIBE users;
   ```

### Step 2: Database Configuration

Update database credentials in `src/utils/DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/recipeshare?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "root";        // Change if needed
private static final String PASSWORD = "password";    // Change to your MySQL password
```

### Step 3: Email Configuration (Optional but Recommended)

For email verification to work, configure SMTP settings in `src/utils/MailUtil.java`:

**Option 1: Environment Variables (Recommended)**
```bash
# Set environment variables
export SMTP_USER="your-email@gmail.com"
export SMTP_PASSWORD="your-app-password"
```

**Option 2: Update directly in MailUtil.java**
```java
private static final String SMTP_USER = "your-email@gmail.com";
private static final String SMTP_PASSWORD = "your-app-password";
```

**For Gmail:**
1. Enable 2-factor authentication
2. Generate an App Password: [Google App Passwords](https://myaccount.google.com/apppasswords)
3. Use the app password (not your regular password)

### Step 4: Project Setup

1. **Clone or download the project:**
   ```bash
   git clone <repository-url>
   cd RecipeShare
   ```

2. **Dependencies** are managed by Maven. If you run the app outside Maven/Tomcat, make sure the following are available on the classpath:
   - `mysql-connector-java-8.0.x.jar` - [Download](https://dev.mysql.com/downloads/connector/j/)
   - `jakarta.mail.jar` - [Download](https://eclipse-ee4j.github.io/mail/)

3. **Configure Tomcat in your IDE:**
   - **Eclipse**: Right-click project → Properties → Project Facets → Enable Dynamic Web Module
   - **IntelliJ**: File → Project Structure → Facets → Add Web Facet

### Step 5: Create Upload Directories

Create the following directories manually or they will be created automatically:

```bash
mkdir -p WebContent/uploads/recipes
mkdir -p WebContent/uploads/profile
```

**Or on Windows:**
```cmd
mkdir WebContent\uploads\recipes
mkdir WebContent\uploads\profile
```

### Step 6: Deploy and Run

1. **Start MySQL server:**
   ```bash
   # Windows
   net start MySQL80
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

2. **Run the app locally with Jetty (serves the built WAR):**
   ```bash
   mvn -DskipTests org.eclipse.jetty:jetty-maven-plugin:12.0.13:run-war
   # optionally set PORT to override the default 8080
   # PORT=3000 mvn -DskipTests org.eclipse.jetty:jetty-maven-plugin:12.0.13:run-war
   ```

3. **Access the application (default Jetty port 8080):**
   ```
   http://localhost:8080/RecipeShare
   ```

### Step 7: Test the Application

1. **Register a new user** at `/register.html`
2. **Check your email** for the verification link
3. **Verify your email** by clicking the link
4. **Log in** at `/login.html`
5. **Create a recipe** at `/addRecipe.html`
6. **Browse recipes** at `/recipes.html`
7. **Rate and comment** on recipes

---

## 📧 How Email Verification Works

### Registration Flow

1. **User registers** with name, email, and password
2. **System generates** a unique verification token (Base64 URL-safe, 32 bytes)
3. **Token is stored** in `email_verification_tokens` table with 24-hour expiry
4. **Verification email** is sent to user's email with link:
   ```
   http://yourdomain.com/RecipeShare/verifyEmail?token=<unique-token>
   ```
5. **User clicks link** → `VerifyEmailServlet` processes the request
6. **System validates** token (exists, not expired)
7. **If valid**: User's `is_verified` flag is set to `TRUE`, token is deleted
8. **If invalid/expired**: Error message displayed, user must re-register

### Login Protection

- **Before login**: System checks `is_verified` status
- **If not verified**: Login is blocked with message: *"Please verify your email before logging in"*
- **If verified**: Normal login proceeds

### Token Security

- **Unique tokens**: Base64 URL-safe random 32-byte tokens
- **Time-limited**: 24-hour expiration
- **One-time use**: Token deleted after successful verification
- **Secure storage**: Token stored with user association and expiry

---

## ⭐ How Rating System Works

### Rating Mechanics

1. **User Rates Recipe**:
   - User clicks "Rate Recipe" button on recipe detail page
   - Modal dialog opens with interactive star rating (1-5 stars)
   - User hovers over stars to preview, clicks to select rating
   - Rating is submitted via POST to `/rateRecipe`

2. **Rating Storage**:
   - Rating stored in `ratings` table with:
     - `recipe_id`: Recipe being rated
     - `user_id`: User who rated
     - `rating`: Value from 1-5
   - **Unique constraint** on `(user_id, recipe_id)` prevents duplicates
   - **ON DUPLICATE KEY UPDATE**: If user re-rates, existing rating is updated

3. **Rating Display**:
   - **Recipe Cards**: Show average rating with star icons and total rating count
   - **Recipe Detail Page**: Display average rating prominently, show user's own rating if logged in
   - **User Profile**: Show average rating of all recipes created by user

4. **Rating Calculation**:
   ```sql
   SELECT AVG(rating) as avg_rating 
   FROM ratings 
   WHERE recipe_id = ?
   ```
   - Average calculated from all ratings for the recipe
   - Displayed rounded to 1 decimal place
   - Star icons filled based on rounded average

### User Experience Features

- **Visual Feedback**: Stars highlight on hover
- **Modal Dialog**: Non-intrusive rating interface
- **Toast Notifications**: Success/error messages
- **Prevent Duplicate Ratings**: System updates instead of creating duplicates
- **Rating History**: User can see and update their previous ratings

---

## 🔌 API Endpoints

### Frontend Pages

| Path | Description | Auth Required |
|------|-------------|---------------|
| `/index.html` | Landing page with highlights | No |
| `/login.html` | Login UI (calls `/api/login`) | No |
| `/register.html` | Registration UI (calls `/api/register`) | No |
| `/recipes.html` | Recipe browsing & search | No |
| `/recipes.html?myRecipes=true` | Current user's recipes | Yes |
| `/addRecipe.html` | Add recipe form | Yes |
| `/viewRecipe.html?id=N` | Recipe detail view | No |
| `/profile.html` | Profile dashboard | Yes |
| `/verification.html` | Email verification status | No |

### REST API

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/register` | Register new user | No |
| POST | `/api/login` | Authenticate and start session | No |
| POST | `/api/logout` | Clear the active session | Yes |
| GET | `/api/user/current` | Get current user session (if any) | No |
| GET | `/api/recipes` | List/search recipes (supports `search`, `page`, `myRecipes`) | No/Yes (myRecipes) |
| POST | `/api/recipes/add` | Create a recipe (supports URL or file image) | Yes |
| GET | `/api/recipes/{id}` | Get recipe details | No |
| POST | `/api/rate` | Rate or update rating for a recipe | Yes |
| POST | `/api/comments` | Add a comment to a recipe | Yes |
| GET | `/api/profile` | Fetch profile data and stats | Yes |

### Profile Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/profile` | User profile page | Yes |
| POST | `/profile` | Update profile (image/password) | Yes |

### Query Parameters

- `search`: Search term for recipes (title, ingredient, category)
- `page`: Page number for pagination (default: 1)
- `myRecipes`: Filter to show only current user's recipes (value: "true")
- `id`: Recipe ID for viewing/rating
- `recipeId`: Recipe ID (for rating/commenting)
- `rating`: Rating value (1-5) for rating submission
- `comment`: Comment text for adding comments
- `token`: Email verification token

---

## 📸 Screenshots

### Home Page
![Home Page](screenshots/home.png)
*Welcome page with feature highlights and call-to-action*

### Recipe Listing
![Recipe Listing](screenshots/recipes.png)
*Grid view of recipes with search bar and pagination*

### Recipe Details
![Recipe Details](screenshots/recipe-detail.png)
*Full recipe view with rating system and comments*

### User Profile
![Profile Page](screenshots/profile.png)
*User profile with statistics and settings*

### Rating Modal
![Rating Modal](screenshots/rating-modal.png)
*Interactive star rating interface*

*Note: Add actual screenshots to a `screenshots/` folder in your project*

---

## 🚀 Future Enhancements

### Planned Features

- [ ] **Recipe Categories Filtering** - Filter recipes by category badges
- [ ] **Recipe Favorites/Bookmarks** - Save favorite recipes
- [ ] **Recipe Collections** - Create custom recipe collections
- [ ] **Advanced Search** - Filter by rating, category, date range
- [ ] **Recipe Sharing** - Share recipes via social media
- [ ] **Recipe Export** - Export recipes as PDF
- [ ] **Nutritional Information** - Add calorie and nutrition data
- [ ] **Recipe Images Gallery** - Multiple images per recipe
- [ ] **Recipe Reviews** - Separate reviews from comments
- [ ] **Admin Panel** - Manage users and content
- [ ] **Email Notifications** - Notify users of comments/ratings
- [ ] **Recipe Recommendations** - Suggest recipes based on user preferences
- [ ] **Mobile App** - Native mobile application
- [ ] **API Documentation** - RESTful API with Swagger
- [ ] **Unit Testing** - Comprehensive test coverage
- [ ] **Docker Support** - Containerized deployment

### Technical Improvements

- [ ] **Caching Layer** - Redis for session and recipe caching
- [ ] **Search Engine** - Elasticsearch for advanced search
- [ ] **CDN Integration** - CloudFront for image delivery
- [ ] **Database Optimization** - Indexing and query optimization
- [ ] **Security Hardening** - CSRF protection, rate limiting
- [ ] **Logging System** - Centralized logging with Log4j2
- [ ] **Monitoring** - Application performance monitoring

---

## 👥 Contributors

- **Development Team** - Initial development and architecture
- **Community Contributors** - Bug fixes and feature enhancements

*Would you like to contribute? Please read our contributing guidelines and submit a pull request!*

---

## 📚 References

1. Oracle Corporation. (2023). *Java Platform, Standard Edition Documentation*. [https://docs.oracle.com/javase/](https://docs.oracle.com/javase/)

2. Apache Software Foundation. (2023). *Apache Tomcat Documentation*. [https://tomcat.apache.org/](https://tomcat.apache.org/)

3. Oracle Corporation. (2023). *MySQL 8.0 Reference Manual*. [https://dev.mysql.com/doc/refman/8.0/en/](https://dev.mysql.com/doc/refman/8.0/en/)

4. Bootstrap Team. (2023). *Bootstrap 5 Documentation*. [https://getbootstrap.com/docs/5.3/](https://getbootstrap.com/docs/5.3/)

5. Eclipse Foundation. (2023). *Jakarta Servlet Specification*. [https://jakarta.ee/specifications/servlet/](https://jakarta.ee/specifications/servlet/)

6. National Institute of Standards and Technology. (2023). *Secure Hash Standard (SHA-256)*. FIPS PUB 180-4. [https://csrc.nist.gov/publications/detail/fips/180/4/final](https://csrc.nist.gov/publications/detail/fips/180/4/final)

7. Oracle Corporation. (2023). *Java Image I/O API Guide*. [https://docs.oracle.com/javase/tutorial/2d/images/index.html](https://docs.oracle.com/javase/tutorial/2d/images/index.html)

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 🙏 Acknowledgments

- **Bootstrap** for the beautiful UI framework
- **Bootstrap Icons** for the icon library
- **MySQL** for the robust database system
- **Apache Tomcat** for the reliable servlet container
- **Java Community** for excellent documentation and resources

---

**Made with ❤️ by the RecipeShare Team**

*Happy Cooking! 🍳👨‍🍳*
