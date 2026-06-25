--liquibase formatted sql

--changeset erp-finance:005-create-bills
CREATE TABLE bills
(
    id                   BIGSERIAL      NOT NULL,
    bill_number          VARCHAR(30)    NOT NULL,
    type                 VARCHAR(30)    NOT NULL,
    vendor               VARCHAR(150)   NOT NULL,
    contract             VARCHAR(50),
    date                 DATE           NOT NULL,
    due_date             DATE           NOT NULL,
    payment_terms        VARCHAR(20)    NOT NULL,
    status               VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',
    subtotal             DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tax_total            DECIMAL(15, 2) NOT NULL DEFAULT 0,
    amount               DECIMAL(15, 2) NOT NULL DEFAULT 0,
    paid                 DECIMAL(15, 2) NOT NULL DEFAULT 0,
    debt                 DECIMAL(15, 2) NOT NULL DEFAULT 0,
    note                 VARCHAR(500),
    original_bill_number VARCHAR(30),
    correction_mode      VARCHAR(20),
    CONSTRAINT pk_bills PRIMARY KEY (id),
    CONSTRAINT uq_bill_number UNIQUE (bill_number)
);

CREATE INDEX idx_bills_status               ON bills (status);
CREATE INDEX idx_bills_vendor               ON bills (vendor);
CREATE INDEX idx_bills_original_bill_number ON bills (original_bill_number);

--rollback DROP INDEX idx_bills_original_bill_number;
--rollback DROP INDEX idx_bills_vendor;
--rollback DROP INDEX idx_bills_status;
--rollback DROP TABLE bills;

