package com.tenco.tboard.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tenco.tboard.model.Board;
import com.tenco.tboard.util.DBUtil;

public class BoardRepositoryImpl implements BoardRepository {
    
    private static final String INSERT_BOARD_SQL = "INSERT INTO board (user_id, title, content) VALUES (?, ?, ?)";
    private static final String UPDATE_BOARD_SQL = "UPDATE board SET title = ?, content = ? WHERE id = ?";
    private static final String DELETE_BOARD_SQL = "DELETE FROM board WHERE id = ?";
    private static final String SELECT_BOARD_BY_ID = "SELECT * FROM board WHERE id = ?";
    private static final String SELECT_ALL_BOARDS = "SELECT * FROM board ORDER BY created_at DESC LIMIT ? OFFSET ?";
    private static final String COUNT_ALL_BOARDS = "SELECT COUNT(*) FROM board";

    @Override
    public void addBoard(Board board) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_BOARD_SQL)) {
            pstmt.setInt(1, board.getUserId());
            pstmt.setString(2, board.getTitle());
            pstmt.setString(3, board.getContent());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBoard(Board board) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOARD_SQL)) {
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setInt(3, board.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBoard(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BOARD_SQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Board getBoardById(int id) {
        Board board = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BOARD_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                board = new Board(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("content"), rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }

    @Override
    public List<Board> getAllBoards(int offset, int limit) {
        List<Board> boards = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_BOARDS)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boards.add(new Board(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("content"), rs.getTimestamp("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    @Override
    public int getTotalBoardCount() {
        int count = 0;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_ALL_BOARDS)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}