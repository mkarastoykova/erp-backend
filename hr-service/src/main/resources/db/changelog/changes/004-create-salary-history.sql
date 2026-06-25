--liquibase formatted sql

--changeset erp:004-create-salary-history
CREATE TABLE salary_history
(
    id               BIGSERIAL     NOT NULL,
    history_code     VARCHAR(20)   NOT NULL,
    employee_id      BIGINT        NOT NULL,
    effective_date   DATE          NOT NULL,
    previous_salary  DECIMAL(15,2) NOT NULL,
    new_salary       DECIMAL(15,2) NOT NULL,
    increase_amount  DECIMAL(15,2) NOT NULL,
    increase_percent DECIMAL(7,2)  NOT NULL,
    reason           VARCHAR(500),
    approved_by      VARCHAR(150)  NOT NULL,
    CONSTRAINT pk_salary_history PRIMARY KEY (id),
    CONSTRAINT uq_salary_history_code UNIQUE (history_code)
);

ALTER TABLE salary_history
    ADD CONSTRAINT fk_salary_history_employee
        FOREIGN KEY (employee_id)
            REFERENCES employees (id)
            ON DELETE CASCADE;

CREATE INDEX idx_salary_history_employee_id ON salary_history (employee_id);
CREATE INDEX idx_salary_history_effective_date ON salary_history (effective_date);

--rollback DROP INDEX idx_salary_history_effective_date;
--rollback DROP INDEX idx_salary_history_employee_id;
--rollback ALTER TABLE salary_history DROP CONSTRAINT fk_salary_history_employee;
--rollback DROP TABLE salary_history;

