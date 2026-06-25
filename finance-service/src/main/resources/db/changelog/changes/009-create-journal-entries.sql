--liquibase formatted sql

--changeset erp-finance:009-create-journal-entries
CREATE TABLE journal_entries
(
    id           BIGSERIAL      NOT NULL,
    entry_number VARCHAR(30)    NOT NULL,
    date         DATE           NOT NULL,
    description  VARCHAR(255)   NOT NULL,
    debit        DECIMAL(15, 2) NOT NULL DEFAULT 0,
    credit       DECIMAL(15, 2) NOT NULL DEFAULT 0,
    account      VARCHAR(50)    NOT NULL,
    CONSTRAINT pk_journal_entries PRIMARY KEY (id),
    CONSTRAINT uq_entry_number UNIQUE (entry_number)
);

CREATE INDEX idx_journal_entries_date    ON journal_entries (date);
CREATE INDEX idx_journal_entries_account ON journal_entries (account);

--rollback DROP INDEX idx_journal_entries_account;
--rollback DROP INDEX idx_journal_entries_date;
--rollback DROP TABLE journal_entries;

