--liquibase formatted sql

--changeset erp-inventory:001-create-inventory-items
CREATE TABLE inventory_items
(
    id            BIGSERIAL      NOT NULL,
    item_code     VARCHAR(20)    NOT NULL,
    name          VARCHAR(200)   NOT NULL,
    sku           VARCHAR(50)    NOT NULL,
    category      VARCHAR(100)   NOT NULL,
    quantity      INTEGER        NOT NULL DEFAULT 0,
    reorder_level INTEGER        NOT NULL DEFAULT 0,
    unit_cost     DECIMAL(15, 2) NOT NULL DEFAULT 0,
    location      VARCHAR(100)   NOT NULL,
    status        VARCHAR(20)    NOT NULL DEFAULT 'IN_STOCK',
    CONSTRAINT pk_inventory_items  PRIMARY KEY (id),
    CONSTRAINT uq_inventory_sku    UNIQUE (sku),
    CONSTRAINT uq_inventory_code   UNIQUE (item_code),
    CONSTRAINT chk_qty_non_negative    CHECK (quantity >= 0),
    CONSTRAINT chk_reorder_non_negative CHECK (reorder_level >= 0),
    CONSTRAINT chk_cost_non_negative   CHECK (unit_cost >= 0)
);

CREATE INDEX idx_inventory_category ON inventory_items (category);
CREATE INDEX idx_inventory_status   ON inventory_items (status);

--rollback DROP INDEX idx_inventory_status;
--rollback DROP INDEX idx_inventory_category;
--rollback DROP TABLE inventory_items;

