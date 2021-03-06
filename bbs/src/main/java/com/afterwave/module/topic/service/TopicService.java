package com.afterwave.module.topic.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;


import com.afterwave.core.bean.Page;
import com.afterwave.core.util.JsonUtil;
import com.afterwave.module.collect.CollectService;
import com.afterwave.module.comment.pojo.CommentWithBLOBs;
import com.afterwave.module.comment.service.CommentService;
 
import com.afterwave.module.log.pojo.LogEventEnum;
import com.afterwave.module.log.pojo.LogTargetEnum;
import com.afterwave.module.log.service.LogService;
import com.afterwave.module.notification.pojo.NotificationEnum;
import com.afterwave.module.notification.service.NotificationService;
import com.afterwave.module.tag.pojo.Tag;
import com.afterwave.module.tag.service.TagService;
import com.afterwave.module.topic.mapper.TopicMapper;
import com.afterwave.module.topic.pojo.Topic;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
import com.afterwave.module.topic.pojo.VoteAction;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.pojo.UserReputation;
import com.afterwave.module.user.service.UserService;


@SuppressWarnings("rawtypes")
@Service
@Transactional
public class TopicService {

	@Autowired
	private CollectService collectService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private LogService logService;
	@Autowired
	private NotificationService notificationService;


	@Autowired
	private TagService tagService;
	@Autowired
	private TopicMapper topicMapper;


	@Autowired
	private TopicTagService topicTagService;
	@Autowired
	private UserService userService;

	public TopicWithBLOBs createTopic(String title, String content, String tag, User user) {
		TopicWithBLOBs topic = new TopicWithBLOBs();
		topic.setTitle(Jsoup.clean(title, Whitelist.none()));
		topic.setContent(Jsoup.clean(content, Whitelist.relaxed()));
		topic.setInTime(new Date());
		topic.setView(0);
		topic.setUserId(user.getId());
		topic.setCommentCount(0);
		topic.setGood(false);
		topic.setTop(false);
		topic.setUp(0);
		topic.setDown(0);
		topic.setUpIds("");
		topic.setDownIds("");
		topic.setTag(Jsoup.clean(tag, Whitelist.none()));
		topic.setWeight(0.0);
		topic = this.save(topic);

		List<Tag> tagList = tagService.save(tag.split(","));
		topicTagService.save(tagList, topic.getId());

		logService.save(LogEventEnum.CREATE_TOPIC, user.getId(), LogTargetEnum.TOPIC.name(), topic.getId(), null,
				JsonUtil.objectToJson(topic), topic);



		return topic;
	}

	public void deleteById(Integer id, Integer userId) {
		Topic topic = findById(id);
		if (topic != null) {

			collectService.deleteByTopicId(id);

			notificationService.deleteByTopic(id);

			commentService.deleteByTopic(id);

			logService.save(LogEventEnum.DELETE_TOPIC, userId, LogTargetEnum.TOPIC.name(), topic.getId(),
					JsonUtil.objectToJson(topic), null, topic);

			topicMapper.deleteByPrimaryKey(id);



		}
	}

	/**
	 * ???????????????????????????????????????????????????????????? ???????????????????????????????????????????????????????????? ???????????????????????????????????????????????????????????????????????????
	 *
	 * @param userId
	 */
	public void deleteByUserId(Integer userId) {
		topicMapper.deleteByUserId(userId);
	}

	public Page<Map> findAllForAdmin(Integer pageNo, Integer pageSize, String username, String startTime,
			String endTime, String status) {
		Integer userId = null;
		if (!StringUtils.isEmpty(username)) {
			User user = userService.findByUsername(username);
			if (user != null)
				userId = user.getId();
		}
		Boolean good = null, top = null;
		if (!StringUtils.isEmpty(status)) {
			if (status.equals("good")) {
				good = true;
			} else if (status.equals("top")) {
				top = true;
			}
		}
		if ("".equals(startTime))
			startTime = null;
		if ("".equals(endTime))
			endTime = null;
		List<Map> list = topicMapper.findTopic(userId, good, top, null, startTime, endTime, (pageNo - 1) * pageSize,
				pageSize, "t.top desc, t.weight desc, t.id desc");
		int count = topicMapper.countTopic(userId, good, top, null, startTime, endTime);
		return new Page<>(pageNo, pageSize, count, list);
	}

