<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
</head>
<body>
    <h2>로그인</h2>
    <c:if test="${not empty errorMessage}">
        <p style="color:red;">${errorMessage}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/user/login" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="user1" required>
        <br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" value="password1" required>
        <br>
        <input type="submit" value="로그인">
    </form>
</body>
</html>