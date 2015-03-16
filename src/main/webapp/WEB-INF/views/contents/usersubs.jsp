<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript">

function showInfo() {
	var link = "<c:url value="/usersubs/${user.id}/" />" + document.getElementById("days").value;
	window.location.href = link;
}
</script>

<h1>User's subscriptions</h1>
<table>
	<tr>
		<td colspan="5"><a href="<c:url value="/addusersub/${user.id}" />">Add</a></td>
	</tr>
	<tr>
		<td colspan="5">${user.username}</td>
	</tr>
	<tr>
		<td>Show</td>
		<td>Enabled</td>
		<td>E-mail notification</td>
		<td>Actions</td>
	</tr>
	<c:forEach var="item" items="${subs}">
		<tr>
			<td>${item.show.title}</td>
			<td>${item.enabled}</td>
			<td>${item.emailEnabled}</td>
			<td>
			<a href="<c:url value="/delusersub/${user.id}/${item.id}" />">Delete</a>
			</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="5">Subscription info</td>
	</tr>
	<tr>
		<td colspan="5">
		<form>
			Days: <input type="text" id="days" value="${days}" /> <a href="javascript:showInfo();">Show</a><br />
			${usersubinfo}
		</form>
		</td>
	</tr>
</table>