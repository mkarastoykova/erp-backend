--liquibase formatted sql

--changeset erp-crm:005-create-support-cases
CREATE TABLE support_cases
(
    id           BIGSERIAL     NOT NULL,
    case_code    VARCHAR(20)   NOT NULL,
    subject      VARCHAR(255)  NOT NULL,
    account_id   BIGINT,
    account_name VARCHAR(200),
    contact_name VARCHAR(200),
    status       VARCHAR(30)   NOT NULL DEFAULT 'OPEN',
    priority     VARCHAR(20)   NOT NULL DEFAULT 'MEDIUM',
    assigned_to  VARCHAR(100),
    created_at   DATE,
    updated_at   DATE,
    description  VARCHAR(2000),
    CONSTRAINT pk_support_cases PRIMARY KEY (id),
    CONSTRAINT uq_case_code UNIQUE (case_code),
    CONSTRAINT fk_case_account FOREIGN KEY (account_id)
        REFERENCES crm_accounts (id) ON DELETE SET NULL
);

CREATE INDEX idx_support_cases_status     ON support_cases (status);
CREATE INDEX idx_support_cases_priority   ON support_cases (priority);
CREATE INDEX idx_support_cases_account_id ON support_cases (account_id);
CREATE INDEX idx_support_cases_assigned   ON support_cases (assigned_to);

--rollback DROP INDEX idx_support_cases_assigned;
--rollback DROP INDEX idx_support_cases_account_id;
--rollback DROP INDEX idx_support_cases_priority;
--rollback DROP INDEX idx_support_cases_status;
--rollback DROP TABLE support_cases;

