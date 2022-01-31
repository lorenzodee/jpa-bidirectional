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

package com.github.lorenzodee.jpa.bidirectional.example.web.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.lorenzodee.jpa.bidirectional.example.domain.model.Order;
import com.github.lorenzodee.jpa.bidirectional.example.domain.model.Orders;

@RestController
@RequestMapping("/api/orders")
class OrdersApiController {

	@Autowired
	Orders allOrders;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Page<Order>> index(Pageable pageable) {
		return ResponseEntity.ok(this.allOrders.findAll(pageable));
	}

	@GetMapping(path = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Order> show(@PathVariable Long id) {
		/*
		return this.allOrders.findById(id)
			.map((order) -> ResponseEntity.ok(order))
			.orElseGet(() -> ResponseEntity.notFound().build());
		*/
		return ResponseEntity.of(this.allOrders.findById(id));
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> save(@RequestBody @Valid Order order, BindingResult bindingResult,
			UriComponentsBuilder uriBuilder) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}
		// By default, all getters/setters will be used for
		// JSON de/serialization.

		// Set `@Id` field to null to ensure a new entity is inserted
		// order.setId(null);
		new DirectFieldAccessor(order).setPropertyValue("id", null);

		order = this.allOrders.save(order);
		URI location = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(path = "/{id:\\d+}", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid Order order, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		// Spring MVC will NOT deserialized the "id" path variable to order.id.
		// We have to explicitly set it.
		// Note: We ignore the "id" in the request body.
		// We do NOT expect the "id" to be in the request body.
		// The "id" should be in the URI instead.
		// order.setId(id);
		new DirectFieldAccessor(order).setPropertyValue("id", id);

		// By default, all getters/setters will be used for
		// JSON de/serialization.

		order = this.allOrders.save(order);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			this.allOrders.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		catch (EmptyResultDataAccessException ex) {
			return ResponseEntity.notFound().build();
		}
	}

}
