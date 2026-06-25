--liquibase formatted sql

--changeset erp-payments:001-create-payment-transactions
CREATE TABLE payment_transactions
(
    id           BIGSERIAL      NOT NULL,
    payment_code VARCHAR(30)    NOT NULL,
    date         DATE           NOT NULL,
    payer        VARCHAR(200)   NOT NULL,
    description  VARCHAR(500)   NOT NULL,
    amount       DECIMAL(15, 2) NOT NULL,
    method       VARCHAR(20)    NOT NULL,
    status       VARCHAR(20)    NOT NULL,
    reference    VARCHAR(100),
    CONSTRAINT pk_payment_transactions PRIMARY KEY (id),
    CONSTRAINT uq_payment_code UNIQUE (payment_code)
);

CREATE INDEX idx_payment_transactions_date ON payment_transactions (date);
CREATE INDEX idx_payment_transactions_payer ON payment_transactions (payer);
CREATE INDEX idx_payment_transactions_status ON payment_transactions (status);
CREATE INDEX idx_payment_transactions_method ON payment_transactions (method);

--rollback DROP INDEX idx_payment_transactions_method;
--rollback DROP INDEX idx_payment_transactions_status;
--rollback DROP INDEX idx_payment_transactions_payer;
--rollback DROP INDEX idx_payment_transactions_date;
--rollback DROP TABLE payment_transactions;

