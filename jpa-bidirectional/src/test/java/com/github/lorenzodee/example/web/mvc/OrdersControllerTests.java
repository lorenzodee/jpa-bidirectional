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

package com.github.lorenzodee.example.web.mvc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.lorenzodee.example.domain.model.Order;
import com.github.lorenzodee.example.domain.model.OrderItem;
import com.github.lorenzodee.example.domain.model.Orders;
import com.github.lorenzodee.example.web.BidirectionalAssociationsHandlerConfiguration;
import com.github.lorenzodee.example.web.JpaModelAttributeMethodProcessorConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(controllers = OrdersController.class)
@Transactional
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Import({ BidirectionalAssociationsHandlerConfiguration.class,
	JpaModelAttributeMethodProcessorConfiguration.class })
class OrdersControllerTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	Orders allOrders;

	@Captor
	ArgumentCaptor<Order> savedOrder;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void givenFormUrlEncoded_whenPostSave_thenBiDirectionalAssociationsAreHandled() throws Exception {
		given(this.allOrders.save(any())).will(returnFirstArg());

		// @formatter:off
		this.mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("items[0].product.id", "7")
					.param("items[0].quantity", "2")
					.param("items[1].product.id", "8")
					.param("items[1].quantity", "1")
					.param("items[2].product.id", "9")
					.param("items[2].quantity", "1"))
				.andExpect(redirectedUrl("/orders"));
		// @formatter:on

		then(this.allOrders).should().save(this.savedOrder.capture());

		Order order = this.savedOrder.getValue();
		assertThat(order.getItems().size()).isEqualTo(3);

		OrderItem item0 = order.getItems().get(0);
		assertThat(item0.getOrder()).as("Child item must point to parent order").isSameAs(order);
		assertThat(item0.getProduct().getId()).isEqualTo(7);
		assertThat(item0.getQuantity()).isEqualTo(2);

		OrderItem item1 = order.getItems().get(1);
		assertThat(item1.getOrder()).as("Child item must point to parent order").isSameAs(order);
		assertThat(item1.getProduct().getId()).isEqualTo(8);
		assertThat(item1.getQuantity()).isEqualTo(1);

		OrderItem item2 = order.getItems().get(2);
		assertThat(item2.getOrder()).as("Child item must point to parent order").isSameAs(order);
		assertThat(item2.getProduct().getId()).isEqualTo(9);
		assertThat(item2.getQuantity()).isEqualTo(1);
	}

	@Test
	void givenFormUrlEncoded_whenPutUpdate_thenBiDirectionalAssociationsAreHandled() throws Exception {
		given(this.allOrders.save(any())).will(returnFirstArg());

		// @formatter:off
		this.mockMvc
				.perform(put("/orders/{id}", 42).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("items[0].id", "420")
					.param("items[0].product.id", "7")
					.param("items[0].quantity", "2")
					.param("items[1].id", "421")
					.param("items[1].product.id", "8")
					.param("items[1].quantity", "1")
					// .param("items[2].id", "") // new item
					.param("items[2].product.id", "9")
					.param("items[2].quantity", "1"))
				.andExpect(redirectedUrl("/orders"));
		// @formatter:on

		then(this.allOrders).should().save(this.savedOrder.capture());

		Order order = this.savedOrder.getValue();
		assertThat(order.getId()).isEqualTo(42);
		assertThat(order.getItems().size()).isEqualTo(3);

		OrderItem item0 = order.getItems().get(0);
		assertThat(item0.getId()).isEqualTo(420);
		assertThat(item0.getOrder()).as("Child item must point to parent order").isSameAs(order);
		assertThat(item0.getProduct().getId()).isEqualTo(7);
		assertThat(item0.getQuantity()).isEqualTo(2);

		OrderItem item1 = order.getItems().get(1);
		assertThat(item1.getId()).isEqualTo(421);
		assertThat(item1.getOrder()).as("Child item must point to parent order").isSameAs(order);
		assertThat(item1.getProduct().getId()).isEqualTo(8);
		assertThat(item1.getQuantity()).isEqualTo(1);

		OrderItem item2 = order.getItems().get(2);
		assertThat(item2.getId()).isNull();
		assertThat(item2.getOrder()).as("Child item must point to parent order").isSameAs(order);
		assertThat(item2.getProduct().getId()).isEqualTo(9);
		assertThat(item2.getQuantity()).isEqualTo(1);
	}

	@SuppressWarnings("unchecked")
	<T> Answer<T> returnFirstArg() {
		return (Answer<T>) new ReturnsArgumentAt(0);
	}

}
