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

package com.github.lorenzodee.jpa.bidirectional.example;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.github.lorenzodee.jpa.bidirectional.BiDirectionalAssociationsHandler;
import com.github.lorenzodee.jpa.bidirectional.web.servlet.mvc.JpaModelAttributeMethodProcessor;

@Configuration
public class JpaModelAttributeMethodProcessorConfiguration {

	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	@Autowired
	private BiDirectionalAssociationsHandler bidirectionalAssociationsHandler;

	@Autowired
	public void replaceServletModelAttributeMethodProcessors() {
		List<HandlerMethodArgumentResolver> resolvers = this.requestMappingHandlerAdapter.getArgumentResolvers();
		List<HandlerMethodArgumentResolver> replacedResolvers = resolvers.stream().map((resolver) -> {
			if (resolver instanceof ServletModelAttributeMethodProcessor) {
				Boolean annotationNotRequired = (Boolean) new DirectFieldAccessor(resolver)
						.getPropertyValue("annotationNotRequired");
				return new JpaModelAttributeMethodProcessor(annotationNotRequired,
						this.bidirectionalAssociationsHandler);
			}
			return resolver;
		}).collect(Collectors.toList());
		this.requestMappingHandlerAdapter.setArgumentResolvers(replacedResolvers);
	}

}