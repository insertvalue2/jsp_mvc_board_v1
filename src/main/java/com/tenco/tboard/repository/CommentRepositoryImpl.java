package com.tenco.tboard.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tenco.tboard.model.Comment;
import com.tenco.tboard.util.DBUtil;

public class CommentRepositoryImpl implements CommentRepository {

    private static final String INSERT_COMMENT_SQL = "INSERT INTO comments (board_id, user_id, content) VALUES (?, ?, ?)";
    private static final String DELETE_COMMENT_SQL = "DELETE FROM comments WHERE id = ?";
    private static final String SELECT_COMMENTS_BY_BOARD_ID = "SELECT * FROM comments WHERE board_id = ? ORDER BY created_at DESC";
    private static final String SELECT_COMMENT_BY_ID = "SELECT * FROM comments WHERE id = ?";

    @Override
    public void addComment(Comment comment) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_COMMENT_SQL)) {

            // 트랜잭션 시작
            conn.setAutoCommit(false);

            pstmt.setInt(1, comment.getBoardId());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setString(3, comment.getContent());
            pstmt.executeUpdate();

            // 트랜잭션 커밋
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DBUtil.getConnection()) {
                // 트랜잭션 롤백
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    @Override
    public void deleteComment(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_COMMENT_SQL)) {

            // 트랜잭션 시작
            conn.setAutoCommit(false);

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            // 트랜잭션 커밋
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DBUtil.getConnection()) {
                // 트랜잭션 롤백
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    @Override
    public List<Comment> getCommentsByBoardId(int boardId) {
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_COMMENTS_BY_BOARD_ID)) {
            pstmt.setInt(1, boardId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                comments.add(new Comment(rs.getInt("id"), rs.getInt("board_id"), rs.getInt("user_id"),
                        rs.getString("content"), rs.getTimestamp("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    @Override
    public Comment getCommentById(int id) {
        Comment comment = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_COMMENT_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                comment = new Comment(rs.getInt("id"), rs.getInt("board_id"), rs.getInt("user_id"),
                        rs.getString("content"), rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }
}
