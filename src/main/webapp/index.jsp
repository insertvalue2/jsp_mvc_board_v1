<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>JSP MVC 게시판</title>
<style type="text/css">
body {
	font-family: Arial, sans-serif;
	background-color: #f4f4f4;
	color: #333;
	margin: 0;
	padding: 0;
}

.container {
	max-width: 600px;
	margin: 50px auto;
	padding: 20px;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	text-align: center;
}

h2 {
	color: #007bff;
	margin-bottom: 20px;
}

.nav-list {
	list-style-type: none;
	padding: 0;
}

.nav-list li {
	margin-bottom: 10px;
}

.nav-list a {
	text-decoration: none;
	display: inline-block;
	padding: 10px 20px;
	border-radius: 4px;
	color: white;
}

.btn-primary {
	background-color: #007bff;
}

.btn-primary:hover {
	background-color: #0056b3;
}

.btn-secondary {
	background-color: #6c757d;
}

.btn-secondary:hover {
	background-color: #5a6268;
}
</style>
</head>
<body>
	<div class="container">
		<h2>JSP MVC 게시판 테스트 페이지</h2>
		<ul class="nav-list">
			<li><a href="${pageContext.request.contextPath}/user/signup"
				class="btn btn-primary">회원가입</a></li>
			<li><a href="${pageContext.request.contextPath}/user/login"
				class="btn btn-primary">로그인</a></li>
			<li><a href="${pageContext.request.contextPath}/user/logout"
				class="btn btn-secondary">로그아웃</a></li>
			<li><a href="${pageContext.request.contextPath}/board/list"
				class="btn btn-primary">게시판 목록</a></li>
		</ul>
	</div>
</body>
</html>
