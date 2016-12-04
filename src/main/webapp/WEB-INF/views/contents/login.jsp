<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<h1>Please log in</h1>
<div id="login-error">${error}</div>
<div>
<a href="<c:url value="/picviewer/groups" />">pics</a><br />
<a href="<c:url value="/showsInfoHtml/maciekwiso" />">shows dates</a>
</div>
<form action="j_spring_security_check" method="post" >

<p>
	<label for="j_username">Username</label>
	<input id="j_username" name="j_username" type="text" value='<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.authentication.principal}" escapeXml="false"/>' />
</p>

<p>
	<label for="j_password">Password</label>
	<input id="j_password" name="j_password" type="password" />
</p>

<input  type="submit" value="Login"/>								
	
</form>