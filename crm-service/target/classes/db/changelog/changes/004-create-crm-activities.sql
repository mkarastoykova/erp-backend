--liquibase formatted sql

--changeset erp-crm:004-create-crm-activities
CREATE TABLE crm_activities
(
    id            BIGSERIAL    NOT NULL,
    activity_code VARCHAR(20)  NOT NULL,
    type          VARCHAR(20)  NOT NULL,
    subject       VARCHAR(255) NOT NULL,
    contact_id    BIGINT,
    contact_name  VARCHAR(200),
    account_name  VARCHAR(200),
    owner         VARCHAR(100),
    date          DATE,
    outcome       VARCHAR(30)  NOT NULL,
    notes         VARCHAR(1000),
    CONSTRAINT pk_crm_activities PRIMARY KEY (id),
    CONSTRAINT uq_activity_code UNIQUE (activity_code),
    CONSTRAINT fk_activity_contact FOREIGN KEY (contact_id)
        REFERENCES crm_contacts (id) ON DELETE SET NULL
);

CREATE INDEX idx_crm_activities_type       ON crm_activities (type);
CREATE INDEX idx_crm_activities_contact_id ON crm_activities (contact_id);
CREATE INDEX idx_crm_activities_date       ON crm_activities (date);

--rollback DROP INDEX idx_crm_activities_date;
--rollback DROP INDEX idx_crm_activities_contact_id;
--rollback DROP INDEX idx_crm_activities_type;
--rollback DROP TABLE crm_activities;

