<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP MVC 게시판</title>
</head>
<body>
    <h2>JSP MVC 게시판 테스트 페이지</h2>
    <ul>
        <li><a href="${pageContext.request.contextPath}/user/signup">회원가입</a></li>
        <li><a href="${pageContext.request.contextPath}/user/login">로그인</a></li>
        <li><a href="${pageContext.request.contextPath}/user/logout">로그아웃</a></li>
        <li><a href="${pageContext.request.contextPath}/board/list">게시판 목록</a></li>
    </ul>
</body>
</html>
