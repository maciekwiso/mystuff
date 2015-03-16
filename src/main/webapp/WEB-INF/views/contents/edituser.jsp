<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h1>Edit user</h1>

<form:form commandName="user">
	<table>
		<tr>
			<td colspan="2"><a href="<c:url value="/usersubs/${user.id}" />">User's subscriptions</a></td>
		</tr>
		<tr>
			<td colspan="2"><form:errors path="*" cssStyle="color : red;" /></td>
		</tr>
		<tr>
			<td>Username</td>
			<td>${user.username}</td>
		</tr>
		<tr>
			<td>Email</td>
			<td><form:input path="email" /></td>
		</tr>
		<tr>
			<td>New password</td>
			<td><form:password path="password" /></td>
		</tr>
		<tr>
			<td>Enabled</td>
			<td><form:checkbox path="enabled" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Save Changes" /></td>
		</tr>
	</table>
</form:form>