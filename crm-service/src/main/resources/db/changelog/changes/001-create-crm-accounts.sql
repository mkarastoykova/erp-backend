--liquibase formatted sql

--changeset erp-crm:001-create-crm-accounts
CREATE TABLE crm_accounts
(
    id             BIGSERIAL      NOT NULL,
    account_code   VARCHAR(20)    NOT NULL,
    name           VARCHAR(200)   NOT NULL,
    industry       VARCHAR(100),
    type           VARCHAR(20)    NOT NULL,
    employees      INTEGER,
    annual_revenue DOUBLE PRECISION,
    owner          VARCHAR(100),
    phone          VARCHAR(30),
    website        VARCHAR(200),
    country        VARCHAR(100),
    status         VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    created_at     DATE,
    CONSTRAINT pk_crm_accounts PRIMARY KEY (id),
    CONSTRAINT uq_account_code UNIQUE (account_code)
);

CREATE INDEX idx_crm_accounts_type    ON crm_accounts (type);
CREATE INDEX idx_crm_accounts_status  ON crm_accounts (status);
CREATE INDEX idx_crm_accounts_owner   ON crm_accounts (owner);

--rollback DROP INDEX idx_crm_accounts_owner;
--rollback DROP INDEX idx_crm_accounts_status;
--rollback DROP INDEX idx_crm_accounts_type;
--rollback DROP TABLE crm_accounts;

