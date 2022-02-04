<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<tr data-nested-fields>
	<td>
		<form:hidden path="id" />
		<form:select path="product.id" cssClass="form-control">
			<form:options items="${products}" itemLabel="label" itemValue="value" />
		</form:select>
	</td>
	<td>
		<form:input path="quantity" type="number" cssClass="form-control" />
	</td>
	<td class="align-middle">
		<button type="button" class="btn btn-warning btn-sm" data-nested-remove>Remove</button>
	</td>
</tr>
