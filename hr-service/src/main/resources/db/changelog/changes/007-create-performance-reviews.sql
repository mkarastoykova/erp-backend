--liquibase formatted sql

--changeset erp:007-create-performance-reviews
CREATE TABLE performance_reviews
(
    id          BIGSERIAL   NOT NULL,
    review_code VARCHAR(20) NOT NULL,
    employee_id BIGINT      NOT NULL,
    period      VARCHAR(20) NOT NULL,
    rating      INT,
    goals       TEXT,
    reviewer    VARCHAR(150) NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    notes       TEXT,
    review_date DATE,
    CONSTRAINT pk_performance_reviews PRIMARY KEY (id),
    CONSTRAINT uq_review_code UNIQUE (review_code),
    CONSTRAINT chk_rating CHECK (rating IS NULL OR (rating >= 0 AND rating <= 5))
);

ALTER TABLE performance_reviews
    ADD CONSTRAINT fk_review_employee
        FOREIGN KEY (employee_id)
            REFERENCES employees (id)
            ON DELETE CASCADE;

CREATE INDEX idx_review_employee_id ON performance_reviews (employee_id);
CREATE INDEX idx_review_period ON performance_reviews (period);
CREATE INDEX idx_review_status ON performance_reviews (status);

--rollback DROP INDEX idx_review_status;
--rollback DROP INDEX idx_review_period;
--rollback DROP INDEX idx_review_employee_id;
--rollback ALTER TABLE performance_reviews DROP CONSTRAINT fk_review_employee;
--rollback DROP TABLE performance_reviews;

