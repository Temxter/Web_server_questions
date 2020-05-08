<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Log in</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
<div style="text-align: center">
    <h3 align="center">Log in</h3>
    <form id="login-form" action="${pageContext.request.contextPath}/singleServlet" method="post">
        <input type="hidden" name="type" value="login">
        <label for="login" action="/auth" method="post">Login: </label>
        <input type="text" id="login" name="login"/>
        <br/>
        <label for="password">Password: </label>
        <input type="password" id="password" name="password"/>
        <br/>
        <br/>${message}
        <br/>
        <input type="submit" value="Login"/>
    </form>
</div>

</body>
</html>