<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style type="text/css">
	a:link {
		color: #FFFFFF;
	}

	/* visited link */
	a:visited {
		color: #FFFFFF;
	}

	/* mouse over link */
	a:hover {
		color: #FFFFFF;
	}

	/* selected link */
	a:active {
		color: #FFFFFF;
	}
</style>
<script>
function selectNext(fromNextValue) {
var inputs = document.getElementsByTagName("input"); //or document.forms[0].elements;
var foundTheValue = false;
for (var i = 0; i < inputs.length; i++) {
  if (inputs[i].type == "checkbox") {
    if (inputs[i].value != '9gag' && foundTheValue)
    	inputs[i].checked = true;
    if (inputs[i].value == fromNextValue) {
		foundTheValue = true;
    }
  }
}
}
</script>
<h1>Pics Viewer Groups</h1>
<form method="post" action="<c:url value="/picviewer/setasseen" />">
<center>
<table>
        <tr>
			<td>
                <a href="<c:url value="/picviewer/showunseen?group=youtube" />">youtube</a>
			</td>
		</tr>
	<c:forEach var="item" items="${groups}">
		<tr>
			<td>
                <a href="<c:url value="/picviewer/showunseen?group=${item}" />">${item}</a> |
				<a href="<c:url value="/picviewer/setasseen?group=${item}" />">set as seen</a> | <input type="checkbox" name="seen" value="${item}"/> | <a href="javascript:;" onClick="selectNext('${item}')">next as seen</a>
				| <a href="<c:url value="/picviewer/moveUp?group=${item}" />">move up</a>
			</td>
		</tr>
	</c:forEach>
</table>
</center>
<input type="submit" value="Set selected as seen" />
</form>