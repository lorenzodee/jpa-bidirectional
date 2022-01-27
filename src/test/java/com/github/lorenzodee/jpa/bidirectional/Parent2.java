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
public class Parent2 {

	private Long id;
	private List<Child2> children;
	private List<ParentItem2> items;
	private Parent2OneBiDirectional oneBiDirectional;
	private Parent2OneUniDirectional oneUniDirectional;

	public Parent2() {
	}

	public Parent2(Long id) {
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

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Child2> getChildren() {
		return this.children;
	}

	public void setChildren(List<Child2> children) {
		this.children = children;
	}

	// This would be ignored since it is uni-directional
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ParentItem2> getItems() {
		return this.items;
	}

	public void setItems(List<ParentItem2> items) {
		this.items = items;
	}

	@OneToOne(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public Parent2OneBiDirectional getOneBiDirectional() {
		return this.oneBiDirectional;
	}

	public void setOneBiDirectional(Parent2OneBiDirectional oneBiDirectional) {
		this.oneBiDirectional = oneBiDirectional;
	}

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	public Parent2OneUniDirectional getOneUniDirectional() {
		return this.oneUniDirectional;
	}

	public void setOneUniDirectional(Parent2OneUniDirectional oneUniDirectional) {
		this.oneUniDirectional = oneUniDirectional;
	}

}
