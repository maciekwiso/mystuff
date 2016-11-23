<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h1>Add show</h1>

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
			<td>Url</td>
			<td><form:input path="url" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Save Changes" /></td>
		</tr>
	</table>
</form:form>