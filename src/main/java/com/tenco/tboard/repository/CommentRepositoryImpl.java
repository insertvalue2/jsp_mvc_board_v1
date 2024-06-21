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
	
	private static final String INSERT_COMMENT_SQL = "INSERT INTO comments (post_id, user_id, content) VALUES (?, ?, ?)";
	private static final String DELETE_COMMENT_SQL = "DELETE FROM comments WHERE id = ?";
	private static final String SELECT_COMMENTS_BY_POST_ID = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at DESC";

	@Override
	public void addComment(Comment comment) {
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(INSERT_COMMENT_SQL)) {
			pstmt.setInt(1, comment.getPostId());
			pstmt.setInt(2, comment.getUserId());
			pstmt.setString(3, comment.getContent());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteComment(int id) {
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(DELETE_COMMENT_SQL)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Comment> getCommentsByPostId(int postId) {
		List<Comment> comments = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SELECT_COMMENTS_BY_POST_ID)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				comments.add(new Comment(rs.getInt("id"), rs.getInt("post_id"), rs.getInt("user_id"),
						rs.getString("content"), rs.getTimestamp("created_at")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comments;
	}
}
