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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

public class JpaRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

	public JpaRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
		super(converters);
		// TODO Auto-generated constructor stub
	}

	public JpaRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
			ContentNegotiationManager manager) {
		super(converters, manager);
		// TODO Auto-generated constructor stub
	}

	public JpaRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
			List<Object> requestResponseBodyAdvice) {
		super(converters, requestResponseBodyAdvice);
		// TODO Auto-generated constructor stub
	}

	public JpaRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
			ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
		super(converters, manager, requestResponseBodyAdvice);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter,
			Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return super.readWithMessageConverters(webRequest, parameter, paramType);
	}

}
