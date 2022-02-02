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

/**
 * Represents a label-value pair intended to be used in constructing user
 * interface elements which have a label to be displayed to the user, and a
 * corresponding value to be returned to the server. One example is the HTML
 * option element.
 *
 * @param <ID> the type of the value
 * @author Lorenzo Dee
 */
public interface LabelAndValue<ID> {

	String getLabel();

	ID getValue();

}
