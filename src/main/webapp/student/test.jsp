<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Test</title>
</head>
<body>

<jsp:include page="/add/header.jsp"/>

<div align="center">
<h3>Test: "${currentTest.name}"</h3>
<form id="test-form" method="post" action="${pageContext.request.contextPath}/singleServlet">
    <input type="hidden" name="type" value="test">
    <p>${questionNumber}. ${currentQuestion.question} </p>
    <% int counter = 0; %>
        <c:forEach items="${currentQuestion.answers}" var="answer">
            <% counter += 1; %>
            <input type="radio" id="answer<%= counter %>" name="answer" value="<%= counter %>" />
            <label for="answer<%= counter %>">${answer.answer}</label>
        </c:forEach>
    <button type="submit">Send answer</button>
</form>
</div>

<jsp:include page="/add/footer.jsp"/>

</body>
</html>