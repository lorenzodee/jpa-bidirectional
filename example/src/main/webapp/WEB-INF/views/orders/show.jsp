<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Order <c:out value="${order.id}" /> - Example</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/webjars/bootstrap/css/bootstrap.min.css" />" />
</head>
<body>
	<main class="container">
		<h1>Order <c:out value="${order.id}" /></h1>
		<table class="table">
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
	</main>

<script src="<c:url value='/webjars/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/webjars/popper.js/umd/popper.min.js' />"></script>
<script src="<c:url value='/webjars/bootstrap/js/bootstrap.min.js' />"></script>
</body>
</html>