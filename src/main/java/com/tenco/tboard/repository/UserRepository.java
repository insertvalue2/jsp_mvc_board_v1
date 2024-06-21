package com.tenco.tboard.repository;

import java.util.List;

import com.tenco.tboard.model.User;

public interface UserRepository {
	void addUser(User user);
	void deleteUser(int id);
	User getUserByUsername(String username);
	List<User> getAllUsers();
}
