-- 품목 마스터 미러
CREATE TABLE tb_item_master_mirror (
                                       sku_no VARCHAR(20) PRIMARY KEY,
                                       item_name VARCHAR(255),
                                       grade_name VARCHAR(255),
                                       variety_name VARCHAR(255),
                                       base_unit VARCHAR(255),
                                       current_origin_price DECIMAL(10, 2),
                                       is_active BOOLEAN DEFAULT TRUE
);

-- 로트 상태 (코드 테이블)
CREATE TABLE tb_lot_status (
                               status_code VARCHAR(20) PRIMARY KEY,
                               status_name VARCHAR(50)
);

-- 로트
CREATE TABLE tb_lot (
                        lot_no VARCHAR(20) PRIMARY KEY,
                        inbound_id VARCHAR(20) NOT NULL,
                        sku_no VARCHAR(20) NOT NULL,
                        quantity INTEGER NOT NULL,
                        inbound_date DATE,
                        lot_status VARCHAR(20) NOT NULL,
                        FOREIGN KEY (sku_no) REFERENCES tb_item_master_mirror(sku_no)
);

-- 가격 정책
CREATE TABLE tb_price_policy (
                                 sku_no VARCHAR(20) PRIMARY KEY,
                                 margin_rate DECIMAL(10, 2) NOT NULL,
                                 is_active BOOLEAN DEFAULT TRUE
);