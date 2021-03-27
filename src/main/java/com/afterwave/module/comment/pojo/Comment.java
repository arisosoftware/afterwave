package com.afterwave.module.comment.pojo;

import java.util.Date;

public class Comment {
	private Integer commentId;

	private Integer down;

	private Integer id;

	private Date inTime;

	private Integer topicId;

	private Integer up;

	private Integer userId;

	public Integer getCommentId() {
		return commentId;
	}

	public Integer getDown() {
		return down;
	}

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public Integer getUp() {
		return up;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public void setDown(Integer down) {
		this.down = down;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public void setUp(Integer up) {
		this.up = up;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}