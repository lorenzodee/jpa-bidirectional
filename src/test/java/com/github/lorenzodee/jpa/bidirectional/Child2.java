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

package com.github.lorenzodee.jpa.bidirectional;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Child2 {

	private Long id;
	private Parent2 parent;
	private List<GrandChild2> grandChildren;

	public Child2() {
	}

	public Child2(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public Parent2 getParent() {
		return this.parent;
	}

	public void setParent(Parent2 parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GrandChild2> getGrandChildren() {
		return this.grandChildren;
	}

	public void setGrandChildren(List<GrandChild2> grandChildren) {
		this.grandChildren = grandChildren;
	}

}
