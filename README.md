# Banking_System_Backend
Secure Banking Backend API built using Spring Boot, Spring Security (JWT), JPA, and MySQL. Supports authentication, account management, transactional fund transfers, and optimistic locking for concurrency control.
🏦 Secure Banking Backend API

A production-style Banking Backend REST API built using Spring Boot, Spring Security (JWT), JPA/Hibernate, and MySQL.

This backend system provides secure authentication, account management, deposit/withdrawal operations, and transactional fund transfers with concurrency handling using optimistic locking.

🚀 Features
🔐 Authentication & Authorization

JWT-based authentication

BCrypt password hashing

Stateless session management

Role-based access control

Custom JWT authentication filter

Protected endpoints

👤 User Management

User registration

Login with JWT token generation

Unique email validation

Secure password storage

💳 Account Management

Create bank account

Fetch account details

Link account to user

Unique account number

💰 Transaction Operations

Deposit money

Withdraw money

Transfer funds between accounts

Transaction history with pagination

Prevent insufficient balance

Prevent self-transfer

🔄 Concurrency & Data Integrity

@Transactional for atomic operations

Optimistic locking using @Version

Prevents lost update problem

Ensures ACID compliance

Handles concurrent update conflicts

🛠 Tech Stack

Java 17+

Spring Boot

Spring Security

JWT (JSON Web Token)

Spring Data JPA

Hibernate

MySQL

Maven

🏗 Architecture

Layered Architecture:

Controller → Service → Repository → Database


Controllers handle HTTP requests

Services contain business logic

Repositories interact with database

Security layer handles authentication & authorization

⚙️ How to Run This Project
1️⃣ Clone the Repository
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name

2️⃣ Configure MySQL Database

Create a database:

CREATE DATABASE bank_db;


Update src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/bank_db
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3️⃣ Build & Run Application

Using Maven:

mvn clean install
mvn spring-boot:run


Application runs at:

http://localhost:8080

🔐 How to Use the API

You can use:

Postman

Thunder Client

Curl

Any frontend application

Step 1: Register User

POST /api/auth/register

Example JSON:

{
  "name": "John",
  "email": "john@example.com",
  "password": "password123"
}

Step 2: Login

POST /api/auth/login

{
  "email": "john@example.com",
  "password": "password123"
}


Response:

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

Step 3: Use JWT Token

For all protected endpoints, add header:

Authorization: Bearer <your_token>

Step 4: Create Account

POST /api/accounts

Step 5: Perform Transactions
Deposit

POST /api/accounts/deposit

Withdraw

POST /api/accounts/withdraw

Transfer

POST /api/accounts/transfer

Step 6: View Transactions

GET
/api/accounts/{accountNumber}/transactions?page=0&size=10

🗄 Database Schema Overview
User

id

name

email (unique)

password (BCrypt hashed)

role

Account

id

accountNumber (unique)

balance

user_id (FK)

version (Optimistic Locking)

Transaction

id

fromAccount

toAccount

amount

type

timestamp

🛡 Security Highlights

Passwords hashed using BCrypt

JWT token validation on every request

Stateless authentication

No server-side session

Optimistic locking prevents race conditions

⚠️ Edge Cases Handled

Negative deposit blocked

Insufficient balance blocked

Self-transfer blocked

Duplicate email rejected

Invalid credentials handled

Concurrent updates handled

📈 Future Improvements

DTO layer separation

Unit & integration testing

Docker support

Redis token blacklist

Refresh token mechanism

CI/CD integration

Audit logging

👨‍💻 Author

Durjoy Chatterjee
Backend Developer – Java & Spring Boot
