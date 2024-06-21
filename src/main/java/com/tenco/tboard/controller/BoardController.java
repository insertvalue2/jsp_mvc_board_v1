package com.tenco.tboard.controller;

import java.io.IOException;
import java.util.List;

import com.tenco.tboard.model.Board;
import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.BoardRepository;
import com.tenco.tboard.repository.BoardRepositoryImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
    private BoardRepository boardRepository;

    @Override
    public void init() {
        // BoardRepository의 구현체를 초기화
        boardRepository = new BoardRepositoryImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        // 현재 요청에 대한 세션 객체를 가져옵니다.
        // false는 현재 세션이 존재하지 않는 경우 새 세션을
        // 생성하지 않고 null을 반환합니다.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        switch (action) {
            case "/create":
                handleCreateBoard(request, response, session);
                break;
            case "/edit":
                handleEditBoard(request, response, session);
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        switch (action) {
            case "/delete":
                handleDeleteBoard(request, response, session);
                break;
            case "/edit":
                showEditBoardForm(request, response, session);
                break;
            case "/list":
                handleListBoards(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    // 게시글 생성 처리 메소드
    private void handleCreateBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        User user = (User) session.getAttribute("user");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Board board = new Board(0, user.getId(), title, content, null);
        boardRepository.addBoard(board);
        response.sendRedirect(request.getContextPath() + "/board/list?page=1");
    }

    // 게시글 수정 처리 메소드
    private void handleEditBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int boardId = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Board board = new Board(boardId, 0, title, content, null);

        // 게시글 작성자만 수정 가능
        if (!isBoardOwner(session, boardId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        boardRepository.updateBoard(board);
        response.sendRedirect(request.getContextPath() + "/board/list?page=1");
    }

    // 게시글 수정 화면 표시 메소드
    private void showEditBoardForm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        int boardId = Integer.parseInt(request.getParameter("id"));

        // 게시글 작성자만 수정 화면 접근 가능
        if (!isBoardOwner(session, boardId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Board board = boardRepository.getBoardById(boardId);
        request.setAttribute("board", board);
        request.getRequestDispatcher("/WEB-INF/views/board/edit.jsp").forward(request, response);
    }

    // 게시글 삭제 처리 메소드
    private void handleDeleteBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int boardId = Integer.parseInt(request.getParameter("id"));

        // 게시글 작성자만 삭제 가능
        if (!isBoardOwner(session, boardId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        boardRepository.deleteBoard(boardId);
        response.sendRedirect(request.getContextPath() + "/board/list?page=1");
    }

    // 게시글 목록 처리 메소드
    private void handleListBoards(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1; // 기본 페이지 번호
        int pageSize = 10; // 페이지당 게시글 수

        // 페이지 번호 파라미터가 없는 경우 기본값으로 설정
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            // 페이지 번호가 유효하지 않은 경우 기본 페이지 번호를 사용
            page = 1;
        }

        int offset = (page - 1) * pageSize; // 시작 위치 계산

        // 게시글 목록 조회
        List<Board> boardList = boardRepository.getAllBoards(offset, pageSize);
        request.setAttribute("boardList", boardList);

        // 전체 게시글 수 조회
        int totalBoards = boardRepository.getTotalBoardCount();

        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalBoards / pageSize);
        request.setAttribute("totalPages", totalPages);

        // 페이지 이동을 위해 JSP로 포워딩
        request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
    }

    // 게시글 작성자인지 확인하는 메소드
    private boolean isBoardOwner(HttpSession session, int boardId) {
        User user = (User) session.getAttribute("user");
        Board board = boardRepository.getBoardById(boardId);
        return board != null && board.getUserId() == user.getId();
    }
}
