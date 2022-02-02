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

package com.github.lorenzodee.jpa.bidirectional.web.servlet.mvc;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.github.lorenzodee.jpa.bidirectional.BidirectionalAssociationsHandler;

/**
 * A JPA-specific {@link ServletModelAttributeMethodProcessor} that handles
 * bi-directional <code>OneTo*</code> associations when resolving arguments (as
 * a {@link org.springframework.web.method.support.HandlerMethodArgumentResolver
 * HandlerMethodArgumentResolver}).
 *
 * @author Lorenzo Dee
 * @see BidirectionalAssociationsHandler
 */
public class JpaModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

	// Could not delegate to a ServletModelAttributeMethodProcessor because
	// bindRequestParameters() needs to be overridden.

	private final BidirectionalAssociationsHandler bidirectionalAssociationsHandler;

	public JpaModelAttributeMethodProcessor(
			boolean annotationNotRequired,
			BidirectionalAssociationsHandler bidirectionalAssociationsHandler) {
		super(annotationNotRequired);
		this.bidirectionalAssociationsHandler = bidirectionalAssociationsHandler;
	}

	/**
	 * This implementation handles bi-directional <code>OneTo*</code> associations
	 * after binding, and before validation.
	 */
	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		super.bindRequestParameters(binder, request);
		Object entity = binder.getTarget();
		if (entity != null) {
			this.bidirectionalAssociationsHandler
					.handleBidirectionalAssociations(entity);
		}
	}

}
