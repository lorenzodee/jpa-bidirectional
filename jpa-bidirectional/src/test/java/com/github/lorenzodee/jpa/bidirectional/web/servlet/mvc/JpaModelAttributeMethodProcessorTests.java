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

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import com.github.lorenzodee.jpa.bidirectional.BidirectionalAssociationsHandler;
import com.github.lorenzodee.jpa.bidirectional.Parent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class JpaModelAttributeMethodProcessorTests {

	JpaModelAttributeMethodProcessor processor;

	@Mock
	BidirectionalAssociationsHandler bidirectionalAssociationsHandler;
	MethodParameter parameter;
	MethodParameter optionalParameter;
	ModelAndViewContainer mavContainer;
	MockHttpServletRequest request;
	NativeWebRequest webRequest;
	WebDataBinderFactory binderFactory;

	@BeforeEach
	void setUp() throws Exception {
		Method method = getClass().getDeclaredMethod("modelAttribute",
				Parent.class, Optional.class);
		parameter = new MethodParameter(method, 0);
		optionalParameter = new MethodParameter(method, 1);

		mavContainer = new ModelAndViewContainer();
		request = new MockHttpServletRequest();
		webRequest = new ServletWebRequest(request);

		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		initializer.setConversionService(new DefaultConversionService());
		binderFactory = new ServletRequestDataBinderFactory(null, initializer);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void passesAnnotationNotRequiredParameterToSuperclass() {
		processor = new JpaModelAttributeMethodProcessor(
				true, bidirectionalAssociationsHandler);
		assertThat((boolean) ReflectionTestUtils.getField(
				processor, "annotationNotRequired")).isTrue();

		processor = new JpaModelAttributeMethodProcessor(
				false, bidirectionalAssociationsHandler);
		assertThat((boolean) ReflectionTestUtils.getField(
				processor, "annotationNotRequired")).isFalse();
	}

	@Test
	void delegatesToBiDirectionalAssociationsHandler() throws Exception {
		processor = new JpaModelAttributeMethodProcessor(
				true, bidirectionalAssociationsHandler);

		Object object1 = processor.resolveArgument(
				parameter, mavContainer, webRequest, binderFactory);

		then(bidirectionalAssociationsHandler).should()
				.handleBidirectionalAssociations(same(object1));

		reset(bidirectionalAssociationsHandler);

		Object object2 = processor.resolveArgument(
				optionalParameter, mavContainer, webRequest, binderFactory);

		then(bidirectionalAssociationsHandler).should()
				.handleBidirectionalAssociations(same(((Optional<?>) object2).get()));
	}

	@SuppressWarnings("unused")
	private void modelAttribute(@ModelAttribute Parent parent,
			@ModelAttribute Optional<Parent> optionalParent) {
	}

}
