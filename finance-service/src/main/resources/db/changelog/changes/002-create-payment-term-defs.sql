--liquibase formatted sql

--changeset erp-finance:002-create-payment-term-defs
CREATE TABLE payment_term_defs
(
    id          BIGSERIAL   NOT NULL,
    term_code   VARCHAR(20) NOT NULL,
    name        VARCHAR(50) NOT NULL,
    due_days    INTEGER     NOT NULL DEFAULT 0,
    description VARCHAR(255),
    CONSTRAINT pk_payment_term_defs PRIMARY KEY (id),
    CONSTRAINT uq_term_code UNIQUE (term_code)
);

--rollback DROP TABLE payment_term_defs;

