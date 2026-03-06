create DATABASE IF NOT EXISTS `bank_db`;

DROP DATABASE `bank_db`;

USE `bank_db`;

CREATE TABLE Customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_no VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    two_factor_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) AUTO_INCREMENT = 101000;

CREATE TABLE Account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    secret_key VARCHAR(255) NOT NULL UNIQUE,
    balance DECIMAL(15, 2) NOT NULL,
    customer_id INT NOT NULL,
    status ENUM('active', 'inactive') NOT NULL DEFAULT 'active',
    type ENUM('savings', 'checking') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at_branch INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer (id),
    FOREIGN KEY (created_at_branch) REFERENCES Branch (id)
) AUTO_INCREMENT = 102000;

CREATE TABLE Branch (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    division VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(255) NOT NULL,
) AUTO_INCREMENT = 103000;

CREATE TABLE Employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL,
    salary DECIMAL(15, 2) NOT NULL,
    date_of_birth DATE,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_no VARCHAR(20) NOT NULL,
    work_branch_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (work_branch_id) REFERENCES Branch (id)
) AUTO_INCREMENT = 104000;

CREATE TABLE Transaction (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('deposit','withdrawal','transfer') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    account_id INT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Account (id)
) AUTO_INCREMENT = 105000;

CREATE TABLE Loan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    amount DECIMAL(15, 2) NOT NULL,
    loan_type INT NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    term_months INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    account_id INT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Account (id),
    FOREIGN KEY (loan_type) REFERENCES LoanType (id)
) AUTO_INCREMENT = 106000;

CREATE TABLE LoanType (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
) AUTO_INCREMENT = 107000;