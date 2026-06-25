--liquibase formatted sql

--changeset erp:001-create-employees
CREATE TABLE employees
(
    id                BIGSERIAL    NOT NULL,
    employee_code     VARCHAR(20)  NOT NULL,
    first_name        VARCHAR(100) NOT NULL,
    last_name         VARCHAR(100) NOT NULL,
    department        VARCHAR(100) NOT NULL,
    position          VARCHAR(150) NOT NULL,
    email             VARCHAR(150) NOT NULL,
    phone             VARCHAR(30),
    start_date        DATE         NOT NULL,
    salary            DECIMAL(15, 2) NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    date_of_birth     DATE,
    gender            VARCHAR(30),
    employment_type   VARCHAR(20)  NOT NULL,
    address           VARCHAR(255),
    city              VARCHAR(100),
    state             VARCHAR(100),
    country           VARCHAR(100),
    postal_code       VARCHAR(20),
    emergency_contact VARCHAR(150),
    emergency_phone   VARCHAR(30),
    manager_id        BIGINT,
    CONSTRAINT pk_employees PRIMARY KEY (id),
    CONSTRAINT uq_employee_code UNIQUE (employee_code),
    CONSTRAINT uq_employee_email UNIQUE (email)
);

ALTER TABLE employees
    ADD CONSTRAINT fk_employee_manager
        FOREIGN KEY (manager_id)
            REFERENCES employees (id)
            ON DELETE SET NULL;

CREATE INDEX idx_employees_department ON employees (department);
CREATE INDEX idx_employees_status ON employees (status);
CREATE INDEX idx_employees_manager_id ON employees (manager_id);

--rollback DROP INDEX idx_employees_manager_id;
--rollback DROP INDEX idx_employees_status;
--rollback DROP INDEX idx_employees_department;
--rollback ALTER TABLE employees DROP CONSTRAINT fk_employee_manager;
--rollback DROP TABLE employees;

