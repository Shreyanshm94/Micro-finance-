CREATE DATABASE loan_management_system;

USE loan_management_system;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- Create loans table
CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    borrower_name VARCHAR(100),
    phone_number VARCHAR(20),
    address VARCHAR(255),
    loan_amount DOUBLE,
    interest_rate DOUBLE,
    duration_months INT,
    due_amount DOUBLE
);
