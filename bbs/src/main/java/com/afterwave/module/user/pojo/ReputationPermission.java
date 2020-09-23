package com.afterwave.module.user.pojo;

/**
 * 
 */
public enum ReputationPermission {

	DELETE_COMMENT("删除评论", 3000), DELETE_TOPIC("删除话题", 5000), EDIT_COMMENT("编辑评论", 200), EDIT_TOPIC("编辑话题", 200),
	VOTE_COMMENT("给评论投票", 15), VOTE_TOPIC("给话题投票", 15);

	private String name;
	private Integer reputation;

	ReputationPermission(String name, Integer reputation) {
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
