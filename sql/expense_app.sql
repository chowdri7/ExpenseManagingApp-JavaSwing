CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    other_field1 VARCHAR(100),
    other_field2 VARCHAR(100),
    password VARCHAR(100) NOT NULL
);

CREATE TABLE passbooks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    passbook_id INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    category VARCHAR(100),
    amount DOUBLE,
    remarks VARCHAR(255),
    person_name VARCHAR(100),
    contact_details VARCHAR(100),
    mode_of_transaction VARCHAR(50),
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (passbook_id) REFERENCES passbooks(id) ON DELETE CASCADE
);
