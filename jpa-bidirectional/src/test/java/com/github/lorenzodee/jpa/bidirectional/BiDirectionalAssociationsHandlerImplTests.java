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

import java.util.LinkedList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BiDirectionalAssociationsHandlerImplTests {

	@Autowired
	TestEntityManager entityManager;

	BiDirectionalAssociationsHandlerImpl handler;

	@BeforeEach
	void setUp() throws Exception {
		handler = new BiDirectionalAssociationsHandlerImpl(
				entityManager.getEntityManager());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void safelyIgnoresNonEntities() throws Exception {
		handler.handleBiDirectionalAssociations(new Object());
	}

	// TODO Support non-entity types that contain entities
	// E.g. FormBean that contains List<TestEntity>

	@Nested
	class OneToManyTests {

		@Test
		void handlesBiDirectionalFieldAccess() throws Exception {
			Parent parent = new Parent();
			parent.setChildren(new LinkedList<>());

			Child child1 = new Child();
			parent.getChildren().add(child1);
			child1.setGrandChildren(new LinkedList<>());
			GrandChild grandChild11 = new GrandChild();
			child1.getGrandChildren().add(grandChild11);

			Child child2 = new Child();
			parent.getChildren().add(child2);
			child2.setGrandChildren(new LinkedList<>());
			GrandChild grandChild21 = new GrandChild();
			child2.getGrandChildren().add(grandChild21);
			GrandChild grandChild22 = new GrandChild();
			child2.getGrandChildren().add(grandChild22);

			Child child3 = new Child();
			parent.getChildren().add(child3);
			child3.setGrandChildren(new LinkedList<>());

			handler.handleBiDirectionalAssociations(parent);

			parent.getChildren().forEach((child) -> {
				assertThat(child.getParent())
					.as("Child must reference parent").isSameAs(parent);
				child.getGrandChildren().forEach(
						(grandChild) -> assertThat(
								grandChild.getChild())
						.as("GrandChild must reference child").isSameAs(child));
			});
		}

		@Test
		void ignoresUniDirectionalFieldAccess() throws Exception {
			Parent parent = new Parent();
			parent.setChildren(new LinkedList<>());
			parent.setItems(new LinkedList<>());

			ParentItem item1 = new ParentItem();
			parent.getItems().add(item1);

			handler.handleBiDirectionalAssociations(parent);

			// passes if handler succeeds
		}

		@Test
		void handlesBiDirectionalPropertyAccess() throws Exception {
			Parent2 parent = new Parent2();
			parent.setChildren(new LinkedList<>());

			Child2 child1 = new Child2();
			parent.getChildren().add(child1);
			child1.setGrandChildren(new LinkedList<>());
			GrandChild2 grandChild11 = new GrandChild2();
			child1.getGrandChildren().add(grandChild11);

			Child2 child2 = new Child2();
			parent.getChildren().add(child2);
			child2.setGrandChildren(new LinkedList<>());
			GrandChild2 grandChild21 = new GrandChild2();
			child2.getGrandChildren().add(grandChild21);
			GrandChild2 grandChild22 = new GrandChild2();
			child2.getGrandChildren().add(grandChild22);

			Child2 child3 = new Child2();
			parent.getChildren().add(child3);
			child3.setGrandChildren(new LinkedList<>());

			handler.handleBiDirectionalAssociations(parent);

			parent.getChildren().forEach((child) -> {
				assertThat(child.getParent())
					.as("Child must reference parent").isSameAs(parent);
				child.getGrandChildren().forEach(
						(grandChild) -> assertThat(
								grandChild.getChild())
						.as("GrandChild must reference child").isSameAs(child));
			});
		}

		@Test
		void ignoresUniDirectionalPropertyAccess() throws Exception {
			Parent2 parent = new Parent2();
			parent.setChildren(new LinkedList<>());
			parent.setItems(new LinkedList<>());

			ParentItem2 item1 = new ParentItem2();
			parent.getItems().add(item1);

			handler.handleBiDirectionalAssociations(parent);

			// passes if handler succeeds
		}

	}

	@Nested
	class OneToOneTests {

		@Test
		void handlesOneToOneBiDirectionalFieldAccess() throws Exception {
			Parent parent = new Parent();
			ParentOneBiDirectional oneBiDirectional = new ParentOneBiDirectional();
			parent.setOneBiDirectional(oneBiDirectional);

			handler.handleBiDirectionalAssociations(parent);

			assertThat(parent.getOneBiDirectional().getParent()).isSameAs(parent);
		}

		@Test
		void ignoresOneToOneUniDirectionalFieldAccess() throws Exception {
			Parent parent = new Parent();
			ParentOneUniDirectional oneUniDirectional = new ParentOneUniDirectional();
			parent.setOneUniDirectional(oneUniDirectional);

			handler.handleBiDirectionalAssociations(parent);

			// passes if handler succeeds
		}

		@Test
		void handlesOneToOneBiDirectionalPropertyAccess() throws Exception {
			Parent2 parent = new Parent2();
			Parent2OneBiDirectional oneBiDirectional = new Parent2OneBiDirectional();
			parent.setOneBiDirectional(oneBiDirectional);

			handler.handleBiDirectionalAssociations(parent);

			assertThat(parent.getOneBiDirectional().getParent()).isSameAs(parent);
		}

		@Test
		void ignoresOneToOneUniDirectionalPropertyAccess() throws Exception {
			Parent2 parent = new Parent2();
			Parent2OneUniDirectional oneUniDirectional = new Parent2OneUniDirectional();
			parent.setOneUniDirectional(oneUniDirectional);

			handler.handleBiDirectionalAssociations(parent);

			// passes if handler succeeds
		}

	}

}
