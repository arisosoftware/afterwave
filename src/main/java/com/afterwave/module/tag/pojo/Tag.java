package com.afterwave.module.tag.pojo;

import java.util.Date;

public class Tag {
	private Integer id;

	private Date inTime;

	private String intro;

	private String logo;

	private String name;

	private Integer topicCount;

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public String getIntro() {
		return intro;
	}

	public String getLogo() {
		return logo;
	}

	public String getName() {
		return name;
	}

	public Integer getTopicCount() {
		return topicCount;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setIntro(String intro) {
		this.intro = intro == null ? null : intro.trim();
	}

	public void setLogo(String logo) {
		this.logo = logo == null ? null : logo.trim();
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public void setTopicCount(Integer topicCount) {
		this.topicCount = topicCount;
	}
}