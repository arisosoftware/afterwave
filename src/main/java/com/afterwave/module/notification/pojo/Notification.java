package com.afterwave.module.notification.pojo;

import java.util.Date;

public class Notification {
	private String action;

	private String content;

	private Integer id;

	private Date inTime;

	private Boolean isRead;

	private Integer targetUserId;

	private Integer topicId;

	private Integer userId;

	public String getAction() {
		return action;
	}

	public String getContent() {
		return content;
	}

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public Integer getTargetUserId() {
		return targetUserId;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setAction(String action) {
		this.action = action == null ? null : action.trim();
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public void setTargetUserId(Integer targetUserId) {
		this.targetUserId = targetUserId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}