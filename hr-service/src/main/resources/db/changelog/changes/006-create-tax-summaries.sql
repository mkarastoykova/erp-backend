--liquibase formatted sql

--changeset erp:006-create-tax-summaries
CREATE TABLE tax_summaries
(
    id               BIGSERIAL     NOT NULL,
    tax_code         VARCHAR(20)   NOT NULL,
    employee_id      BIGINT        NOT NULL,
    year             INT           NOT NULL,
    gross_earnings   DECIMAL(15,2) NOT NULL,
    federal_withheld DECIMAL(15,2) NOT NULL,
    state_withheld   DECIMAL(15,2) NOT NULL,
    social_security  DECIMAL(15,2) NOT NULL,
    medicare         DECIMAL(15,2) NOT NULL,
    total_taxes      DECIMAL(15,2) NOT NULL,
    filing_status    VARCHAR(40)   NOT NULL,
    w2_issued        BOOLEAN       NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_tax_summaries PRIMARY KEY (id),
    CONSTRAINT uq_tax_code UNIQUE (tax_code),
    CONSTRAINT uq_tax_employee_year UNIQUE (employee_id, year)
);

ALTER TABLE tax_summaries
    ADD CONSTRAINT fk_tax_employee
        FOREIGN KEY (employee_id)
            REFERENCES employees (id)
            ON DELETE CASCADE;

CREATE INDEX idx_tax_employee_id ON tax_summaries (employee_id);
CREATE INDEX idx_tax_year ON tax_summaries (year);

--rollback DROP INDEX idx_tax_year;
--rollback DROP INDEX idx_tax_employee_id;
--rollback ALTER TABLE tax_summaries DROP CONSTRAINT fk_tax_employee;
--rollback DROP TABLE tax_summaries;

