<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h1>Edit Show</h1>

<form:form commandName="show">
	<table>
		<tr>
			<td colspan="2"><form:errors path="*" cssStyle="color : red;" /></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><form:input path="title" /></td>
		</tr>
		<tr>
			<td>URL</td>
			<td><form:input path="url" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Save Changes" /></td>
		</tr>
	</table>
</form:form>

<table>
	<tr>
		<td>Number</td>
		<td>Title</td>
		<td>Air date</td>
	</tr>
	<c:forEach var="item" items="${eps}">
		<tr>
			<td>${item.seasonAndNumber}</td>
			<td>${item.title}</td>
			<td>${item.airdateFormatted}</td>
		</tr>
	</c:forEach>
</table>