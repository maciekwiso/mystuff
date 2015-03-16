<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<h1>this is main page after being logged in</h1>

<sec:authorize access="hasRole('ADMIN')"><h2>this is just for admins</h2></sec:authorize>

<sec:authorize access="hasRole('USER')"><h2>this is for users</h2></sec:authorize>