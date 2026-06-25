--liquibase formatted sql

--changeset erp:005-create-employee-benefits
CREATE TABLE employee_benefits
(
    id            BIGSERIAL     NOT NULL,
    benefit_code  VARCHAR(20)   NOT NULL,
    employee_id   BIGINT        NOT NULL,
    type          VARCHAR(30)   NOT NULL,
    plan          VARCHAR(150)  NOT NULL,
    employer_cost DECIMAL(15,2) NOT NULL,
    employee_cost DECIMAL(15,2) NOT NULL,
    enrolled_date DATE          NOT NULL,
    status        VARCHAR(30)   NOT NULL,
    CONSTRAINT pk_employee_benefits PRIMARY KEY (id),
    CONSTRAINT uq_benefit_code UNIQUE (benefit_code)
);

ALTER TABLE employee_benefits
    ADD CONSTRAINT fk_benefit_employee
        FOREIGN KEY (employee_id)
            REFERENCES employees (id)
            ON DELETE CASCADE;

CREATE INDEX idx_benefit_employee_id ON employee_benefits (employee_id);
CREATE INDEX idx_benefit_type ON employee_benefits (type);
CREATE INDEX idx_benefit_status ON employee_benefits (status);

--rollback DROP INDEX idx_benefit_status;
--rollback DROP INDEX idx_benefit_type;
--rollback DROP INDEX idx_benefit_employee_id;
--rollback ALTER TABLE employee_benefits DROP CONSTRAINT fk_benefit_employee;
--rollback DROP TABLE employee_benefits;

