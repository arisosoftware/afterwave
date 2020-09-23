package com.afterwave.module.collect;

import java.util.Date;
/*
 * @Author Arisosoftwaredeveloper
 * 
 * this is Pojo model
 */
public class Collect {
	private Integer id;

	private Date inTime;

	private Integer topicId;

	private Integer userId;

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public Integer getUserId() {
		return userId;
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

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}