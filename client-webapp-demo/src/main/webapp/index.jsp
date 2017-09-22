<%@ page import="mysso.client.core.context.Configuration" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="<%=request.getContextPath()%>"/>
<%
    final Configuration cfg = (Configuration) application.getAttribute("cfg");
%>
<c:set var="cfg" value='<%=cfg%>'/>
<c:set var="assertion" value="<%=session.getAttribute(cfg.getAssertionName())%>"/>

<html>
<head>
    <title>mysso-client-demo</title>
</head>
<body>
<h1>hello, this is a demo client for mysso</h1>
<p><a href="${cfg.serverLogoutUrl}">退出</a> </p>
<p>principal</p>
<p>id: ${assertion.principal.id}</p>
<p>attributes: </p>
<c:set var="attributes" value="${assertion.principal.attributes}"/>
<c:forEach var="attr" items="${attributes}">
    <p>&nbsp;&nbsp;&nbsp;&nbsp;${attr.key}: ${attr.value}</p>
</c:forEach>
</body>
</html>
