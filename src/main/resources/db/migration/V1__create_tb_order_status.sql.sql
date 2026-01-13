CREATE TABLE IF NOT EXISTS tb_order_status (
                                               order_status_type VARCHAR(20) NOT NULL,
                                               order_status_name VARCHAR(20) NOT NULL,
                                               PRIMARY KEY (order_status_type)
);
