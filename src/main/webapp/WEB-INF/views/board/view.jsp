<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>게시글 상세보기</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/view.css">
</head>
<body>
    <div class="container">
        <h2>${board.title}</h2>
        <p>${board.content}</p>
        <p><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
        <c:if test="${board.userId == userId}">
            <a href="${pageContext.request.contextPath}/board/edit?id=${board.id}" class="btn btn-edit">수정</a>
            <a href="${pageContext.request.contextPath}/board/delete?id=${board.id}" class="btn btn-delete">삭제</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/board/list?page=1" class="btn btn-back">목록으로 돌아가기</a>

        <h3>댓글</h3>
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <p>${comment.content}</p>
                <p><fmt:formatDate value="${comment.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                <c:if test="${comment.userId == userId}">
                    <a href="${pageContext.request.contextPath}/board/deleteComment?commentId=${comment.id}&boardId=${board.id}" class="btn btn-delete">삭제</a>
                </c:if>
            </div>
        </c:forEach>
        
        <h3>댓글 작성</h3>
        <form action="${pageContext.request.contextPath}/board/addComment" method="post">
            <input type="hidden" name="boardId" value="${board.id}">
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" rows="3" required></textarea>
            </div>
            <div class="form-group">
                <input type="submit" value="댓글 작성" class="btn btn-submit">
            </div>
        </form>
    </div>
</body>
</html>
