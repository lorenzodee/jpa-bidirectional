<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Orders - Example</title>
</head>
<body>
	<h1>Orders</h1>
	<ul>
	<c:forEach items="${ordersPage.content}" var="order">
		<spring:url value="/orders/{id}" var="showOrderUrl">
			<spring:param name="id" value="${order.id}" />
		</spring:url>
		<spring:url value="/orders/{id}/edit" var="editOrderUrl">
			<spring:param name="id" value="${order.id}" />
		</spring:url>
		<li>
			Order #<c:out value="${order.id}" /> (<c:out value="${order.itemsCount}" /> item(s))
			| <a href="${showOrderUrl}">Show</a>
			| <a href="${editOrderUrl}">Edit</a>
		</li>
	</c:forEach>
	</ul>
</body>
</html>