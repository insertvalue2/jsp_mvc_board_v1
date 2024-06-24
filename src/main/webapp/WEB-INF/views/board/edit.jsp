<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>게시글 수정하기</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/edit.css">
</head>
<body>
    <div class="container">
        <h2>게시글 수정하기</h2>
        <form action="${pageContext.request.contextPath}/board/edit" method="post">
            <input type="hidden" name="id" value="${board.id}">
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" value="${board.title}" required>
            </div>
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" rows="10" required>${board.content}</textarea>
            </div>
            <div class="form-group">
                <input type="submit" value="수정하기" class="btn btn-submit">
                <a href="${pageContext.request.contextPath}/board/view?id=${board.id}" class="btn btn-back">취소</a>
            </div>
        </form>
    </div>
</body>
</html>
