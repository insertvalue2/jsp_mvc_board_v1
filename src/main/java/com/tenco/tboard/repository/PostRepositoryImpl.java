package com.tenco.tboard.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tenco.tboard.model.Post;
import com.tenco.tboard.util.DBUtil;

public class PostRepositoryImpl implements PostRepository {
    
	private static final String INSERT_POST_SQL = "INSERT INTO posts (user_id, title, content) VALUES (?, ?, ?)";
    private static final String UPDATE_POST_SQL = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
    private static final String DELETE_POST_SQL = "DELETE FROM posts WHERE id = ?";
    private static final String SELECT_POST_BY_ID = "SELECT * FROM posts WHERE id = ?";
    private static final String SELECT_ALL_POSTS = "SELECT * FROM posts ORDER BY created_at DESC LIMIT ? OFFSET ?";
    private static final String COUNT_ALL_POSTS = "SELECT COUNT(*) FROM posts";

    @Override
    public void addPost(Post post) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_POST_SQL)) {
            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getTitle());
            pstmt.setString(3, post.getContent());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePost(Post post) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_POST_SQL)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePost(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_POST_SQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Post getPostById(int id) {
        Post post = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_POST_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                post = new Post(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("content"), rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public List<Post> getAllPosts(int offset, int limit) {
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_POSTS)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                posts.add(new Post(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("content"), rs.getTimestamp("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public int getTotalPostCount() {
        int count = 0;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_ALL_POSTS)) {
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