# JPA Bi-Directional

Handle JPA bi-directional associations when binding HTML form submissions (`application/x-www-form-urlencoded`) in Spring Web MVC applications.

## Getting Started

Just add as a dependency, and add configurations (below) to the Spring application. See [example](example) for more details.

```java
@Configuration
public class BidirectionalAssociationsHandlerConfiguration {

	@Bean
	public BidirectionalAssociationsHandler bidirectionalAssociationsHandler(
			EntityManager entityManager) {
		return new BidirectionalAssociationsHandlerImpl(entityManager);
	}

}
```

```java
@Configuration
public class JpaModelAttributeMethodProcessorConfiguration {

	@Autowired
	public void replaceServletModelAttributeMethodProcessors(
			RequestMappingHandlerAdapter requestMappingHandlerAdapter,
			BidirectionalAssociationsHandler bidirectionalAssociationsHandler) {
		List<HandlerMethodArgumentResolver> resolvers = requestMappingHandlerAdapter.getArgumentResolvers();
		List<HandlerMethodArgumentResolver> replacedResolvers = resolvers.stream().map((resolver) -> {
			if (resolver instanceof ServletModelAttributeMethodProcessor) {
				Boolean annotationNotRequired = (Boolean) new DirectFieldAccessor(resolver)
						.getPropertyValue("annotationNotRequired");
				return new JpaModelAttributeMethodProcessor(annotationNotRequired,
						bidirectionalAssociationsHandler);
			}
			return resolver;
		}).collect(Collectors.toList());
		requestMappingHandlerAdapter.setArgumentResolvers(replacedResolvers);
	}

}
```

## Build and Running Locally

### Prerequisites

- JDK 11
- Maven 3

```
cd jpa-bidirectional
mvn install
cd ..\example
mvn spring-boot:run
```