	public TopicWithBLOBs findById(Integer id) {
		return topicMapper.selectByPrimaryKey(id);
	}

	/**
	 * ??????????????????????????????????????????????????????
	 *
	 * @param title
	 * @return
	 */
	public Topic findByTitle(String title) {
		return topicMapper.findByTitle(title);
	}

	/**
	 * ?????????????????????
	 *
	 * @return
	 */
	public Page<Map> findByUser(Integer pageNo, Integer pageSize, Integer userId) {
		List<Map> list = topicMapper.findTopic(userId, null, null, null, null, null, (pageNo - 1) * pageSize, pageSize,
				"t.id desc");
		int count = topicMapper.countTopic(userId, null, null, null, null, null);
		return new Page<>(pageNo, pageSize, count, list);
	}

	public Page<Map> page(Integer pageNo, Integer pageSize, String tab) {
		if (tab.equalsIgnoreCase("good")) {
			List<Map> list = topicMapper.findTopic(null, true, null, null, null, null, (pageNo - 1) * pageSize,
					pageSize, "t.top desc, t.weight desc, t.id desc");
			int count = topicMapper.countTopic(null, true, null, null, null, null);
			return new Page<>(pageNo, pageSize, count, list);
		} else if (tab.equalsIgnoreCase("newest")) {
			List<Map> list = topicMapper.findTopic(null, null, null, null, null, null, (pageNo - 1) * pageSize,
					pageSize, "t.id desc, t.weight desc");
			int count = topicMapper.countTopic(null, null, null, null, null, null);
			return new Page<>(pageNo, pageSize, count, list);
		} else if (tab.equalsIgnoreCase("noanswer")) {
			List<Map> list = topicMapper.findTopic(null, null, null, 0, null, null, (pageNo - 1) * pageSize, pageSize,
					"t.top desc, t.weight desc, t.id desc");
			int count = topicMapper.countTopic(null, null, null, 0, null, null);
			return new Page<>(pageNo, pageSize, count, list);
		} else {
			List<Map> list = topicMapper.findTopic(null, null, null, null, null, null, (pageNo - 1) * pageSize,
					pageSize, "t.top desc, t.weight desc, t.id desc");
			int count = topicMapper.countTopic(null, null, null, null, null, null);
			return new Page<>(pageNo, pageSize, count, list);
		}
	}

	
	public Page<Map> pageByTagId(Integer pageNo, Integer pageSize, Integer tagId) {
		List<Map> list = topicMapper.findTopicsByTagId(tagId, (pageNo - 1) * pageSize, pageSize,
				"t.weight desc, t.id desc");
		int count = topicMapper.countTopicsByTagId(tagId);
		return new Page<>(pageNo, pageSize, count, list);
	}

	public TopicWithBLOBs save(TopicWithBLOBs topic) {
		topicMapper.insertSelective(topic);
		return topic;
	}

	public void update(TopicWithBLOBs topic) {
		topicMapper.updateByPrimaryKeySelective(topic);
	}

	public TopicWithBLOBs updateTopic(Topic oldTopic, TopicWithBLOBs topic, User user) {
		this.update(topic);

		topicTagService.deleteByTopicId(topic.getId());
		List<Tag> tagList = tagService.save(topic.getTag().split(","));
		topicTagService.save(tagList, topic.getId());

		logService.save(LogEventEnum.EDIT_TOPIC, user.getId(), LogTargetEnum.TOPIC.name(), topic.getId(),
				JsonUtil.objectToJson(oldTopic), JsonUtil.objectToJson(topic), topic);



		return topic;
	}

