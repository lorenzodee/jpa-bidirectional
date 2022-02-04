<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<c:choose>
	<c:when test="${not empty order.id}">
		<spring:url value="/orders/{id}" var="action">
			<spring:param name="id" value="${order.id}" />
		</spring:url>
		<c:set value="put" var="method" />
		<c:set value="edit" var="idPrefix" />
	</c:when>
	<c:otherwise>
		<spring:url value="/orders" var="action" />
		<c:set value="post" var="method" />
		<c:set value="create" var="idPrefix" />
	</c:otherwise>
</c:choose>
<form:form id="${idPrefix}_order" method="${method}" action="${action}" modelAttribute="order" data-nested-reindex="1">
	<%-- jsp:include page="_formfields_order.jsp" / --%>
	<table class="table table-borderless table-sm">
		<colgroup>
			<col><!-- occupy remaining space -->
			<col style="width: 25%">
			<col style="width: 25%">
		</colgroup>
		<tr>
			<th scope="col">Product</th>
			<th scope="col">Quantity</th>
			<th scope="col" class="align-middle">
				<span class="sr-only">Actions</span>
				<button type="button" class="btn btn-secondary btn-sm"
					data-nested-add="items"
					data-nested-next-index="${order.items.size()}"
					data-nested-template-id="new_nestedOrderItem"
					data-nested-template-content-selector="tr"
					data-nested-insert="#${idPrefix}_order table tr:last-child"
					data-nested-insert-method="after">Add</button>
			</th>
		</tr>
		<c:forEach items="${order.items}" var="item" varStatus="itemStatus">
			<spring:nestedPath path="items[${itemStatus.index}]">
				<jsp:include page="_formfields_orderItem.jsp" />
			</spring:nestedPath>
		</c:forEach>
	</table>
	<div class="form-group">
		<button class="btn btn-success">Save</button>
		<a href="/orders" class="btn btn-secondary">Cancel</a>
	</div>
</form:form>
