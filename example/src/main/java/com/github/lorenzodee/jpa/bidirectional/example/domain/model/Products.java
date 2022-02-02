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
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * Repository for {@link Product} aggregate.
 * <p>
 *
 * @author Lorenzo Dee
 */
public interface Products extends Repository<Product, Long>, LabelAndValueOptionsProvider<Long> {

	// Page<ProductProjection> queryAll(Pageable pageable);

	Optional<Product> findById(Long id);

	// Optional<ProductProjection> queryById(Long id);

	<S extends Product> S save(S product);

	void deleteById(Long id);

	@Query
	List<LabelAndValue<Long>> provideOptions();

}
