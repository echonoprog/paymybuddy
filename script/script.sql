
CREATE DATABASE IF NOT EXISTS paymybuddy;
USE paymybuddy;


CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    balance FLOAT
);


CREATE TABLE IF NOT EXISTS user_contacts (
    user_id INT,
    contact_id INT,
    PRIMARY KEY (user_id, contact_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (contact_id) REFERENCES user(id)
);


CREATE TABLE IF NOT EXISTS bank_account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    iban VARCHAR(255),
    bank_name VARCHAR(255),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);


CREATE TABLE IF NOT EXISTS bank_transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    bank_account_id INT,
    date DATE,
    amountbank FLOAT,
    credit BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);


CREATE TABLE IF NOT EXISTS transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT,
    receiver_id INT,
    transaction_date DATE,
    amount FLOAT,
    commission FLOAT,
    description VARCHAR(255),
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
);
