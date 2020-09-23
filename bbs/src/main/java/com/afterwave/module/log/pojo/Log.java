package com.afterwave.module.log.pojo;

import java.util.Date;

public class Log {
	private String event;

	private Integer id;

	private Date inTime;

	private String target;

	private Integer targetId;

	private Integer userId;

	public String getEvent() {
		return event;
	}

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public String getTarget() {
		return target;
	}

	public Integer getTargetId() {
		return targetId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setEvent(String event) {
		this.event = event == null ? null : event.trim();
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setTarget(String target) {
		this.target = target == null ? null : target.trim();
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}