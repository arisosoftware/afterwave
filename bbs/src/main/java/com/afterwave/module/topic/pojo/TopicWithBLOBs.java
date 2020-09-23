package com.afterwave.module.topic.pojo;

public class TopicWithBLOBs extends Topic {
	private String content;

	private String downIds;

	private String upIds;

	public String getContent() {
		return content;
	}

	public String getDownIds() {
		return downIds;
	}

	public String getUpIds() {
		return upIds;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public void setDownIds(String downIds) {
		this.downIds = downIds == null ? null : downIds.trim();
	}

	public void setUpIds(String upIds) {
		this.upIds = upIds == null ? null : upIds.trim();
	}
}