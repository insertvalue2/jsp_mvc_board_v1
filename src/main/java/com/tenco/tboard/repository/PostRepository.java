package com.tenco.tboard.repository;

import java.util.List;

import com.tenco.tboard.model.Post;

public interface PostRepository {
	void addPost(Post post);
	void updatePost(Post post);
	void deletePost(int id);
	Post getPostById(int id);
	List<Post> getAllPosts(int offset, int limit);
	int getTotalPostCount();
}
