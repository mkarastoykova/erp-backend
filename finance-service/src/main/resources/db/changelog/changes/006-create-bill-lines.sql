--liquibase formatted sql

--changeset erp-finance:006-create-bill-lines
CREATE TABLE bill_lines
(
    id          BIGSERIAL      NOT NULL,
    bill_id     BIGINT         NOT NULL,
    line_ref    VARCHAR(20),
    item        VARCHAR(200)   NOT NULL,
    description VARCHAR(500),
    qty         INTEGER        NOT NULL DEFAULT 1,
    price       DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tax_rate    VARCHAR(20)    NOT NULL DEFAULT 'NONE',
    subtotal    DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tax_amount  DECIMAL(15, 2) NOT NULL DEFAULT 0,
    total       DECIMAL(15, 2) NOT NULL DEFAULT 0,
    CONSTRAINT pk_bill_lines PRIMARY KEY (id),
    CONSTRAINT fk_bill_line_bill FOREIGN KEY (bill_id)
        REFERENCES bills (id) ON DELETE CASCADE
);

CREATE INDEX idx_bill_lines_bill_id ON bill_lines (bill_id);

--rollback DROP INDEX idx_bill_lines_bill_id;
--rollback DROP TABLE bill_lines;

