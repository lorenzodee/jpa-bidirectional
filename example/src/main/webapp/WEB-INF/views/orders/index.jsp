<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Orders - Example</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/webjars/bootstrap/css/bootstrap.min.css" />" />
</head>
<body>
	<main class="container">
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
		<spring:url value="/orders/create" var="createOrderUrl" />
		<a href="${createOrderUrl}" class="btn btn-secondary">Add new order</a>
	</main>

<script src="<c:url value='/webjars/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/webjars/popper.js/umd/popper.min.js' />"></script>
<script src="<c:url value='/webjars/bootstrap/js/bootstrap.min.js' />"></script>
</body>
</html>