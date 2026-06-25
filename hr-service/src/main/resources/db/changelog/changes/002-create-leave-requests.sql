--liquibase formatted sql

--changeset erp:002-create-leave-requests
CREATE TABLE leave_requests
(
    id          BIGSERIAL   NOT NULL,
    leave_code  VARCHAR(20) NOT NULL,
    employee_id BIGINT      NOT NULL,
    type        VARCHAR(20) NOT NULL,
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    days        INT         NOT NULL,
    reason      VARCHAR(500),
    status      VARCHAR(20) NOT NULL,
    applied_on  DATE        NOT NULL,
    approved_by VARCHAR(150),
    CONSTRAINT pk_leave_requests PRIMARY KEY (id),
    CONSTRAINT uq_leave_code UNIQUE (leave_code)
);

ALTER TABLE leave_requests
    ADD CONSTRAINT fk_leave_employee
        FOREIGN KEY (employee_id)
            REFERENCES employees (id)
            ON DELETE CASCADE;

CREATE INDEX idx_leave_employee_id ON leave_requests (employee_id);
CREATE INDEX idx_leave_status ON leave_requests (status);
CREATE INDEX idx_leave_type ON leave_requests (type);

--rollback DROP INDEX idx_leave_type;
--rollback DROP INDEX idx_leave_status;
--rollback DROP INDEX idx_leave_employee_id;
--rollback ALTER TABLE leave_requests DROP CONSTRAINT fk_leave_employee;
--rollback DROP TABLE leave_requests;

