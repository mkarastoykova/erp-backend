--liquibase formatted sql

--changeset erp-crm:003-create-opportunities
CREATE TABLE opportunities
(
    id                  BIGSERIAL      NOT NULL,
    opportunity_code    VARCHAR(20)    NOT NULL,
    name                VARCHAR(255)   NOT NULL,
    account_id          BIGINT,
    account_name        VARCHAR(200),
    contact_id          BIGINT,
    contact_name        VARCHAR(200),
    stage               VARCHAR(30)    NOT NULL,
    value               DECIMAL(15, 2),
    probability         INTEGER,
    close_date          DATE,
    owner               VARCHAR(100),
    created_at          DATE,
    CONSTRAINT pk_opportunities PRIMARY KEY (id),
    CONSTRAINT uq_opportunity_code UNIQUE (opportunity_code),
    CONSTRAINT fk_opp_account FOREIGN KEY (account_id)
        REFERENCES crm_accounts (id) ON DELETE SET NULL,
    CONSTRAINT fk_opp_contact FOREIGN KEY (contact_id)
        REFERENCES crm_contacts (id) ON DELETE SET NULL
);

CREATE INDEX idx_opportunities_stage      ON opportunities (stage);
CREATE INDEX idx_opportunities_owner      ON opportunities (owner);
CREATE INDEX idx_opportunities_account_id ON opportunities (account_id);
CREATE INDEX idx_opportunities_close_date ON opportunities (close_date);

--rollback DROP INDEX idx_opportunities_close_date;
--rollback DROP INDEX idx_opportunities_account_id;
--rollback DROP INDEX idx_opportunities_owner;
--rollback DROP INDEX idx_opportunities_stage;
--rollback DROP TABLE opportunities;

