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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

/**
 * This {@link BiDirectionalAssociationsHandler} implementation uses JPA
 * {@link Metamodel} to scan for bi-directional <code>OneTo*</code> associations
 * and uses Spring {@link ReflectionUtils} to set the back reference.
 *
 * @author Lorenzo Dee
 */
public class BiDirectionalAssociationsHandlerImpl implements BiDirectionalAssociationsHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final EntityManager entityManager;
	private final Map<Class<?>, List<Attribute<?, ?>>> managedAttributesByManagedClass;

	public BiDirectionalAssociationsHandlerImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
		this.managedAttributesByManagedClass = new ConcurrentHashMap<>();
	}

	@Override
	public void handleBiDirectionalAssociations(Object entity) {
		Class<? extends Object> entityClass = entity.getClass();
		List<Attribute<?, ?>> managedAttributes = this.managedAttributesByManagedClass.get(entityClass);
		if (managedAttributes == null) {
			managedAttributes = getManagedAttributes(entityClass);
			this.managedAttributesByManagedClass.put(entityClass, managedAttributes);
		}
		List<Object> nestedEntities = new LinkedList<>();
		for (Attribute<?, ?> attributeObject : managedAttributes) {
			Attribute<?, ?> attribute = (Attribute<?, ?>) attributeObject;
			this.logger.debug("Managing bi-directional attribute: {}", attribute);
			Annotation associationAnnotation;
			String mappedBy;
			switch (attribute.getPersistentAttributeType()) {
			case ONE_TO_MANY:
				// Handle bi-directional OneToMany
				associationAnnotation = getAssociationAnnotation(attribute);
				mappedBy = getMappedBy(associationAnnotation);
				handleBiDirectionalOneToManyAssociation(attribute, mappedBy, entity, nestedEntities);
				break;
			case ONE_TO_ONE:
				// Handle bi-directional OneToOne
				associationAnnotation = getAssociationAnnotation(attribute);
				mappedBy = getMappedBy(associationAnnotation);
				handleBiDirectionalOneToOneAssociation(attribute, mappedBy, entity, nestedEntities);
				break;
			case EMBEDDED:
				// TODO Support embeddable types
				// These can contain bi-directional OneTo* associations
			case ELEMENT_COLLECTION:
				// TODO Support element collection types
				// The collection can contain embeddable elements which in turn contain
				// bi-directional OneTo* associations
			case MANY_TO_ONE:
				// Nothing to do with ManyToOne attributes
				// The resulting model may need to replace the FKs with a proxy
				// from EntityManager.getReference(FK).
			case BASIC:
				// Nothing to do with Basic attributes
			case MANY_TO_MANY:
				// Nothing to do with ManyToMany attributes
			default:
				break;
			}
		}
		// Handle nested entities (if any)
		for (Object nestedEntity : nestedEntities) {
			handleBiDirectionalAssociations(nestedEntity);
		}
	}

	private List<Attribute<?, ?>> getManagedAttributes(Class<?> clazz) {
		this.logger.debug("Analyzing {} for managed attributes", clazz);
		List<Attribute<?, ?>> managedAttributes = new LinkedList<>();
		EntityType<?> entityType = getEntityType(clazz);
		if (entityType != null) {
			Set<?> attributeObjects = entityType.getAttributes();
			for (Object attributeObject : attributeObjects) {
				Attribute<?, ?> attribute = (Attribute<?, ?>) attributeObject;
				switch (attribute.getPersistentAttributeType()) {
				case ONE_TO_MANY:
				case ONE_TO_ONE:
					if (isBiDirectionalAssociation(attribute)) {
						managedAttributes.add(attribute); // bi-directional OneTo* associations
					}
					break;
				case EMBEDDED:
					// TODO Support embeddable types
					// These can contain bi-directional OneTo* and ManyToOne associations
				case ELEMENT_COLLECTION:
					// TODO Support element collection types
					// These can contain embeddable elements which in turn contain
					// bi-directional OneTo* and ManyToOne associations
				case MANY_TO_ONE:
					// Nothing to do with ManyToOne attributes
				case BASIC:
					// Nothing to do with Basic attributes
				case MANY_TO_MANY:
					// Nothing to do with ManyToMany attributes
				default:
					break;
				}
			}
		}
		return managedAttributes;
	}

	private EntityType<?> getEntityType(Class<?> clazz) {
		Set<EntityType<?>> entities = this.entityManager.getMetamodel().getEntities();
		for (EntityType<?> entityType : entities) {
			Class<?> entityClass = entityType.getJavaType();
			if (entityClass.equals(clazz)) {
				return entityType;
			}
		}
		return null;
	}

	private boolean isBiDirectionalAssociation(Attribute<?, ?> attribute) {
		if (attribute.isAssociation()) {
			Annotation associationAnnotation = getAssociationAnnotation(attribute);
			String mappedBy = getMappedBy(associationAnnotation);
			return mappedBy != null;
		}
		return false;
	}

	private Annotation getAssociationAnnotation(Attribute<?, ?> attribute) {
		Annotation[] annotations = null;
		Member member = attribute.getJavaMember();
		if (member instanceof Field) {
			annotations = ((Field) member).getAnnotations();
		}
		else if (member instanceof Method) {
			annotations = ((Method) member).getAnnotations();
		}
		for (Annotation annotation : annotations) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (OneToMany.class.equals(annotationType)
					|| OneToOne.class.equals(annotationType)
					|| ManyToMany.class.equals(annotationType)
					|| ManyToOne.class.equals(annotationType)) {
				return annotation;
			}
		}
		return null;
	}

	private String getMappedBy(Annotation annotation) {
		String mappedBy = null;
		Class<? extends Annotation> annotationType = annotation.annotationType();
		if (OneToMany.class.equals(annotationType)) {
			mappedBy = ((OneToMany) annotation).mappedBy();
		}
		else if (OneToOne.class.equals(annotationType)) {
			mappedBy = ((OneToOne) annotation).mappedBy();
		}
		else if (ManyToMany.class.equals(annotationType)) {
			mappedBy = ((ManyToMany) annotation).mappedBy();
		}
		// ManyToOne does not have a mappedBy attribute
		return "".equals(mappedBy) ? null : mappedBy;
	}

	private void handleBiDirectionalOneToManyAssociation(
			Attribute<?, ?> attribute, String mappedBy, Object entity, List<Object> nestedEntities) {
		// Read the field/property value of entity attribute
		Object collectionOrMap = readAttributeValue(attribute, entity);
		if (collectionOrMap != null) {
			EntityType<?> targetEntityType = getTargetEntityType(attribute);
			Attribute<?, ?> targetEntityTypeAttribute = targetEntityType.getAttribute(mappedBy);
			if (collectionOrMap instanceof Collection) {
				Collection<?> collection = (Collection<?>) collectionOrMap;
				for (Object targetEntity : collection) {
					// Write the field/property value of target entity attribute
					writeAttributeValue(targetEntityTypeAttribute, targetEntity, entity);
					// Add targetEntity for further/recursive processing
					nestedEntities.add(targetEntity);
				}
			}
			else if (collectionOrMap instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) collectionOrMap;
				for (Object targetEntity : map.values()) {
					writeAttributeValue(targetEntityTypeAttribute, targetEntity, entity);
					// Add targetEntity for further/recursive processing
					nestedEntities.add(targetEntity);
				}
			}
		}
	}

	private void handleBiDirectionalOneToOneAssociation(Attribute<?, ?> attribute, String mappedBy, Object entity,
			List<Object> nestedEntities) {
		// Read the field/property value of entity attribute
		Object targetEntity = readAttributeValue(attribute, entity);
		if (targetEntity != null) {
			EntityType<?> targetEntityType = getTargetEntityType(attribute);
			Attribute<?, ?> targetEntityTypeAttribute = targetEntityType.getAttribute(mappedBy);
			// Write the field/property value of target entity attribute
			writeAttributeValue(targetEntityTypeAttribute, targetEntity, entity);
			// Add targetEntity for further/recursive processing
			nestedEntities.add(targetEntity);
		}
	}

	/**
	 * Returns the target entity type of the given association attribute.
	 * <p>
	 * Given the <code>items</code> attribute (see below), this returns an
	 * <code>EntityType&lt;OrderItem&gt;</code> object.
	 *
	 * <pre>
	 * &#064;Entity
	 * public class Order {
	 * 	&#064;OneToMany(mappedBy = "order")
	 * 	private List&lt;OrderItem&gt; items;
	 * 	// &#064;OneToMany(mappedBy = "order", targetEntity = OrderItem.class)
	 * 	// private List items;
	 * }
	 * </pre>
	 * <pre>
	 * &#064;Entity
	 * public class OrderItem {
	 * 	&#064;ManyToOne
	 * 	private Order order;
	 * }
	 * </pre>
	 * </p>
	 *
	 * <p>
	 * Given the <code>address</code> attribute (see below), this returns an
	 * <code>EntityType&lt;PostalAddress&gt;</code> object.
	 * <pre>
	 * &#064;Entity
	 * public class Person {
	 * 	&#064;OneToOne(mappedBy = "person")
	 * 	private PostalAddress address;
	 * }
	 * </pre>
	 * <pre>
	 * &#064;Entity
	 * public class PostalAddress {
	 * 	&#064;OneToOne
	 * 	private Person person;
	 * }
	 * </pre>
	 * </p>
	 * @param attribute the given association attribute
	 * @return the target entity type of the given association attribute
	 */
	private EntityType<?> getTargetEntityType(Attribute<?, ?> attribute) {
		Class<?> targetEntityClass = null;

		if (attribute.isAssociation()) {
			if (attribute instanceof SingularAttribute) {
				SingularAttribute<?, ?> singularAttribute = (SingularAttribute<?, ?>) attribute;
				targetEntityClass = singularAttribute.getType().getJavaType();
			}
			else if (attribute instanceof PluralAttribute) {
				PluralAttribute<?, ?, ?> pluralAttribute = (PluralAttribute<?, ?, ?>) attribute;
				targetEntityClass = pluralAttribute.getElementType().getJavaType();
			}
		}
		return (targetEntityClass != null) ? getEntityType(targetEntityClass) : null;
	}

	private Object readAttributeValue(Attribute<?, ?> targetEntityTypeAttribute, Object targetEntity) {
		Member member = targetEntityTypeAttribute.getJavaMember();
		if (member instanceof Field) {
			Field fieldMember = (Field) member;
			ReflectionUtils.makeAccessible(fieldMember);
			return ReflectionUtils.getField(fieldMember, targetEntity);
		}
		else if (member instanceof Method) {
			Method methodMember = (Method) member;
			PropertyDescriptor propertyForMethod = BeanUtils.findPropertyForMethod(methodMember);
			Method readMethod = propertyForMethod.getReadMethod();
			ReflectionUtils.makeAccessible(readMethod);
			return ReflectionUtils.invokeMethod(readMethod, targetEntity);
		}
		return null;
	}

	private void writeAttributeValue(Attribute<?, ?> targetEntityTypeAttribute, Object targetEntity, Object value) {
		Member member = targetEntityTypeAttribute.getJavaMember();
		if (member instanceof Field) {
			Field fieldMember = (Field) member;
			// Set targetEntityTypeAttribute using direct field access
			ReflectionUtils.makeAccessible(fieldMember);
			ReflectionUtils.setField(fieldMember, targetEntity, value);
		}
		else if (member instanceof Method) {
			Method methodMember = (Method) member;
			// Set targetEntityTypeAttribute using setter method
			PropertyDescriptor propertyForMethod = BeanUtils.findPropertyForMethod(methodMember);
			Method writeMethod = propertyForMethod.getWriteMethod();
			ReflectionUtils.makeAccessible(writeMethod);
			ReflectionUtils.invokeMethod(writeMethod, targetEntity, value);
		}
	}

}
