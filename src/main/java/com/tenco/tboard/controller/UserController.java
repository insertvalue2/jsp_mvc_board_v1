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
        // 요청 파라미터에서 사용자 입력값을 가져옵니다.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        // 빌더 패턴을 사용하여 User 객체를 생성합니다.
        User user = User.builder().username(username).password(password).email(email).build();
        
        // 사용자 정보를 저장합니다.
        userRepository.addUser(user);
        
        // 로그인 페이지로 리디렉션합니다.
        response.sendRedirect(request.getContextPath() + "/user/login");
    }

    // 로그인 처리 메소드
    // 보안적 이유로 POST 요청으로 처리합니다.
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // 요청 파라미터에서 사용자 입력값을 가져옵니다.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 사용자 정보를 데이터베이스에서 조회합니다.
        User user = userRepository.getUserByUsernameAndPassword(username, password);
        
        // 사용자 인증을 확인합니다.
        if (user != null && user.getPassword().equals(password)) {
            // 세션에 사용자 정보를 저장합니다.
            HttpSession session = request.getSession();
            session.setAttribute("principal", user);
            
            // 게시판 목록 페이지로 리디렉션합니다.
            response.sendRedirect(request.getContextPath() + "/board/list");
        } else {
            // 인증 실패 시 오류 메시지를 설정하고 로그인 페이지로 포워딩합니다.
            request.setAttribute("errorMessage", "잘못된 요청입니다.");
            request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
        }
    }

    // 로그아웃 처리 메소드
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 세션을 무효화합니다.
        HttpSession session = request.getSession();
        session.invalidate();
        
        // 로그인 페이지로 리디렉션합니다.
        response.sendRedirect(request.getContextPath() + "/user/login");
    }
}
