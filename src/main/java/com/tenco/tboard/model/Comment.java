package com.tenco.tboard.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
	private int id;
	private int postId;
	private int userId;
	private String content;
	private Timestamp createdAt;
}
