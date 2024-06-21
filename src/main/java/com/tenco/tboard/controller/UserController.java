package com.tenco.tboard.controller;

import java.io.IOException;

import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.UserRepository;
import com.tenco.tboard.repository.UserRepositoryImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
	private UserRepository userRepository;

	@Override
	public void init() {
		// UserRepository의 구현체를 초기화
		userRepository = new UserRepositoryImpl();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		switch (action) {
		case "/signup":
			handleSignup(request, response);
			break;
		case "/login":
			handleLogin(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		switch (action) {
		case "/signup":
			request.getRequestDispatcher("/WEB-INF/views/user/signup.jsp").forward(request, response);
			break;
		case "/login":
			request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
			break;
		case "/logout":
			handleLogout(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	// 회원가입 처리 메소드
	private void handleSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 유효성 검사 생략
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		User user = User.builder().username(username).password(password).email(email).build();
		userRepository.addUser(user);
		response.sendRedirect(request.getContextPath() + "/user/login");
	}

	// 로그인 처리 메소드
	// 보안적 이유로 post 요청
	private void handleLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// 유효성 검사 생략
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = userRepository.getUserByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect(request.getContextPath() + "/board/list");
		} else {
			request.setAttribute("errorMessage", "잘못된 요청 입니다.");
			request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
		}
	}

	// 로그아웃 처리 메소드
	private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect(request.getContextPath() + "/user/login");
	}
}