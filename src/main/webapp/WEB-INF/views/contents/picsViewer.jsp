<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1>Pics Viewer</h1>
<center>
<table>
	<c:forEach var="item" items="${pics}">
		<tr>
			<td>
                <c:choose>
                <c:when test="${item.video}">
                    <video preload="auto" style="min-height:500px;width: 500px;" width="500" loop muted autoplay>
                        <source src="/picviewer/pic/${item.id}" type="video/mp4">
                    </video>
                </c:when>
                <c:otherwise>
                    <img src="<c:url value='/picviewer/pic/${item.id}' />" />
                </c:otherwise>
                </c:choose>
			<br />
			${item.title}<br />
			${item.url}
			</td>
		</tr>
	</c:forEach>
</table>
</center>