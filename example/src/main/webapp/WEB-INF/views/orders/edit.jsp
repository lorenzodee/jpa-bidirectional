<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Order <c:out value="${order.id}" /> - Example</title>
</head>
<body>
	<h1>Edit Order <c:out value="${order.id}" /></h1>
	<jsp:include page="_form.jsp" />
</body>
</html>