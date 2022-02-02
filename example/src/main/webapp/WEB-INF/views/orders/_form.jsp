<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<c:choose>
	<c:when test="${not empty order.id}">
		<spring:url value="/orders/{id}" var="action">
			<spring:param name="id" value="${order.id}" />
		</spring:url>
		<c:set var="method" value="put" />
	</c:when>
	<c:otherwise>
		<spring:url value="/orders" var="action" />
		<c:set var="method" value="post" />
	</c:otherwise>
</c:choose>
<form:form method="${method}" action="${action}" modelAttribute="order">
	<table>
		<tr>
			<th>Product</th>
			<th>Quantity</th>
		</tr>
		<c:forEach items="${order.items}" var="item" varStatus="itemStatus">
		<tr>
			<td>
				<form:hidden path="items[${itemStatus.index}].id" />
				<%-- WARNING: When using direct field access, JPA lazy loading proxies will cause null values to be returned --%>
				<form:select path="items[${itemStatus.index}].product.id">
					<form:options items="${products}" itemLabel="label" itemValue="value" />
				</form:select>
			</td>
			<td>
				<form:input path="items[${itemStatus.index}].quantity" type="number" />
			</td>
		</tr>
		</c:forEach>
	</table>
	<div>
		<button>Save</button>
		<a href="/orders">Cancel</a>
	</div>
</form:form>
