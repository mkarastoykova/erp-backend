--liquibase formatted sql

--changeset erp:003-create-payroll-records
CREATE TABLE payroll_records
(
    id             BIGSERIAL     NOT NULL,
    payroll_code   VARCHAR(20)   NOT NULL,
    employee_id    BIGINT        NOT NULL,
    month          VARCHAR(7)    NOT NULL,
    base_salary    DECIMAL(15,2) NOT NULL,
    bonus          DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    deductions     DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    tax_federal    DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    tax_state      DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    tax_ss         DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    tax_medicare   DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    net_pay        DECIMAL(15,2) NOT NULL,
    status         VARCHAR(20)   NOT NULL,
    payment_method VARCHAR(30)   NOT NULL,
    bank_last4     VARCHAR(4),
    disbursed      BOOLEAN       NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_payroll_records PRIMARY KEY (id),
    CONSTRAINT uq_payroll_code UNIQUE (payroll_code),
    CONSTRAINT uq_payroll_employee_month UNIQUE (employee_id, month)
);

ALTER TABLE payroll_records
    ADD CONSTRAINT fk_payroll_employee
        FOREIGN KEY (employee_id)
            REFERENCES employees (id)
            ON DELETE CASCADE;

CREATE INDEX idx_payroll_employee_id ON payroll_records (employee_id);
CREATE INDEX idx_payroll_month ON payroll_records (month);
CREATE INDEX idx_payroll_status ON payroll_records (status);

--rollback DROP INDEX idx_payroll_status;
--rollback DROP INDEX idx_payroll_month;
--rollback DROP INDEX idx_payroll_employee_id;
--rollback ALTER TABLE payroll_records DROP CONSTRAINT fk_payroll_employee;
--rollback DROP TABLE payroll_records;

