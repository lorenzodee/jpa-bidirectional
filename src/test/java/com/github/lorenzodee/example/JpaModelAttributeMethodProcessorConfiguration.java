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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.lorenzodee.jpa.bidirectional.BiDirectionalAssociationsHandlerImpl;
import com.github.lorenzodee.jpa.bidirectional.web.servlet.mvc.JpaModelAttributeMethodProcessor;

@Configuration
public class JpaModelAttributeMethodProcessorConfiguration implements WebMvcConfigurer {

	// TODO Use custom RequestMappingHandlerAdapter to replace built-in
	// ServletModelAttributeMethodProcessor(s)
	// That way, parameters annotated with @ModelAttribute will still work. And
	// after the ServletModelAttributeMethodProcessor is done binding,
	// if the argument is a JPA entity, then this can manage some associations
	// before validation occurs (and make it ready for persistence).

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		boolean annotationNotRequired = true;
		resolvers.add(new JpaModelAttributeMethodProcessor(annotationNotRequired,
				new BiDirectionalAssociationsHandlerImpl(this.entityManager)));
	}

}
