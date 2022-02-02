DROP TABLE IF EXISTS order_items CASCADE 
DROP TABLE IF EXISTS orders CASCADE 
DROP TABLE IF EXISTS products CASCADE 
    
DROP SEQUENCE order_item_seq IF EXISTS;
DROP SEQUENCE order_seq IF EXISTS;
DROP SEQUENCE product_seq IF EXISTS;

CREATE SEQUENCE order_item_seq start with 1 increment by 5;
CREATE SEQUENCE order_seq start with 1 increment by 1;
CREATE SEQUENCE product_seq start with 1 increment by 1;

CREATE TABLE order_items (
    order_item_id bigint GENERATED BY DEFAULT AS SEQUENCE order_item_seq,
    quantity integer not null,
    order_id bigint not null,
    product_id bigint not null,
    PRIMARY KEY (order_item_id)
);
CREATE TABLE orders (
    order_id bigint GENERATED BY DEFAULT AS SEQUENCE order_seq,
    PRIMARY KEY (order_id)
);
CREATE TABLE products (
    product_id bigint GENERATED BY DEFAULT AS SEQUENCE product_seq, 
    title varchar(255),
    PRIMARY KEY (product_id)
);
-- HSQLDB does not support deferring constraint enforcement
-- ALTER TABLE order_items
--    ADD CONSTRAINT uk_order_items_1 UNIQUE (order_id, product_id);
ALTER TABLE order_items
   ADD CONSTRAINT fk_order_items_orders
   FOREIGN KEY (order_id)
   REFERENCES orders;
ALTER TABLE order_items
   ADD CONSTRAINT fk_order_items_products
   FOREIGN KEY (product_id)
   REFERENCES products;
