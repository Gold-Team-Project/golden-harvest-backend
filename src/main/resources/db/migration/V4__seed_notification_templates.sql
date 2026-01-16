INSERT INTO tb_notification_template (type, title, body)
VALUES
    ('SIGNUP_PENDING', '신규 회원 승인 대기',
     '새 회원가입 요청이 접수되었습니다. 관리자 승인 또는 반려 처리를 진행해주세요.')
ON DUPLICATE KEY UPDATE
                     title = VALUES(title),
                     body  = VALUES(body);
