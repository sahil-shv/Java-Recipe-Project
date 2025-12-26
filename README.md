# 🍽️ Recipe Sharing Platform

A place where recipes feel human again.

This project is a full-stack **Recipe Sharing Platform** designed to bring people together through food. Users can share their favorite recipes, explore dishes from others, and even get AI-powered recipe ideas based on ingredients they already have.

Built with a classic Java web stack, the platform focuses on **clarity, security, and real-world structure**—without unnecessary complexity.

---

## ✨ Why This Project Exists

Cooking is personal.  
This platform reflects that by combining:

- Community-driven recipe sharing  
- Thoughtful moderation  
- Clean UI/UX  
- Practical backend architecture  
- A touch of AI to inspire creativity  

It’s designed to feel **approachable for users** and **structured for developers**.

---

## 🌟 Core Features

### 👤 User Experience
- Create an account and securely log in
- Share your own recipes with images and instructions
- Explore recipes shared by the community
- Like and comment on approved recipes
- Get AI-generated recipe ideas using available ingredients

### 🧠 Smart Assistance
- AI-powered recipe generation
- Ingredient-based suggestions
- Clear, readable instructions and tips

### 🛡️ Moderation & Trust
- Recipes are reviewed before becoming public
- Clean separation between user and admin capabilities
- Safe interactions through validation and access control

---

## 🧩 Admin Capabilities

Admins have access to a dedicated interface that allows them to:

- Review and moderate submitted recipes
- Manage users and community activity
- Monitor engagement through a simple dashboard
- Keep the platform clean and welcoming

---

## 🛠️ Tech Stack

This project intentionally uses a **traditional, proven Java web stack** to demonstrate solid backend fundamentals.

### Backend
- Java (Servlets & JSP)
- JDBC
- Maven

### Database
- MySQL

### Frontend
- HTML5  
- CSS3  
- JavaScript  
- Bootstrap 5  

### Security
- BCrypt password hashing
- Session-based authentication
- Role-based access control
- Input validation & sanitization

### AI Integration
- OpenAI API for recipe generation

---

## 🗂️ Project Structure


The project follows a clean, layered architecture commonly used in real-world Java web applications.
Each package has a clear responsibility, making the codebase easy to understand, maintain, and extend.

```
RecipeSharingPlatform/
│
├── controller/
│   └── Handles all incoming HTTP requests and responses  
│      (Servlets act as the bridge between UI and business logic)
│
├── dao/
│   └── Manages all database interactions using JDBC  
│      (CRUD operations, queries, and data mapping)
│
├── model/
│   └── Plain Old Java Objects (POJOs)  
│      (Represents users, recipes, comments, likes, etc.)
│
├── util/
│   └── Shared utility classes  
│      (Database connection, password hashing, validation helpers)
│
├── filter/
│   └── Request filters for authentication and authorization  
│      (Protects user and admin routes)
│
├── admin/
│   └── Admin-facing JSP pages  
│      (Dashboard, moderation, management screens)
│
├── user/
│   └── User-facing JSP pages  
│      (Recipe browsing, profile, interactions)
│
├── assets/
│   ├── css/        → Stylesheets  
│   ├── js/         → Client-side scripts  
│   └── images/     → Static images
│
└── resources/
    └── Application configuration and supporting files
```

This structure encourages:

* Clear separation of concerns
* Easier debugging and testing
* Scalable feature additions
* Familiarity for developers experienced with Java EE–style applications


---

## 🔒 Security by Design

Security is treated as a **baseline**, not an afterthought:

- Passwords are hashed using BCrypt
- SQL injection is prevented via prepared statements
- Sessions are managed securely
- Role-based filters protect restricted areas
- Inputs are validated and sanitized
- Uploaded images are checked and handled safely

---

## 🎨 UI & Experience

- Clean, responsive layout using Bootstrap
- Mobile-friendly design
- Clear navigation and feedback
- Simple interactions (likes, comments, uploads)
- Focus on usability over visual noise

---

## 🤖 AI Recipe Generator

The AI feature helps users answer the question:

> “What can I cook with what I already have?”

**From a user’s perspective:**
- Enter available ingredients
- Receive a complete recipe suggestion
- Get clear steps and cooking tips

The integration is intentionally lightweight and user-focused.

---

## 📚 What This Project Demonstrates

- Real-world Java web application structure
- Secure authentication and authorization
- Clean separation of concerns
- Practical database interaction with JDBC
- Thoughtful feature design
- Responsible use of AI APIs
- A balance between backend rigor and frontend usability

---

## 📄 License

This project is intended for **educational, learning, and evaluation purposes**.

---

## 🙌 Final Note

This platform isn’t just about recipes—it’s about showing how a well-structured, secure, and user-friendly Java web application can be built with intention and clarity.

If you enjoy building things that feel *thoughtful*, this project was made with you in mind.