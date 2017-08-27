<%@ page import="mysso.client.core.AuthenticationFilter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>protected page</title>
</head>
<body>
<c:set var="assertion" value="<%=request.getSession().getAttribute(AuthenticationFilter.assertionName)%>"/>
<h1>hello, this is a demo client for mysso</h1>
<p><a href="http://localhost:8080/mysso/logout">退出</a> </p>
<p>principal</p>
<p>id: ${assertion.principal.id}</p>
<p>attributes: </p>
<c:set var="attributes" value="${assertion.principal.attributes}"/>
<c:forEach var="attr" items="${attributes}">
<p>&nbsp;&nbsp;&nbsp;&nbsp;${attr.key}: ${attr.value}</p>
</c:forEach>
</body>
</html>
