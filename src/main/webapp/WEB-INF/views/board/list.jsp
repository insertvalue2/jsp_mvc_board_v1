<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>게시판 목록</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/board.css">
</head>
<body>
    <div class="container">
        <h2>게시판 목록</h2>
        <c:forEach var="board" items="${boardList}">
            <div class="board">
                <h3><a href="${pageContext.request.contextPath}/board/view?id=${board.id}">${board.title}</a></h3>
                <p>${board.content}</p>
                <p><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                <div class="board-actions">
                    <a href="${pageContext.request.contextPath}/board/edit?id=${board.id}">수정</a>
                    <a href="${pageContext.request.contextPath}/board/delete?id=${board.id}">삭제</a>
                </div>
            </div>
        </c:forEach>
        <div class="pagination">
            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="current-page">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/board/list?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </div>
</body>
</html>
