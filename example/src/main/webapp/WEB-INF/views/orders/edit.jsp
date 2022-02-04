<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Edit Order <c:out value="${order.id}" /> - Example</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/webjars/bootstrap/css/bootstrap.min.css" />" />
</head>
<body>
	<main class="container">
		<h1>Edit Order <c:out value="${order.id}" /></h1>
		<jsp:include page="_form.jsp" />
	</main>
	<template id="new_nestedOrderItem">
		<form:form id="new_orderItem" modelAttribute="newOrderItem">
			<table>
				<jsp:include page="_formfields_orderItem.jsp" />
			</table>
		</form:form>
	</template>

<script src="<c:url value='/webjars/jquery/jquery.min.js' />"></script>
<script src="<c:url value='/webjars/popper.js/umd/popper.min.js' />"></script>
<script src="<c:url value='/webjars/bootstrap/js/bootstrap.min.js' />"></script>
<script src="<c:url value='/js/nested.js' />"></script>
</body>
</html>