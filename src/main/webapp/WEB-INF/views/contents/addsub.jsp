<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Add a User's subscription</h1>
<form id="form" method="POST" action="<c:url value="/addusersub" />">
<input type="hidden" id="userId" name="userId" value="${user.id}" />
<table>
	<tr>
		<td colspan="5"><a href="<c:url value="/usersubs/${user.id}" />">Back</a></td>
	</tr>
	<tr>
		<td colspan="5">${user.username}</td>
	</tr>
	<tr>
		<td>Search a show:</td>
		<td><input type="text" id="showName" name="showName" value="${showName}" /></td>
		<td><input type="submit" value="Search" /></td>
	</tr>
<c:if test="${searchResults != null}">
	<tr>
		<td colspan="5">Search results</td>
	</tr>
	<tr>
		<td>Show name</td>
		<td>Action</td>
	</tr>
	<c:forEach var="item" items="${searchResults}">
		<tr>
			<td>${item.name}</td>
			<td><a href="<c:url value="/addselectedusersub/${user.id}/${item.name}/${item.showUpdaterId}/${item.showId}/0" />">Add (no email)</a> | 
			<a href="<c:url value="/addselectedusersub/${user.id}/${item.name}/${item.showUpdaterId}/${item.showId}/1" />">Add (with email)</a></td>
		</tr>
	</c:forEach>
</c:if>
    <tr>
		<td colspan="5">Current subscriptions</td>
	</tr>
	<tr>
		<td>Show</td>
		<td>Enabled</td>
		<td>E-mail notification</td>
	</tr>
	<c:forEach var="item" items="${subs}">
		<tr>
			<td>${item.show.title}</td>
			<td>${item.enabled}</td>
			<td>${item.emailEnabled}</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="5">Add subscription</td>
	</tr>
	<tr>
		<td colspan="2">Show</td>
		<td>Action</td>
	</tr>
	<c:forEach var="item" items="${shows}">
		<tr>
			<td colspan="2">${item.title}</td>
			<td><a href="<c:url value="/addnewsub/${user.id}/${item.id}" />">Add</a></td>
		</tr>
	</c:forEach>
</table>
</form>