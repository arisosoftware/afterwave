package com.afterwave.module.comment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.afterwave.config.MailTemplateConfig;
import com.afterwave.config.SiteConfig;
import com.afterwave.core.bean.Page;
import com.afterwave.core.util.EmailUtil;
import com.afterwave.core.util.FreemarkerUtil;
import com.afterwave.core.util.JsonUtil;
import com.afterwave.module.comment.mapper.CommentMapper;
import com.afterwave.module.comment.pojo.Comment;
import com.afterwave.module.comment.pojo.CommentWithBLOBs;
import com.afterwave.module.log.pojo.LogEventEnum;
import com.afterwave.module.log.pojo.LogTargetEnum;
import com.afterwave.module.log.service.LogService;
import com.afterwave.module.notification.pojo.NotificationEnum;
import com.afterwave.module.notification.service.NotificationService;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
import com.afterwave.module.topic.pojo.VoteAction;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.pojo.UserReputation;
import com.afterwave.module.user.service.UserService;

@SuppressWarnings("rawtypes")
@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private FreemarkerUtil freemarkerUtil;
	@Autowired
	private LogService logService;
	@Autowired
	private MailTemplateConfig mailTemplateConfig;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TopicService topicService;
	@Autowired
	private UserService userService;

	public CommentWithBLOBs createComment(Integer userId, TopicWithBLOBs topic, Integer commentId, String content) {
		CommentWithBLOBs comment = new CommentWithBLOBs();
		comment.setCommentId(commentId);
		comment.setUserId(userId);
		comment.setTopicId(topic.getId());
		comment.setInTime(new Date());
		comment.setUp(0);
		comment.setDown(0);
		comment.setUpIds("");
		comment.setDownIds("");
		comment.setContent(Jsoup.clean(content, Whitelist.relaxed()));
		this.save(comment);


		topic.setCommentCount(topic.getCommentCount() + 1);
		topic.setLastCommentTime(new Date());
		topicService.update(topic);


		User commentUser = userService.findById(comment.getUserId());
		if (commentId != null) {
			Comment replyComment = this.findById(commentId);
			if (!userId.equals(replyComment.getUserId())) {
				notificationService.sendNotification(userId, replyComment.getUserId(), NotificationEnum.REPLY,
						topic.getId(), content);


				User _commentUser = userService.findById(replyComment.getUserId());
				if (_commentUser.getReplyEmail() && !StringUtils.isEmpty(_commentUser.getEmail())) {
					Map<String, Object> params = Maps.newHashMap();
					params.put("username", commentUser.getUsername());
					params.put("topic", topic);
					params.put("domain", siteConfig.getBaseUrl());
					params.put("content", content);
					String subject = freemarkerUtil.format((String) mailTemplateConfig.getReplyComment().get("subject"),
							params);
					String emailContent = freemarkerUtil
							.format((String) mailTemplateConfig.getReplyComment().get("content"), params);
					emailUtil.sendEmail(_commentUser.getEmail(), subject, emailContent);
				}
			}
		}
		if (!topic.getUserId().equals(userId)) {
			notificationService.sendNotification(userId, topic.getUserId(), NotificationEnum.COMMENT, topic.getId(),
					content);

			User topicUser = userService.findById(topic.getUserId());
			if (topicUser.getCommentEmail() && !StringUtils.isEmpty(topicUser.getEmail())) {
				Map<String, Object> params = Maps.newHashMap();
				params.put("username", commentUser.getUsername());
				params.put("topic", topic);
				params.put("domain", siteConfig.getBaseUrl());
				params.put("content", content);
				String subject = freemarkerUtil.format((String) mailTemplateConfig.getCommentTopic().get("subject"),
						params);
				String emailContent = freemarkerUtil
						.format((String) mailTemplateConfig.getCommentTopic().get("content"), params);
				emailUtil.sendEmail(topicUser.getEmail(), subject, emailContent);
			}
		}

		return comment;
	}

	public void delete(Integer id, Integer userId) {
		CommentWithBLOBs comment = this.findById(id);
		if (comment != null) {
			TopicWithBLOBs topic = topicService.findById(comment.getTopicId());
			topic.setCommentCount(topic.getCommentCount() - 1);

			logService.save(LogEventEnum.DELETE_COMMENT, userId, LogTargetEnum.COMMENT.name(), comment.getId(),
					JsonUtil.objectToJson(comment), null, topic);

			List<CommentWithBLOBs> commentWithBLOBs = commentMapper.findChildByCommentId(id);
			if (commentWithBLOBs != null && commentWithBLOBs.size() > 0) {
				for (CommentWithBLOBs commentWithBLOB : commentWithBLOBs) {

					logService.save(LogEventEnum.DELETE_COMMENT, userId, LogTargetEnum.COMMENT.name(),
							commentWithBLOB.getId(), JsonUtil.objectToJson(commentWithBLOB), null, topic);
					topic.setCommentCount(topic.getCommentCount() - 1);
					commentMapper.deleteByPrimaryKey(commentWithBLOB.getId());
				}
			}
			topicService.update(topic);
			commentMapper.deleteByPrimaryKey(id);

			topicService.weight(topic, null);
		}
	}

	/**
	 * 根据话题删除评论
	 *
	 * @param topicId
	 */
	public void deleteByTopic(Integer topicId) {
		commentMapper.deleteByTopicId(topicId);
	}

	/**
	 * 删除用户发布的所有评论 不做日志记录 原因 {@link TopicService#deleteByUserId(Integer)}
	 *
	 * @param userId
	 */
	public void deleteByUserId(Integer userId) {
		commentMapper.deleteByUserId(userId);
	}


	public Page<Map> findAllForAdmin(Integer pageNo, Integer pageSize) {
		List<Map> list = commentMapper.findAllForAdmin((pageNo - 1) * pageSize, pageSize, "c.id desc");
		int count = commentMapper.countAllForAdmin();
		return new Page<>(pageNo, pageSize, count, list);
	}

	public CommentWithBLOBs findById(Integer id) {
		return commentMapper.selectByPrimaryKey(id);
	}

	public List<CommentWithBLOBs> findByTopicId(Integer topicId) {
		return commentMapper.findCommentByTopicId(topicId);
	}

	/**
	 * 查询用户的评论列表
	 *
	 * @return
	 */
	public Page<Map> findByUser(Integer pageNo, Integer pageSize, Integer userId) {
		List<Map> list = commentMapper.findByUserId(userId, (pageNo - 1) * pageSize, pageSize, "c.id desc");
		int count = commentMapper.countByUserId(userId);
		return new Page<>(pageNo, pageSize, count, list);
	}

	/**
	 * 根据话题查询评论列表
	 *
	 * @param topicId
	 * @return
	 */
	public List<Map> findCommentWithTopic(Integer topicId) {
		List<Map> comments = commentMapper.findByTopicId(topicId);
		return sortLayer(comments, new ArrayList<>(), 1); // 初始深度为1
	}

	public void save(CommentWithBLOBs comment) {
		commentMapper.insertSelective(comment);
	}

	@SuppressWarnings("unchecked")
	private List<Map> sortLayer(List<Map> comments, List<Map> newComments, Integer layer) {
		if (comments == null || comments.size() == 0) {
			return newComments;
		}
		if (newComments.size() == 0) {
			comments.forEach(map -> {
				if (map.get("comment_id") == null) {
					map.put("layer", layer);
					newComments.add(map);
				}
			});
			comments.removeAll(newComments);
			return sortLayer(comments, newComments, layer + 1);
		} else {
			for (int index = 0; index < newComments.size(); index++) {
				Map newComment = newComments.get(index);

				List<Map> findComments = new ArrayList<>();
				comments.forEach(map -> {
					if (Objects.equals(map.get("comment_id"), newComment.get("id"))) {
						map.put("layer", layer);
						findComments.add(map);
					}
				});
				comments.removeAll(findComments);

				newComments.addAll(newComments.indexOf(newComment) + 1, findComments);
				index = newComments.indexOf(newComment) + findComments.size();
			}
			return sortLayer(comments, newComments, layer + 1);
		}
	}

	public void update(CommentWithBLOBs commentWithBLOBs) {
		commentMapper.updateByPrimaryKeySelective(commentWithBLOBs);
	}

	public CommentWithBLOBs update(TopicWithBLOBs topic, CommentWithBLOBs oldComment, CommentWithBLOBs comment,
			Integer userId) {
		this.update(comment);

		logService.save(LogEventEnum.EDIT_COMMENT, userId, LogTargetEnum.COMMENT.name(), comment.getId(),
				JsonUtil.objectToJson(oldComment), JsonUtil.objectToJson(comment), topic);
		return comment;
	}

	/**
	 * 对评论投票
	 *
	 * @param userId
	 * @param comment
	 */
	public Map<String, Object> vote(Integer userId, CommentWithBLOBs comment, String action) {
		Map<String, Object> map = new HashMap<>();
		List<String> upIds = new ArrayList<>();
		List<String> downIds = new ArrayList<>();
		LogEventEnum logEventEnum = null;
		NotificationEnum notificationEnum = null;
		User commentUser = userService.findById(comment.getUserId());
		if (!StringUtils.isEmpty(comment.getUpIds())) {
			upIds = Lists.newArrayList(comment.getUpIds().split(","));
		}
		if (!StringUtils.isEmpty(comment.getDownIds())) {
			downIds = Lists.newArrayList(comment.getDownIds().split(","));
		}
		if (action.equals(VoteAction.UP.name())) {
			logEventEnum = LogEventEnum.UP_COMMENT;
			notificationEnum = NotificationEnum.UP_COMMENT;

			if (downIds.contains(String.valueOf(userId))) {
				commentUser.setReputation(commentUser.getReputation() + UserReputation.DOWN_COMMENT.getReputation());
				comment.setDown(comment.getDown() - 1);
				downIds.remove(String.valueOf(userId));
			}

			if (!upIds.contains(String.valueOf(userId))) {
				commentUser.setReputation(commentUser.getReputation() + UserReputation.UP_COMMENT.getReputation());
				upIds.add(String.valueOf(userId));
				comment.setUp(comment.getUp() + 1);
				map.put("isUp", true);
				map.put("isDown", false);
			} else {
				commentUser.setReputation(commentUser.getReputation() - UserReputation.UP_COMMENT.getReputation());
				upIds.remove(String.valueOf(userId));
				comment.setUp(comment.getUp() - 1);
				map.put("isUp", false);
				map.put("isDown", false);
			}
		} else if (action.equals(VoteAction.DOWN.name())) {
			logEventEnum = LogEventEnum.DOWN_COMMENT;
			notificationEnum = NotificationEnum.DOWN_COMMENT;

			if (upIds.contains(String.valueOf(userId))) {
				commentUser.setReputation(commentUser.getReputation() - UserReputation.UP_COMMENT.getReputation());
				comment.setUp(comment.getUp() - 1);
				upIds.remove(String.valueOf(userId));
			}

			if (!downIds.contains(String.valueOf(userId))) {
				commentUser.setReputation(commentUser.getReputation() + UserReputation.DOWN_COMMENT.getReputation());
				downIds.add(String.valueOf(userId));
				comment.setDown(comment.getDown() + 1);
				map.put("isUp", false);
				map.put("isDown", true);
			} else {
				commentUser.setReputation(commentUser.getReputation() - UserReputation.DOWN_COMMENT.getReputation());
				downIds.remove(String.valueOf(userId));
				comment.setDown(comment.getDown() - 1);
				map.put("isUp", false);
				map.put("isDown", false);
			}
		}
		map.put("commentId", comment.getId());
		map.put("up", comment.getUp());
		map.put("down", comment.getDown());
		map.put("vote", comment.getUp() - comment.getDown());
		comment.setUpIds(StringUtils.collectionToCommaDelimitedString(upIds));
		comment.setDownIds(StringUtils.collectionToCommaDelimitedString(downIds));
		update(comment);

		notificationService.sendNotification(userId, commentUser.getId(), notificationEnum, comment.getTopicId(), null);

		TopicWithBLOBs topic = topicService.findById(comment.getTopicId());
		logService.save(logEventEnum, userId, LogTargetEnum.COMMENT.name(), comment.getId(), null, null, topic);

		userService.update(commentUser);

		topicService.weight(topic, null);
		return map;
	}
}
