package com.afterwave.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
 
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailTemplateConfig {

	Map<String, Object> commentTopic;
	Map<String, Object> register;
	Map<String, Object> replyComment;

	public Map<String, Object> getCommentTopic() {
		return commentTopic;
	}

	public Map<String, Object> getRegister() {
		return register;
	}

	public Map<String, Object> getReplyComment() {
		return replyComment;
	}

	public void setCommentTopic(Map<String, Object> commentTopic) {
		this.commentTopic = commentTopic;
	}

	public void setRegister(Map<String, Object> register) {
		this.register = register;
	}

	public void setReplyComment(Map<String, Object> replyComment) {
		this.replyComment = replyComment;
	}
}
