<%@ page import="mysso.client.core.context.Configuration" %>
<%@ page import="mysso.client.core.context.BeansContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="<%=request.getContextPath()%>"/>
<c:set var="cfg" value="<%=BeansContext.getInstance().getBean(Configuration.class)%>"/>
<c:set var="assertion" value="<%=request.getSession().getAttribute(BeansContext.getInstance().getBean(Configuration.class).getAssertionName())%>"/>

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
