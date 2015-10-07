<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Pics Viewer Groups</h1>
<center>
<table>
	<c:forEach var="item" items="${groups}">
		<tr>
			<td>
                <a href="<c:url value="/picviewer/showunseen?group=${item}" />">${item}</a> |
				<a href="<c:url value="/picviewer/setasseen?group=${item}" />">set as seen</a>
			</td>
		</tr>
	</c:forEach>
</table>
</center>