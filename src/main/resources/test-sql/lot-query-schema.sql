-- 1. 품목 마스터 미러 (ItemMasterMirror)
CREATE TABLE tb_item_master_mirror (
                                       sku_no VARCHAR(255) NOT NULL,
                                       item_name VARCHAR(255),
                                       grade_name VARCHAR(255),
                                       variety_name VARCHAR(255),
                                       base_unit VARCHAR(255),
                                       current_origin_price DECIMAL(10, 2),
                                       is_active BOOLEAN NOT NULL,
                                       PRIMARY KEY (sku_no)
);

-- 2. 가격 정책 (PricePolicy)
CREATE TABLE tb_price_policy (
                                 sku_no VARCHAR(255) NOT NULL,
                                 margin_rate DECIMAL(10, 2) NOT NULL,
                                 is_active BOOLEAN,
                                 PRIMARY KEY (sku_no)
);

-- 3. 입고 (Inbound)
CREATE TABLE tb_inbound (
                            inbound_id VARCHAR(20) NOT NULL,
                            purchase_order_item_id VARCHAR(20) NOT NULL,
                            sku_no VARCHAR(20) NOT NULL,
                            quantity INTEGER NOT NULL,
                            inbound_date DATE,
                            PRIMARY KEY (inbound_id)
);

-- 4. 로트 (Lot)
CREATE TABLE tb_lot (
                        lot_no VARCHAR(20) NOT NULL,
                        inbound_id VARCHAR(20) NOT NULL,
                        sku_no VARCHAR(20) NOT NULL,
                        quantity INTEGER NOT NULL,
                        inbound_date DATE,
                        lot_status VARCHAR(255) NOT NULL, -- Enum: AVAILABLE, ALLOCATED, DEPLETED, DISCARDED
                        PRIMARY KEY (lot_no)
);

-- 5. 로트 이력 (LotHistory)
CREATE TABLE tb_lot_history (
                                lot_history_id VARCHAR(20) NOT NULL,
                                lot_no VARCHAR(20) NOT NULL,
                                from_status VARCHAR(20),
                                to_status VARCHAR(20),
                                changed_at DATETIME(6) NOT NULL,
                                changed_by VARCHAR(255),
                                PRIMARY KEY (lot_history_id),
                                CONSTRAINT fk_lot_history_lot FOREIGN KEY (lot_no) REFERENCES tb_lot (lot_no)
);

-- 6. 출고 (Outbound)
CREATE TABLE tb_outbound (
                             outbound_id VARCHAR(20) NOT NULL,
                             sales_order_item_id VARCHAR(20) NOT NULL,
                             lot_no VARCHAR(20) NOT NULL,
                             outbound_date DATE,
                             quantity INTEGER NOT NULL,
                             outbound_price DECIMAL(10, 2),
                             PRIMARY KEY (outbound_id),
                             CONSTRAINT fk_outbound_lot FOREIGN KEY (lot_no) REFERENCES tb_lot (lot_no)
);