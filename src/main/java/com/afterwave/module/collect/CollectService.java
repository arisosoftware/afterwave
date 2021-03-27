package com.afterwave.module.collect;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afterwave.core.bean.Page;
import com.afterwave.core.util.JsonUtil;
import com.afterwave.module.log.pojo.LogEventEnum;
import com.afterwave.module.log.pojo.LogTargetEnum;
import com.afterwave.module.log.service.LogService;
import com.afterwave.module.notification.pojo.NotificationEnum;
import com.afterwave.module.notification.service.NotificationService;
import com.afterwave.module.topic.pojo.Topic;
import com.afterwave.module.topic.service.TopicService;
/*
 * @Author Arisosoftwaredeveloper
 */
@SuppressWarnings("rawtypes")
@Service
@Transactional
public class CollectService {

	@Autowired
	private CollectMapper collectMapper;
	@Autowired
	private LogService logService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private TopicService topicService;

	public long countByTopicId(Integer topicId) {
		return collectMapper.countByTopicId(topicId);
	}

	public long countByUserId(Integer userId) {
		return collectMapper.countByUserId(userId);
	}

	public Collect createCollect(Topic topic, Integer userId) {
		Collect collect = new Collect();
		collect.setInTime(new Date());
		collect.setTopicId(topic.getId());
		collect.setUserId(userId);
		this.save(collect);

		if (!topic.getUserId().equals(userId)) {
			notificationService.sendNotification(userId, topic.getUserId(), NotificationEnum.COLLECT, topic.getId(),
					null);
		}

		logService.save(LogEventEnum.COLLECT_TOPIC, collect.getUserId(), LogTargetEnum.COLLECT.name(), collect.getId(),
				null, JsonUtil.objectToJson(collect), topic);
		return collect;
	}

	public void deleteById(int id) {
		Collect collect = collectMapper.selectByPrimaryKey(id);

		Topic topic = topicService.findById(collect.getTopicId());
		logService.save(LogEventEnum.DELETE_COLLECT_TOPIC, collect.getUserId(), LogTargetEnum.COLLECT.name(),
				collect.getId(), JsonUtil.objectToJson(collect), null, topic);
		collectMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 话题被删除了，删除对应的所有收藏记录
	 *
	 * @param topicId
	 */
	public void deleteByTopicId(Integer topicId) {
		collectMapper.deleteByTopicId(topicId);
	}

	/**
	 * 用户被删除了，删除对应的所有收藏记录 不做日志，原因 {@link TopicService#deleteByUserId(Integer)}
	 *
	 * @param userId
	 */
	public void deleteByUserId(Integer userId) {
		collectMapper.deleteByUserId(userId);
	}

	public Page<Map> findByUserId(Integer pageNo, Integer pageSize, Integer userId) {
		List<Map> list = collectMapper.findByUserId(userId, (pageNo - 1) * pageSize, pageSize, "t.id desc");
		int count = collectMapper.countByUserId(userId);
		return new Page<>(pageNo, pageSize, count, list);
	}

	public Collect findByUserIdAndTopicId(Integer userId, Integer topicId) {
		return collectMapper.findByUserIdAndTopicId(userId, topicId);
	}

	public void save(Collect collect) {
		collectMapper.insertSelective(collect);
	}
}
