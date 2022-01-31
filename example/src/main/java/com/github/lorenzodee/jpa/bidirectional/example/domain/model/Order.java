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

package com.github.lorenzodee.jpa.bidirectional.example.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Order aggregate root.
 *
 * @author Lorenzo Dee
 */
@Entity
@Table(name = "orders")
@SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
public class Order {

	// By default, *hide* the aggregate-root ID.
	// It's exposed via the URL and in "self" hypermedia links.
	// See https://github.com/spring-projects/spring-data-rest/issues/13
	// For non-aggregate-root entities, the ID can be exposed
	// to allow updates.

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
	@Column(name = "order_id")
	@JsonIgnore
	private Long id;

	// If ordering is needed, use @OrderColumn.
	@OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<OrderItem> items;

	// As of Spring Web MVC 3.2, non-public zero-args constructors
	// have been supported for x-www-form-urlencoded data binding.
	// Must provide a public/protected zero-args constructor.

	// Here, we provide a public zero-args constructor that is used
	// in rest/controllers and aid in unit tests.

	public Order() {
	}

	// Alternatively, the `@Id` field can be effectively final and read-only.
	// To do so, Spring MVC controllers must use direct field access to
	// support x-www-form-urlencoded data binding, and the constructor
	// must be annotated with `@JsonCreator` to allow Jackson to deserialize
	// and set the `id` field properly. Unfortunately, Jackson will _not_
	// use the designated `@JsonCreator` because there is an existing
	// zero-args constructor.

	public Order(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	/*
	public void setId(Long id) {
		this.id = id;
	}
	*/

	// TODO Can we use a aggregate-specific ID type? E.g. OrderId
	/*

	// Hide getId() and setId(Long id)
	// and replace with the getId() and setId(OrderId orderId) below.

	@Transient
	@JsonIgnore
	private transient OrderId orderId;

	public OrderId getId() {
		return this.orderId != null
				? this.orderId
				: this.orderId = OrderId.of(this.id);
	}

	public void setId(OrderId orderId) {
		this.orderId = orderId;
		if (this.orderId != null) {
			this.id = orderId.getValue();
		}
		else {
			this.id = null;
		}
	}
	*/

	public List<OrderItem> getItems() {
		// No need to return a "copy" or "defensive copy"
		return this.items;
	}

	public void setItems(List<OrderItem> items) {
		// No need to create a "copy" or "defensive copy"
		this.items = items;
	}

	public void handleBiDirectionalAssociations() {
		if (this.items != null) {
			this.items.forEach((item) -> {
				if (item != null) {
					item.setOrder(this);
				}
			});
		}
	}

}
