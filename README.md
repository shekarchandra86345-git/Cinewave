# Cinewave - Online Ticket Booking System

Cinewave is a comprehensive Online Ticket Booking System designed to provide users with a seamless movie ticket booking experience. It features a robust backend powered by Spring Boot and a dynamic, responsive frontend built with modern web technologies.

## 🚀 Features

- **User Authentication**: Secure user registration and login.
- **Movie Browsing**: View a list of available movies with details like price and timing.
- **Seat Selection**: Interactive seat map for choosing preferred seats.
- **Booking Management**: Book tickets and view booking history.
- **Admin Panel**: Manage movies, theaters, and view all bookings.
- **Ticket Freezing**: Prevents multiple users from booking the same seat simultaneously.

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.2, Spring Data JPA.
- **Frontend**: Plain HTML5, CSS3 (Vanilla), JavaScript (ES6+).
- **Database**: H2 (Development), MySQL/PostgreSQL (Production ready).
- **Tools**: Maven, Docker (optional).

## 📂 Project Structure

- `/backend`: Contains the Spring Boot application source code.
- `/frontend`: Contains the web interface files (HTML, CSS, JS).
- `/database`: SQL scripts for database schema and initial data.
- `/data`: Default location for H2 database files.

## ⚙️ Setup Instructions

### Backend Setup

1.  Navigate to the `backend` directory:
    ```bash
    cd backend
    ```
2.  Build the project using Maven:
    ```bash
    ./mvnw clean install
    ```
3.  Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```
    The backend server will start at `http://localhost:8080`.

### Frontend Setup

The frontend files can be served directly by the backend or opened in a web browser.

- To access the user portal: `http://localhost:8080/index.html`
- To access the admin portal: `http://localhost:8080/admin_login.html`

Alternatively, you can open `frontend/index.html` directly in your browser, ensuring the backend server is running for API requests.

## 🛢️ Database Configuration

By default, the application uses an **H2 in-memory database** for development. To use a persistent database like MySQL or PostgreSQL, configure the following environment variables:

- `DATABASE_URL`: JDBC connection string (e.g., `jdbc:mysql://localhost:3306/ticketdb`)
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password
- `DB_DRIVER`: Driver class name (e.g., `com.mysql.cj.jdbc.Driver`)

## 🛡️ Admin Access

Default admin credentials (if seeded):
- **Username**: `admin`
- **Password**: `admin123`

---
*Created with ❤️ for Cinewave Users.*
