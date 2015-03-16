<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TV Show Informer</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/styles.css" />" />
</head>
<body>
<div><img src="<c:url value="/resources/images/banner.jpg" />" /> </div>
<div id="header-style"><tiles:insertAttribute name="header-content" /></div>
<div id="primary-style"><tiles:insertAttribute name="primary-content" /></div>
<div id="footer-style"><tiles:insertAttribute name="footer-content" /></div>

</body>
</html>