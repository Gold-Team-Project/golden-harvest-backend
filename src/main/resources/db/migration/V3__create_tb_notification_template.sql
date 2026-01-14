CREATE TABLE IF NOT EXISTS tb_notification_template (
                                                        type        VARCHAR(50)  NOT NULL,
                                                        title       VARCHAR(255) NULL,
                                                        body        TEXT         NULL,
                                                        created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                        PRIMARY KEY (type)
);
