# Simple Authentication System

A full-stack authentication system built with **Java Spring Boot** (backend) and **HTML + DaisyUI + TailwindCSS** (frontend), using **Neon PostgreSQL** as the database.

## Features

- User Registration with form validation
- User Login with BCrypt password hashing
- Forgot Password with token-based reset
- Dashboard showing logged-in user details
- Database Details page showing all registered users
- Clean responsive UI with DaisyUI
- Dark mode by default

## Project Structure

```
authentication-app/
├── backend/                          # Spring Boot backend
│   ├── pom.xml                       # Maven dependencies
│   └── src/main/
│       ├── java/com/example/auth/
│       │   ├── AuthApplication.java        # Main entry point
│       │   ├── config/SecurityConfig.java  # Security & CORS config
│       │   ├── controller/AuthController.java  # REST API endpoints
│       │   ├── dto/                        # Request/Response DTOs
│       │   │   ├── ApiResponse.java
│       │   │   ├── ForgotPasswordRequest.java
│       │   │   ├── LoginRequest.java
│       │   │   ├── RegisterRequest.java
│       │   │   └── ResetPasswordRequest.java
│       │   ├── entity/User.java           # JPA Entity
│       │   ├── repository/UserRepository.java  # Data access
│       │   └── service/AuthService.java   # Business logic
│       └── resources/
│           ├── application.properties     # Database config
│           ├── schema.sql                 # Sample SQL schema
│           └── static/                    # Frontend (served by Spring Boot)
│               ├── index.html
│               ├── register.html
│               ├── forgot-password.html
│               ├── dashboard.html
│               ├── database-details.html
│               └── assets/app.js
├── frontend/                         # Frontend source files
│   ├── index.html
│   ├── register.html
│   ├── forgot-password.html
│   ├── dashboard.html
│   ├── database-details.html
│   └── assets/app.js
└── README.md
```

## Git Setup

### Initialize the repository
```bash
cd authentication-app
git init
```

### Create .gitignore
Create a `.gitignore` file in the project root:

```gitignore
### Java ###
target/
*.class
*.jar
*.war
*.log

### Maven ###
!**/src/main/**/target/
!**/src/test/**/target/

### IDE ###
.idea/
*.iml
.vscode/
.project
.classpath
.settings/
*.swp
*.swo

### OS ###
.DS_Store
Thumbs.db

### Env / Secrets ###
*.env
application-local.properties
```

### Commit and push
```bash
git add .
git commit -m "Initial commit - Simple Auth System"
git branch -M main

# Create a repo on GitHub first, then:
git remote add origin https://github.com/your-username/your-repo-name.git
git push -u origin main
```

## Prerequisites

