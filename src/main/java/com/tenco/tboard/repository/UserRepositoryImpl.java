package com.tenco.tboard.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tenco.tboard.model.User;
import com.tenco.tboard.util.DBUtil;

public class UserRepositoryImpl implements UserRepository {
    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM users WHERE username = ? AND password = ?";

    @Override
    public void addUser(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            // 트랜잭션 시작
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getEmail());
                pstmt.executeUpdate();
                
                // 트랜잭션 커밋
                conn.commit();
            } catch (SQLException e) {
                // 오류 발생 시 롤백
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Connection conn = DBUtil.getConnection()) {
            // 트랜잭션 시작
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_USER_SQL)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                
                // 트랜잭션 커밋
                conn.commit();
            } catch (SQLException e) {
                // 오류 발생 시 롤백
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByUsername(String username) {
        User user = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getTimestamp("created_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
