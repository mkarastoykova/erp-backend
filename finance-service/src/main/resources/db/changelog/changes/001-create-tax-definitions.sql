--liquibase formatted sql

--changeset erp-finance:001-create-tax-definitions
CREATE TABLE tax_definitions
(
    id       BIGSERIAL    NOT NULL,
    tax_code VARCHAR(20)  NOT NULL,
    name     VARCHAR(100) NOT NULL,
    rate     DOUBLE PRECISION NOT NULL,
    tax_type VARCHAR(20)  NOT NULL,
    active   BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_tax_definitions PRIMARY KEY (id),
    CONSTRAINT uq_tax_code UNIQUE (tax_code)
);

CREATE INDEX idx_tax_definitions_tax_type ON tax_definitions (tax_type);

--rollback DROP INDEX idx_tax_definitions_tax_type;
--rollback DROP TABLE tax_definitions;

