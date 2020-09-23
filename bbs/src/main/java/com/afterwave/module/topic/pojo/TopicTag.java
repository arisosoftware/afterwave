package com.afterwave.module.topic.pojo;

public class TopicTag {
	private Long id;

	private Integer tagId;

	private Integer topicId;

	public Long getId() {
		return id;
	}

	public Integer getTagId() {
		return tagId;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}
}