CREATE TABLE USERS(
    id VARCHAR(10) NOT NULL,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(10) NOT NULL,
    level INT NOT NULL,
    login INT NOT NULL,
    recommend INT NOT NULL,
    email VARCHAR(100) NOT NULL
);
ALTER TABLE USERS ADD CONSTRAINT CONSTRAINT_4 PRIMARY KEY(id);