--liquibase formatted sql

--changeset erp-crm:002-create-crm-contacts
CREATE TABLE crm_contacts
(
    id            BIGSERIAL    NOT NULL,
    contact_code  VARCHAR(20)  NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    title         VARCHAR(150),
    company       VARCHAR(200),
    account_id    BIGINT,
    email         VARCHAR(150) NOT NULL,
    phone         VARCHAR(30),
    owner         VARCHAR(100),
    last_activity DATE,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at    DATE,
    CONSTRAINT pk_crm_contacts PRIMARY KEY (id),
    CONSTRAINT uq_contact_code UNIQUE (contact_code),
    CONSTRAINT uq_contact_email UNIQUE (email),
    CONSTRAINT fk_contact_account FOREIGN KEY (account_id)
        REFERENCES crm_accounts (id) ON DELETE SET NULL
);

CREATE INDEX idx_crm_contacts_account_id ON crm_contacts (account_id);
CREATE INDEX idx_crm_contacts_owner      ON crm_contacts (owner);
CREATE INDEX idx_crm_contacts_status     ON crm_contacts (status);

--rollback DROP INDEX idx_crm_contacts_status;
--rollback DROP INDEX idx_crm_contacts_owner;
--rollback DROP INDEX idx_crm_contacts_account_id;
--rollback DROP TABLE crm_contacts;

