--liquibase formatted sql

--changeset erp-finance:003-create-invoices
CREATE TABLE invoices
(
    id               BIGSERIAL      NOT NULL,
    invoice_number   VARCHAR(30)    NOT NULL,
    type             VARCHAR(30)    NOT NULL,
    customer         VARCHAR(150)   NOT NULL,
    contract         VARCHAR(50),
    date             DATE           NOT NULL,
    due_date         DATE           NOT NULL,
    payment_terms    VARCHAR(20)    NOT NULL,
    status           VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',
    subtotal         DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tax_total        DECIMAL(15, 2) NOT NULL DEFAULT 0,
    amount           DECIMAL(15, 2) NOT NULL DEFAULT 0,
    paid             DECIMAL(15, 2) NOT NULL DEFAULT 0,
    debt             DECIMAL(15, 2) NOT NULL DEFAULT 0,
    note             VARCHAR(500),
    shipment_created BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_invoices PRIMARY KEY (id),
    CONSTRAINT uq_invoice_number UNIQUE (invoice_number)
);

CREATE INDEX idx_invoices_status       ON invoices (status);
CREATE INDEX idx_invoices_customer     ON invoices (customer);
CREATE INDEX idx_invoices_due_date     ON invoices (due_date);

--rollback DROP INDEX idx_invoices_due_date;
--rollback DROP INDEX idx_invoices_customer;
--rollback DROP INDEX idx_invoices_status;
--rollback DROP TABLE invoices;

