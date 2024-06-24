package com.tenco.tboard.controller;

import java.io.IOException;
import java.util.List;

import com.tenco.tboard.model.Board;
import com.tenco.tboard.model.Comment;
import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.BoardRepository;
import com.tenco.tboard.repository.BoardRepositoryImpl;
import com.tenco.tboard.repository.CommentRepository;
import com.tenco.tboard.repository.CommentRepositoryImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private BoardRepository boardRepository;
	private CommentRepository commentRepository;

	@Override
	public void init() {
		// BoardRepository와 CommentRepository의 구현체를 초기화
		boardRepository = new BoardRepositoryImpl();
		commentRepository = new CommentRepositoryImpl();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("principal") == null) {
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
		case "/addComment":
			handleAddComment(request, response, session);
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
		if (session == null || session.getAttribute("principal") == null) {
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
			handleListBoards(request, response, session);
			break;
		case "/view":
			handleViewBoard(request, response, session);
			break;
		case "/create":
			showCreateBoardForm(request, response, session);
			break;
		case "/deleteComment":
			handleDeleteComment(request, response, session);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	// 게시글 생성 처리 메소드
	private void handleCreateBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		User user = (User) session.getAttribute("principal");
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
	private void handleListBoards(HttpServletRequest request, HttpServletResponse response, HttpSession session)
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
		request.setAttribute("currentPage", page); // 현재 페이지 번호 설정

		// 현재 로그인한 사용자의 ID 설정
		if (session != null) {
			User user = (User) session.getAttribute("principal");
			if (user != null) {
				request.setAttribute("userId", user.getId());
			}
		}

		// 페이지 이동을 위해 JSP로 포워딩
		request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
	}

	// 게시글 상세보기 처리 메소드
	private void handleViewBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		int boardId = Integer.parseInt(request.getParameter("id"));
		Board board = boardRepository.getBoardById(boardId);
		if (board == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		request.setAttribute("board", board);

		// 현재 로그인한 사용자의 ID 설정
		User user = (User) session.getAttribute("principal");
		if (user != null) {
			request.setAttribute("userId", user.getId());
		}

		// 댓글 조회 및 설정
		List<Comment> comments = commentRepository.getCommentsByBoardId(boardId);
		request.setAttribute("comments", comments);

		request.getRequestDispatcher("/WEB-INF/views/board/view.jsp").forward(request, response);
	}

	// 댓글 추가 처리 메소드
	private void handleAddComment(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		User user = (User) session.getAttribute("principal");
		int boardId = Integer.parseInt(request.getParameter("boardId"));
		String content = request.getParameter("content");
		Comment comment = new Comment(0, boardId, user.getId(), content, null);
		commentRepository.addComment(comment);
		response.sendRedirect(request.getContextPath() + "/board/view?id=" + boardId);
	}

	// 댓글 삭제 처리 메소드
	private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		int boardId = Integer.parseInt(request.getParameter("boardId"));

		Comment comment = commentRepository.getCommentById(commentId);
		User user = (User) session.getAttribute("principal");

		if (comment != null && comment.getUserId() == user.getId()) {
			commentRepository.deleteComment(commentId);
		}

		response.sendRedirect(request.getContextPath() + "/board/view?id=" + boardId);
	}

	// 게시글 작성 화면 표시 메소드
	private void showCreateBoardForm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/board/create.jsp").forward(request, response);
	}

	// 게시글 작성자인지 확인하는 메소드
	private boolean isBoardOwner(HttpSession session, int boardId) {
		User user = (User) session.getAttribute("principal");
		Board board = boardRepository.getBoardById(boardId);
		return board != null && board.getUserId() == user.getId();
	}
}
