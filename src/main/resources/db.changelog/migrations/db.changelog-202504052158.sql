--liquibase formatted sql
--changeset kayky:202504052158
--comment: board table create

CREATE TABLE BOARDS(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

--rollback DROP TABLE BOARDS