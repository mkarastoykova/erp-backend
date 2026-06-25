--liquibase formatted sql

--changeset erp-finance:004-create-invoice-lines
CREATE TABLE invoice_lines
(
    id          BIGSERIAL      NOT NULL,
    invoice_id  BIGINT         NOT NULL,
    line_ref    VARCHAR(20),
    item        VARCHAR(200)   NOT NULL,
    description VARCHAR(500),
    qty         INTEGER        NOT NULL DEFAULT 1,
    price       DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tax_rate    VARCHAR(20)    NOT NULL DEFAULT 'NONE',
    subtotal    DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tax_amount  DECIMAL(15, 2) NOT NULL DEFAULT 0,
    total       DECIMAL(15, 2) NOT NULL DEFAULT 0,
    CONSTRAINT pk_invoice_lines PRIMARY KEY (id),
    CONSTRAINT fk_invoice_line_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices (id) ON DELETE CASCADE
);

CREATE INDEX idx_invoice_lines_invoice_id ON invoice_lines (invoice_id);

--rollback DROP INDEX idx_invoice_lines_invoice_id;
--rollback DROP TABLE invoice_lines;

