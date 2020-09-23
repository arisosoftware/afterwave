package com.afterwave.module.topic.pojo;

import java.util.Date;

public class Topic {
	private Integer commentCount;

	private Integer down;

	private Boolean good;

	private Integer id;

	private Date inTime;

	private Date lastCommentTime;

	private Date modifyTime;

	private String tag;

	private String title;

	private Boolean top;

	private Integer up;

	private Integer userId;

	private Integer view;

	private Double weight;

	public Integer getCommentCount() {
		return commentCount;
	}

	public Integer getDown() {
		return down;
	}

	public Boolean getGood() {
		return good;
	}

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public Date getLastCommentTime() {
		return lastCommentTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public String getTag() {
		return tag;
	}

	public String getTitle() {
		return title;
	}

	public Boolean getTop() {
		return top;
	}

	public Integer getUp() {
		return up;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getView() {
		return view;
	}

	public Double getWeight() {
		return weight;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public void setDown(Integer down) {
		this.down = down;
	}

	public void setGood(Boolean good) {
		this.good = good;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setLastCommentTime(Date lastCommentTime) {
		this.lastCommentTime = lastCommentTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public void setTag(String tag) {
		this.tag = tag == null ? null : tag.trim();
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public void setTop(Boolean top) {
		this.top = top;
	}

	public void setUp(Integer up) {
		this.up = up;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setView(Integer view) {
		this.view = view;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}