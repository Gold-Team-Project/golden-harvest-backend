INSERT INTO tb_lot_status (status_code, status_name) VALUES ('AVAILABLE', '가용');

INSERT INTO tb_item_master_mirror (sku_no, item_name, current_origin_price, is_active)
VALUES ('SKU001', '유기농 사과', 10000.00, true);

INSERT INTO tb_lot (lot_no, inbound_id, sku_no, quantity, lot_status)
VALUES ('LOT001', 'IN001', 'SKU001', 50, 'AVAILABLE');

INSERT INTO tb_price_policy (sku_no, margin_rate, is_active)
VALUES ('SKU001', 0.15, true);