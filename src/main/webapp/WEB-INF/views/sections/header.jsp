<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div>
<sec:authorize access="hasRole('ADMIN')">
<a href="<c:url value="/shows" />">Shows</a> |  
<a href="<c:url value="/email_queue" />">Email queue</a> | 
<a href="<c:url value="/users" />">Users</a> | 
</sec:authorize>
<a href="<c:url value="/profile" />">My profile</a> | 
<a href="<c:url value="/logout" />">Logout</a>
</div>