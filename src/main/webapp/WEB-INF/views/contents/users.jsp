<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Users</h1>
<table>
	<tr>
		<td colspan="5"><a href="adduser">Add</a></td>
	</tr>
	<tr>
		<td>ID</td>
		<td>Name</td>
		<td>E-mail</td>
		<td>Actions</td>
	</tr>
	<c:forEach var="item" items="${users}">
		<tr>
			<td>${item.id}</td>
			<td>${item.username}</td>
			<td>${item.email} | ${item.password}</td>
			<td>
			<a href="edituser/${item.id}">Edit</a>
			</td>
		</tr>
	</c:forEach>
</table>