- **Java 17+** installed
- **Maven** installed (or use the Maven wrapper)
- **Neon PostgreSQL** database (free tier at [neon.tech](https://neon.tech))
- A modern web browser

## Setup Instructions

### 1. Database Setup (Neon PostgreSQL)

1. Go to [neon.tech](https://neon.tech) and create a free account
2. Create a new project (it will auto-create a database called `neondb`)
3. Copy your connection string from the dashboard
4. It will look like: `postgresql://username:password@ep-xxxx.us-east-2.aws.neon.tech/neondb?sslmode=require`

### 2. Backend Setup

1. Open `backend/src/main/resources/application.properties`
2. Replace the database URL, username, and password with your Neon credentials:

```properties
spring.datasource.url=jdbc:postgresql://ep-your-project-123456.us-east-2.aws.neon.tech/neondb?sslmode=require
spring.datasource.username=your_actual_username
spring.datasource.password=your_actual_password
```

3. Build and run the backend:

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on **http://localhost:8080** and tables will be auto-created by JPA.

### 3. Frontend Setup

The frontend is already baked into the backend. Just start the backend and open **http://localhost:8080** in your browser. Spring Boot serves all frontend files automatically from the same port — no CORS issues.

If you want to modify the frontend, edit the files in `frontend/`, then copy them to `backend/src/main/resources/static/` and restart the backend.

## API Endpoints

### Base URL: `http://localhost:8080/api/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register` | Register a new user |
| POST | `/login` | Login with username and password |
| POST | `/forgot-password` | Request a password reset token |
| POST | `/reset-password` | Reset password using token |
| GET | `/users` | Get all registered users |

### Example Requests

#### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "email": "test@example.com", "password": "mypassword"}'
```

**Success Response:**
```json
{
  "success": true,
  "message": "Registration successful! You can now log in."
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Username already taken"
}
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "mypassword"}'
```

**Success Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com"
  }
}
```

#### Forgot Password
```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com"}'
```

**Success Response:**
```json
{
  "success": true,
  "message": "Reset token generated. Check your email (or see response for demo).",
  "data": {
    "resetToken": "a1b2c3d4-...-uuid"
  }
}
```

#### Reset Password
```bash
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{"token": "your-reset-token", "newPassword": "newpassword123"}'
```

#### Get All Users
```bash
curl http://localhost:8080/api/auth/users
```

## Sample Test Users

After starting the backend, you can register users through the UI or use these sample credentials:

| Username | Email | Password |
|----------|-------|----------|
| `john_doe` | `john@example.com` | `password123` |
| `jane_smith` | `jane@example.com` | `password123` |

*Note: The sample users are inserted via `schema.sql`. If the table is auto-created by JPA, the SQL script may need to be run manually.*

## Deployment on AWS VPS (EC2)

### 1. Launch EC2 Instance

- Use **Amazon Linux 2023** or **Ubuntu 22.04** (free tier eligible)
- Security group — add inbound rules:
  - **SSH** (port 22) — your IP
  - **HTTP** (port 80) — 0.0.0.0/0
  - **Custom TCP** (port 8080) — 0.0.0.0/0 (or use Nginx reverse proxy on port 80)
- Choose or create a key pair for SSH access

### 2. Connect & Install Dependencies

```bash
# SSH into your EC2 instance
ssh -i your-key.pem ec2-user@your-instance-public-ip

# Install Java 17 (Amazon Linux 2023)
sudo dnf install -y java-17-amazon-corretto-devel

# or (Ubuntu)
# sudo apt update && sudo apt install -y openjdk-17-jdk maven git

# Install Maven
sudo dnf install -y maven
```

### 3. Get the Project on the Server

```bash
# Clone from your repo, or use scp to copy files
# Option A: Using git
git clone https://github.com/your-username/your-repo.git authentication-app

# Option B: Using scp (from your local machine)
# scp -r -i your-key.pem /path/to/authentication-app ec2-user@ip:/home/ec2-user/
```

### 4. Configure Neon Database

Your Neon DB is already cloud-hosted — no need to install PostgreSQL. Just make sure the URL in `application.properties` is correct:

```bash
cd authentication-app/backend
nano src/main/resources/application.properties
# Verify spring.datasource.url, username, and password
```

### 5. Build & Run

```bash
cd authentication-app/backend

# Build the project (skip tests for speed)
mvn clean package -DskipTests

# Run the JAR
nohup java -jar target/auth-system-1.0.0.jar > app.log 2>&1 &
```

The app will start on **http://your-instance-public-ip:8080**.

### 6. (Optional) Set up Nginx as Reverse Proxy

This lets you access the app on port 80 (no need to type :8080):

```bash
sudo dnf install -y nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

Create `/etc/nginx/conf.d/auth-app.conf`:

```nginx
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

```bash
sudo nginx -t
sudo systemctl reload nginx
```

Now access at **http://your-instance-public-ip**.

### 7. (Optional) Use systemd to auto-restart

Create `/etc/systemd/system/auth-app.service`:

```ini
[Unit]
Description=Auth System Spring Boot App
After=network.target

[Service]
User=ec2-user
WorkingDirectory=/home/ec2-user/authentication-app/backend
ExecStart=/usr/bin/java -jar /home/ec2-user/authentication-app/backend/target/auth-system-1.0.0.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable auth-app
sudo systemctl start auth-app
sudo journalctl -u auth-app -f   # Watch logs
```

### 8. Useful Commands

```bash
# Check if app is running
ps aux | grep java

# View logs
tail -f app.log

# Restart the app
pkill -f auth-system
nohup java -jar target/auth-system-1.0.0.jar > app.log 2>&1 &
```

## Tech Stack

- **Backend:** Spring Boot 3.2, Spring Security, Spring Data JPA, BCrypt
- **Frontend:** HTML5, DaisyUI 4, TailwindCSS, Vanilla JS (Fetch API)
- **Database:** Neon PostgreSQL
- **Build:** Maven
