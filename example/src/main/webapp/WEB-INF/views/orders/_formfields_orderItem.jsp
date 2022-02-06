<%@ include file="/WEB-INF/views/_pagedirectives.jspf" %>
<tr data-nested-fields>
	<td>
		<form:hidden path="id" />
		<form:select path="product.id" cssClass="form-control" cssErrorClass="form-control is-invalid" required="1">
			<form:option value="">- Select one -</form:option>
			<form:options items="${productOptions}" itemLabel="label" itemValue="value" />
		</form:select>
	</td>
	<td>
		<form:input path="quantity" type="number" cssClass="form-control" cssErrorClass="form-control is-invalid" required="1" min="1" />
	</td>
	<td class="align-middle">
		<button type="button" class="btn btn-warning btn-sm" data-nested-remove>Remove</button>
	</td>
</tr>
