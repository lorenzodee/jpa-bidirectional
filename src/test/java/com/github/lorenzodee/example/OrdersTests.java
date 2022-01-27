/*
 * Copyright 2021-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lorenzodee.example;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrdersTests {

	@Autowired
	Orders allOrders;
	@Autowired
	TestEntityManager entityManager;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@Sql(statements = {
			"INSERT INTO products (product_id) VALUES (7)",
			"INSERT INTO products (product_id) VALUES (8)",
			"INSERT INTO products (product_id) VALUES (9)"
		})
	void whenPersist_handlesBiDirectionalAssociations() throws Exception {
		Product p7 = new Product(7L);
		Product p8 = new Product(8L);
		Product p9 = new Product(9L);

		Order order = new Order();
		order.setItems(new ArrayList<>(3));
		OrderItem orderItem;
		orderItem = new OrderItem();
		orderItem.setProduct(p7);
		orderItem.setQuantity(2);
		orderItem.setOrder(order);
		order.getItems().add(orderItem);
		orderItem = new OrderItem();
		orderItem.setProduct(p8);
		orderItem.setQuantity(1);
		orderItem.setOrder(order);
		order.getItems().add(orderItem);
		orderItem = new OrderItem();
		orderItem.setProduct(p9);
		orderItem.setQuantity(1);
		orderItem.setOrder(order);
		order.getItems().add(orderItem);

		final Order savedOrder = this.allOrders.save(order);
		this.entityManager.flush();

		assertThat(savedOrder.getId()).isNotNull();
		savedOrder.getItems().forEach((item) -> {
			assertThat(item.getId()).isNotNull();
			assertThat(item.getOrder()).isSameAs(savedOrder);
		});
	}

	@Test
	@Sql(statements = {
			"INSERT INTO products (product_id) VALUES (6)",
			"INSERT INTO products (product_id) VALUES (7)",
			"INSERT INTO products (product_id) VALUES (8)",
			"INSERT INTO products (product_id) VALUES (9)",
			"INSERT INTO orders (order_id) VALUES (42)",
			"INSERT INTO order_items (order_id, order_item_id, product_id, quantity) VALUES (42, 420, 7, 2)",
			"INSERT INTO order_items (order_id, order_item_id, product_id, quantity) VALUES (42, 421, 8, 1)",
			"INSERT INTO order_items (order_id, order_item_id, product_id, quantity) VALUES (42, 422, 9, 1)"
		})
	void whenMerge_handlesBiDirectionalAssociations() throws Exception {
		Product p6 = new Product(6L);
		Product p7 = new Product(7L);
		Product p8 = new Product(8L);
		Product p9 = new Product(9L);

		Order order = new Order(42L);
		order.setItems(new ArrayList<>(3));
		OrderItem orderItem;
		orderItem = new OrderItem();
		orderItem.setId(420L);
		orderItem.setProduct(p7);
		orderItem.setQuantity(2);
		orderItem.setOrder(order);
		order.getItems().add(orderItem);
		orderItem = new OrderItem();
		orderItem.setId(421L);
		orderItem.setProduct(p8);
		orderItem.setQuantity(1);
		orderItem.setOrder(order);
		order.getItems().add(orderItem);
		orderItem = new OrderItem();
		orderItem.setId(422L);
		orderItem.setProduct(p9);
		orderItem.setQuantity(1);
		orderItem.setOrder(order);
		order.getItems().add(orderItem);
		orderItem = new OrderItem();
		orderItem.setId(null); // new item
		orderItem.setProduct(p6);
		orderItem.setQuantity(1);
		orderItem.setOrder(order);
		order.getItems().add(orderItem);

		final Order savedOrder = this.allOrders.save(order);
		this.entityManager.flush();

		assertThat(savedOrder.getId()).isNotNull();
		savedOrder.getItems().forEach(
				(item) -> assertThat(item.getOrder()).isSameAs(savedOrder));
	}

}
