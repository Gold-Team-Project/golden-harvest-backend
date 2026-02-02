-- 품목 마스터 미러 데이터
INSERT INTO tb_item_master_mirror (sku_no, item_name, grade_name, variety_name, base_unit, current_origin_price, is_active, file_url)
VALUES
    ('SKU_20260120_000001', '부사 사과', '특급', '부사', 'BOX', 30000.00, 1, 'file_url_1'),
    ('SKU_20260120_000002', '신고 배', '특급', '신고', 'BOX', 45000.00, 1, 'file_url_2'),
    ('SKU_20260120_000003', '샤인머스켓', '1등급', '샤인', 'KG', 25000.00, 1, 'file_url_3'),
    ('SKU_20260120_000004', '제주 감귤', '2등급', '조생', 'BOX', 15000.00, 1, 'file_url_4'),
    ('SKU_20260120_000005', '단감', '특급', '부유', 'BOX', 20000.00, 0, 'file_url_5');

-- 가격 정책 데이터 (마진율 15%)
INSERT INTO tb_price_policy (sku_no, margin_rate, is_active)
VALUES
    ('SKU_20260120_000001', 0.15, 1),
    ('SKU_20260120_000002', 0.20, 1),
    ('SKU_20260120_000003', 0.10, 1),
    ('SKU_20260120_000004', 0.25, 1),
    ('SKU_20260120_000005', 0.15, 1);

-- 입고 데이터
INSERT INTO tb_inbound (inbound_id, purchase_order_item_id, sku_no, quantity, inbound_date)
VALUES
    ('INB_20260120_000001', 'POI_001', 'SKU_20260120_000001', 100, '2026-01-20'),
    ('INB_20260120_000002', 'POI_002', 'SKU_20260120_000002', 50, '2026-01-18'),
    ('INB_20260120_000003', 'POI_003', 'SKU_20260120_000003', 30, '2026-01-19'),
    ('INB_20260120_000004', 'POI_004', 'SKU_20260120_000004', 200, '2026-01-17');

-- 로트 데이터 (입고를 통해 생성됨, 현재 20개 출고 후 80개 남은 상태로 가정)
INSERT INTO tb_lot (lot_no, inbound_id, sku_no, quantity, inbound_date, lot_status)
VALUES
    ('LOT_20260120_000001', 'INB_20260120_000001', 'SKU_20260120_000001', 80, '2026-01-20', 'AVAILABLE'),
    ('LOT_20260120_000002', 'INB_20260120_000002', 'SKU_20260120_000002', 50, '2026-01-20', 'AVAILABLE'),
    ('LOT_20260120_000003', 'INB_20260120_000003', 'SKU_20260120_000003', 0, '2026-01-19', 'DEPLETED'),
    ('LOT_20260120_000004', 'INB_20260120_000004', 'SKU_20260120_000004', 150, '2026-01-20', 'AVAILABLE');

-- 출고 데이터 (판매가 = 원가 30,000 * 1.15 = 34,500)
INSERT INTO tb_outbound (outbound_id, sales_order_item_id, lot_no, outbound_date, quantity, outbound_price)
VALUES ('OUT_20260120_000001', 'SOI_20260120_000001', 'LOT_20260120_000001', '2026-01-20', 20, 34500.00),
       ('OUT_20260120_000002', 'SOI_002', 'LOT_20260120_000003', '2026-01-19', 30, 27500.00);

-- 로트 이력 데이터
INSERT INTO tb_lot_history (lot_history_id, lot_no, from_status, to_status, changed_at, changed_by)
VALUES
    ('HIS_20260120_000001', 'LOT_20260120_000001', NULL, 'AVAILABLE', '2026-01-20 09:00:00', 'SYSTEM'),
    ('HIS_20260120_000002', 'LOT_20260120_000003', 'AVAILABLE', 'DEPLETED', '2026-01-20 11:00:00', 'WORKER_A'),
    ('HIS_20260120_000003', 'LOT_20260120_000004', NULL, 'AVAILABLE', '2026-01-20 13:00:00', 'SYSTEM');