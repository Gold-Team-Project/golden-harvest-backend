INSERT INTO tb_order_status (order_status_type, order_status_name) VALUES
                                                                       ('DRAFT', '임시저장'),
                                                                       ('SUBMITTED', '제출'),
                                                                       ('APPROVED', '승인'),
                                                                       ('REJECTED', '반려'),
                                                                       ('SENT', '발주전송'),
                                                                       ('CANCELLED', '취소')
ON DUPLICATE KEY UPDATE
    order_status_name = VALUES(order_status_name);