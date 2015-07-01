<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Performance analytics</title>
</head>
<body>
<table>
    <tr>
        <c:forEach items="${requests}" var="request">
        <td>
            <c:out value="${request.year}"/>-<c:out value="${request.month}"/>-<c:out value="${request.day}"/> <c:out
                value="${request.hour}"/>h
        </td>
        <td>
            <c:out value="${request.url}"/>
        </td>
        <td>
            <c:out value="${request.count}"/> times
        </td>
        <td>
            <c:out value="${request.duration}"/> ms
        </td>
    </tr>
    </c:forEach>
</table>

</body>
</html>
