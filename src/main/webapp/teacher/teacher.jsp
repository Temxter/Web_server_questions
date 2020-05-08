<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>


<jsp:include page="/add/header.jsp"/>

<div align="center">

    <h2>Teacher menu</h2>

    <h3>Assign a test to a student:</h3>
    <form action="${pageContext.request.contextPath}/singleServlet" method="post">
        <input type="hidden" name="type" value="assignTest">
        <select name="student">
            <c:forEach var="student" items="${students}">
                <option value="${student.id}">${student.name}</option>
            </c:forEach>
        </select>

        <select name="test">
                <c:forEach var="test" items="${tests}">
                    <option value="${test.id}">${test.name}</option>
                </c:forEach>
        </select>
        <input type="submit" value="Assign">
    </form>

    <h3>All marks of students:</h3>
    <table width="100%" border="1px">
        <tr>
            <th>Student</th>
            <th>Test</th>
            <th>Right Answers</th>
            <th>All Answers</th>
            <th>Date</th>
        </tr>
        <c:forEach var="mark" items="${marks}">
            <tr>
                <td>${mark.studentName}</td>
                <td>${mark.testName}</td>
                <td>${mark.passed == true ? mark.rightAnswers : "Assigned"}</td>
                <td>${mark.questionsSize}</td>
                <td>${mark.dateWithoutTime}</td>
            </tr>
        </c:forEach>
    </table>

</div>

<jsp:include page="/add/footer.jsp"/>

</body>
</html>