	public Map<String, Object> vote(Integer userId, TopicWithBLOBs topic, String action) {
		Map<String, Object> map = new HashMap<>();
		List<String> upIds = new ArrayList<>();
		List<String> downIds = new ArrayList<>();
		LogEventEnum logEventEnum = null;
		NotificationEnum notificationEnum = null;
		User topicUser = userService.findById(topic.getUserId());
		if (!StringUtils.isEmpty(topic.getUpIds())) {
			upIds = Lists.newArrayList(topic.getUpIds().split(","));
		}
		if (!StringUtils.isEmpty(topic.getDownIds())) {
			downIds = Lists.newArrayList(topic.getDownIds().split(","));
		}
		if (action.equals(VoteAction.UP.name())) {
			logEventEnum = LogEventEnum.UP_TOPIC;
			notificationEnum = NotificationEnum.UP_TOPIC;

			if (downIds.contains(String.valueOf(userId))) {
				topicUser.setReputation(topicUser.getReputation() + UserReputation.DOWN_TOPIC.getReputation());
				topic.setDown(topic.getDown() - 1);
				downIds.remove(String.valueOf(userId));
			}

			if (!upIds.contains(String.valueOf(userId))) {
				topicUser.setReputation(topicUser.getReputation() + UserReputation.UP_TOPIC.getReputation());
				upIds.add(String.valueOf(userId));
				topic.setUp(topic.getUp() + 1);
				map.put("isUp", true);
				map.put("isDown", false);
			} else {
				topicUser.setReputation(topicUser.getReputation() - UserReputation.UP_TOPIC.getReputation());
				upIds.remove(String.valueOf(userId));
				topic.setUp(topic.getUp() - 1);
				map.put("isUp", false);
				map.put("isDown", false);
			}
		} else if (action.equals(VoteAction.DOWN.name())) {
			logEventEnum = LogEventEnum.DOWN_TOPIC;
			notificationEnum = NotificationEnum.DOWN_TOPIC;

			if (upIds.contains(String.valueOf(userId))) {
				topicUser.setReputation(topicUser.getReputation() - UserReputation.UP_TOPIC.getReputation());
				topic.setUp(topic.getUp() - 1);
				upIds.remove(String.valueOf(userId));
			}

			if (!downIds.contains(String.valueOf(userId))) {
				topicUser.setReputation(topicUser.getReputation() + UserReputation.DOWN_TOPIC.getReputation());
				downIds.add(String.valueOf(userId));
				topic.setDown(topic.getDown() + 1);
				map.put("isUp", false);
				map.put("isDown", true);
			} else {
				topicUser.setReputation(topicUser.getReputation() - UserReputation.DOWN_TOPIC.getReputation());
				downIds.remove(String.valueOf(userId));
				topic.setDown(topic.getDown() - 1);
				map.put("isUp", false);
				map.put("isDown", false);
			}
		}
		topic.setUpIds(StringUtils.collectionToCommaDelimitedString(upIds));
		topic.setDownIds(StringUtils.collectionToCommaDelimitedString(downIds));
		update(topic);
		map.put("up", topic.getUp());
		map.put("down", topic.getDown());
		map.put("topicId", topic.getId());
		map.put("vote", topic.getUp() - topic.getDown());

		userService.update(topicUser);

		this.weight(topic, null);

		notificationService.sendNotification(userId, topic.getUserId(), notificationEnum, topic.getId(), null);

		logService.save(logEventEnum, userId, LogTargetEnum.TOPIC.name(), topic.getId(), null, null, topic);
		return map;
	}


	public void weight(TopicWithBLOBs topic, List<CommentWithBLOBs> comments) {
		if (comments == null) {
			comments = commentService.findByTopicId(topic.getId());
		}
		double Qview = Math.log10(topic.getView());
		int Qanswer = comments.size();
		int Qscore = topic.getUp() - topic.getDown();
		Optional<Integer> Ascore = Optional.of(0);
		if (Qanswer > 0) {
			Ascore = comments.stream().map(comment -> comment.getUp() - comment.getDown()).reduce(Integer::sum);
		}
		long Qage = topic.getInTime().getTime();
		long Qupdated = topic.getLastCommentTime() == null ? 0 : topic.getLastCommentTime().getTime();
		double weightScore = ((Qview * 4) + (Qanswer * Qscore) / 5 + Ascore.get())
				/ Math.pow(((Qage + 1) - (Qage - Qupdated) / 2), 1.5);
		topic.setWeight(weightScore);
		update(topic);
	}

}
