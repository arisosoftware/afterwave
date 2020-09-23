package com.afterwave.module.log.pojo;
 
public enum LogEventEnum {

	COLLECT_TOPIC("收藏话题", "collectTopic"), COMMENT_TOPIC("评论主题", "commentTopic"), CREATE_TOPIC("发布主题", "createTopic"),
	DELETE_COLLECT_TOPIC("删除收藏的话题", "deleteCollectTopic"), DELETE_COMMENT("删除评论", "deleteComment"),
	DELETE_TOPIC("删除话题", "deleteTopic"), DOWN_COMMENT("点踩评论", "downComment"), DOWN_TOPIC("点踩话题", "downTopic"),
	EDIT_COMMENT("编辑评论", "editComment"), EDIT_TOPIC("编辑话题", "editTopic"), REPLY_COMMENT("回复评论", "replyComment"),
	UP_COMMENT("点赞评论", "upComment"), UP_TOPIC("点赞话题", "upTopic");

	private String event;
	private String name;

	LogEventEnum(String event) {
		this.event = event;
	}

	LogEventEnum(String event, String name) {
		this.event = event;
		this.name = name;
	}

	public String getEvent() {
		return event;
	}

	public String getName() {
		return name;
	}
}
