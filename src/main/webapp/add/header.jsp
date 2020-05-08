<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Main page</title>
    <style type="text/css">
        form {
            display:inline;
            margin:0px;
            padding:0px;
        }
    </style>
</head>
<body>
<div style="text-align: center">
    <h2>Testing system</h2>
    <a href="${pageContext.request.contextPath}">Info</a> |
    <a href="${pageContext.request.contextPath}">Home</a> |
    <form id="login-form" action="${pageContext.request.contextPath}/singleServlet" method="post">
    <input type="hidden" name="type" value="logout">
    <input type="submit" value="Logout"/>
    </form>
    <p align="right">Hello, ${user != null ? user.getLogin() : "guest"}!</p>
    ${message}
</div>
</body>
</html>