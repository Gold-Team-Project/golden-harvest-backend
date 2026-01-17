CREATE TABLE tb_sales_customer_info (
    customer_email VARCHAR(255) NOT NULL PRIMARY KEY,
    customer_company VARCHAR(20) NOT NULL,
    business_number VARCHAR(20),
    customer_name VARCHAR(20),
    customer_phone VARCHAR(20) NOT NULL,
    address_line1 VARCHAR(20),
    address_line2 VARCHAR(20),
    postal_code VARCHAR(20)
);
