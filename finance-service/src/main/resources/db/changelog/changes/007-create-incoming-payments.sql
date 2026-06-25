--liquibase formatted sql

--changeset erp-finance:007-create-incoming-payments
CREATE TABLE incoming_payments
(
    id              BIGSERIAL      NOT NULL,
    payment_number  VARCHAR(30)    NOT NULL,
    type            VARCHAR(10)    NOT NULL,
    date            DATE           NOT NULL,
    amount          DECIMAL(15, 2) NOT NULL,
    partner         VARCHAR(150),
    partner_account VARCHAR(100),
    company         VARCHAR(150),
    company_account VARCHAR(50),
    note            VARCHAR(500),
    base            VARCHAR(50),
    status          VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',
    match_status    VARCHAR(20)    NOT NULL DEFAULT 'NOT_MATCHED',
    matched_amount  DECIMAL(15, 2) NOT NULL DEFAULT 0,
    CONSTRAINT pk_incoming_payments PRIMARY KEY (id),
    CONSTRAINT uq_incoming_payment_number UNIQUE (payment_number)
);

CREATE TABLE incoming_payment_matched_docs
(
    payment_id BIGINT      NOT NULL,
    doc_number VARCHAR(50) NOT NULL,
    CONSTRAINT fk_ipmd_payment FOREIGN KEY (payment_id)
        REFERENCES incoming_payments (id) ON DELETE CASCADE
);

CREATE INDEX idx_incoming_payments_status       ON incoming_payments (status);
CREATE INDEX idx_incoming_payments_match_status ON incoming_payments (match_status);
CREATE INDEX idx_ipmd_payment_id                ON incoming_payment_matched_docs (payment_id);

--rollback DROP INDEX idx_ipmd_payment_id;
--rollback DROP INDEX idx_incoming_payments_match_status;
--rollback DROP INDEX idx_incoming_payments_status;
--rollback DROP TABLE incoming_payment_matched_docs;
--rollback DROP TABLE incoming_payments;

