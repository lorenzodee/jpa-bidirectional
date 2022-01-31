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

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Repository for {@link Order} aggregate.
 * <p>
 * There shall be no repository for {@link OrderItem}.
 *
 * @author Lorenzo Dee
 */
public interface Orders extends Repository<Order, Long> {

	Page<Order> findAll(Pageable pageable);

	// Page<OrderProjection> queryAll(Pageable pageable);

	Optional<Order> findById(Long id);

	// Optional<OrderProjection> queryById(Long id);

	<S extends Order> S save(S order);

	void deleteById(Long id);

}
