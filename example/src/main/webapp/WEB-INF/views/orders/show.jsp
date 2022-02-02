<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order <c:out value="${order.id}" /> - Example</title>
</head>
<body>
	<h1>Order <c:out value="${order.id}" /></h1>
	<table>
		<tr>
			<th>Product</th>
			<th>Quantity</th>
		</tr>
		<c:forEach items="${order.items}" var="item">
		<tr>
			<td><c:out value="${item.product.title}" /></td>
			<td><c:out value="${item.quantity}" /></td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>