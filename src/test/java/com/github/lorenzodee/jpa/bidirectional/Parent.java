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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Parent {

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Child> children;

	// This would be ignored since it is uni-directional
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ParentItem> items;

	@OneToOne(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private ParentOneBiDirectional oneBiDirectional;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private ParentOneUniDirectional oneUniDirectional;

	public Parent() {
	}

	public Parent(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public List<Child> getChildren() {
		return this.children;
	}

	public void setChildren(List<Child> children) {
		this.children = children;
	}

	public List<ParentItem> getItems() {
		return this.items;
	}

	public void setItems(List<ParentItem> items) {
		this.items = items;
	}

	public ParentOneBiDirectional getOneBiDirectional() {
		return this.oneBiDirectional;
	}

	public void setOneBiDirectional(ParentOneBiDirectional oneBiDirectional) {
		this.oneBiDirectional = oneBiDirectional;
	}

	public ParentOneUniDirectional getOneUniDirectional() {
		return this.oneUniDirectional;
	}

	public void setOneUniDirectional(ParentOneUniDirectional oneUniDirectional) {
		this.oneUniDirectional = oneUniDirectional;
	}

}
