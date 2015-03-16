<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Pics Viewer</h1>
<center>
<table>
	<c:forEach var="item" items="${pics}">
		<tr>
			<td>
			<img src="<c:url value='/picviewer/pic/${item.id}' />" /><br />
			${item.title}<br />
			${item.url}
			</td>
		</tr>
	</c:forEach>
</table>
</center>