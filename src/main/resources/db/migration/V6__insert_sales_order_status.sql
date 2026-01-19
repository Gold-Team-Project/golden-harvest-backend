-- V6__insert_sales_order_status.sql
INSERT INTO tb_sales_order_status (sales_status_id, sales_status_name, sales_status_type) VALUES
(1, '주문 접수', 'PENDING'),
(2, '결제 완료', 'PAID'),
(3, '배송 준비중', 'PREPARING'),
(4, '배송 중', 'SHIPPING'),
(5, '배송 완료', 'DELIVERED'),
(6, '주문 취소', 'CANCELLED');
