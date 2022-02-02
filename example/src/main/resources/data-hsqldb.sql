INSERT INTO products (title) VALUES ('A');
INSERT INTO products (title) VALUES ('B');
INSERT INTO products (title) VALUES ('C');

SET AUTOCOMMIT FALSE;

INSERT INTO orders (order_id) VALUES (NEXT VALUE FOR order_seq);

INSERT INTO order_items (order_item_id, order_id, quantity, product_id) VALUES
	(NEXT VALUE FOR order_item_seq, CURRENT VALUE FOR order_seq, 2, 1),
	(CURRENT VALUE FOR order_item_seq + 1, CURRENT VALUE FOR order_seq, 1, 2),
	(CURRENT VALUE FOR order_item_seq + 2, CURRENT VALUE FOR order_seq, 3, 3)
	;

COMMIT;
