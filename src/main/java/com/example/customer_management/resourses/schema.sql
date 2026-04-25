CREATE TABLE customers (
        id BIGINT(20) NOT NULL AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL,
        dob DATE NOT NULL,
        nic VARCHAR(20) NOT NULL,
        parent_customer_id BIGINT(20) DEFAULT NULL,
        PRIMARY KEY (id),
        UNIQUE KEY (nic),
        CONSTRAINT fk_parent_customer FOREIGN KEY (parent_customer_id) REFERENCES customers (id)
);

CREATE TABLE customer_mobile (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        customer_id BIGINT,
        mobile_number VARCHAR(15),
        FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE addresses (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        customer_id BIGINT,
        line1 VARCHAR(255),
        line2 VARCHAR(255),
        city_id BIGINT,
        country_id BIGINT,
        FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE customer_family (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        customer_id BIGINT,
        family_member_id BIGINT,
        FOREIGN KEY (customer_id) REFERENCES customers(id),
        FOREIGN KEY (family_member_id) REFERENCES customers(id)
);


CREATE TABLE countries (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100)
);

CREATE TABLE cities (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100),
        country_id BIGINT,
        FOREIGN KEY (country_id) REFERENCES countries(id)
);