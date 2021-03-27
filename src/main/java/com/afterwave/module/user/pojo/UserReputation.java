package com.afterwave.module.user.pojo;

/**
 * 
 */
public enum UserReputation {

	DOWN_COMMENT("点踩评论", -2), DOWN_TOPIC("点踩话题", -2),

	UP_COMMENT("评论被点赞", 10), UP_TOPIC("话题被点赞", 5);

	private String name;
	private Integer reputation;

	UserReputation(String name, Integer reputation) {
		this.name = name;
		this.reputation = reputation;
	}

	public String getName() {
		return name;
	}

	public Integer getReputation() {
		return reputation;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReputation(Integer reputation) {
		this.reputation = reputation;
	}
}
