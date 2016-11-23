<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Shows</h1>
<table>
	<tr>
		<td colspan="5"><a href="deleteunsubscribedshows">Delete unsubscribed shows</a></td>
	</tr>
	<tr>
    	<td colspan="5"><a href="addshow">Add</a></td>
    </tr>
	<tr>
		<td>ID</td>
		<td>Name</td>
		<td>URL</td>
		<td>Actions</td>
	</tr>
	<c:forEach var="item" items="${shows}">
		<tr>
			<td>${item.id}</td>
			<td>${item.title}</td>
			<td>${item.url}</td>
			<td>
			<a href="editshow/${item.id}">Edit</a> | <a href="delshow/${item.id}">Delete</a>
			</td>
		</tr>
	</c:forEach>
</table>