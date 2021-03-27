package com.afterwave.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afterwave.core.base.BaseEntity;
import com.afterwave.web.tag.CommentsDirective;
import com.afterwave.web.tag.CurrentUserDirective;
import com.afterwave.web.tag.NotificationsDirective;
import com.afterwave.web.tag.ReputationDirective;
import com.afterwave.web.tag.TopicsDirective;
import com.afterwave.web.tag.UserCollectDirective;
import com.afterwave.web.tag.UserCommentDirective;
import com.afterwave.web.tag.UserDirective;
import com.afterwave.web.tag.UserTopicDirective;

import freemarker.template.TemplateModelException;

@Component
public class FreemarkerConfig {

	@Autowired
	private BaseEntity baseEntity;

	@Autowired
	private CommentsDirective commentsDirective;
	@Autowired
	private freemarker.template.Configuration configuration;
	@Autowired
	private CurrentUserDirective currentUserDirective;
	private Logger log = LoggerFactory.getLogger(FreemarkerConfig.class);
	@Autowired
	private NotificationsDirective notificationsDirective;
	@Autowired
	private ReputationDirective reputationDirective;
	@Autowired
	private SecurityTag securityTag;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TopicsDirective topicsDirective;
	@Autowired
	private UserCollectDirective userCollectDirective;
	@Autowired
	private UserCommentDirective userCommentDirective;
	@Autowired
	private UserDirective userDirective;
	@Autowired
	private UserTopicDirective userTopicDirective;




	@PostConstruct
	public void setSharedVariable() throws TemplateModelException {


		configuration.setSharedVariable("site", siteConfig);
		configuration.setSharedVariable("model", baseEntity);
		configuration.setSharedVariable("sec", securityTag);

		configuration.setSharedVariable("user_topics_tag", userTopicDirective);
		configuration.setSharedVariable("user_comments_tag", userCommentDirective);
		configuration.setSharedVariable("user_collects_tag", userCollectDirective);
		configuration.setSharedVariable("topics_tag", topicsDirective);
		configuration.setSharedVariable("user_tag", userDirective);
		configuration.setSharedVariable("current_user_tag", currentUserDirective);
		configuration.setSharedVariable("notifications_tag", notificationsDirective);
		configuration.setSharedVariable("comments_tag", commentsDirective);
		configuration.setSharedVariable("reputation_tag", reputationDirective);

		log.info("init freemarker sharedVariables {site} success...");
	}

}