package com.afterwave.module.log.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.afterwave.config.LogEventConfig;
import com.afterwave.core.bean.Page;
import com.afterwave.core.util.FreemarkerUtil;
import com.afterwave.module.log.mapper.LogMapper;
import com.afterwave.module.log.pojo.LogEventEnum;
import com.afterwave.module.log.pojo.LogWithBLOBs;
import com.afterwave.module.topic.pojo.Topic;

@SuppressWarnings("rawtypes")
@Service
public class LogService {

	@Autowired
	private FreemarkerUtil freemarkerUtil;
	@Autowired
	private LogEventConfig logEventConfig;
	@Autowired
	private LogMapper logMapper;

	public void deleteByUserId(Integer userId) {
		logMapper.deleteByUserId(userId);
	}

	public Page<Map> findAllForAdmin(Integer pageNo, Integer pageSize) {
		List<Map> list = logMapper.findAllForAdmin((pageNo - 1) * pageSize, pageSize, "l.id desc");
		int count = logMapper.countAllForAdmin();
		return new Page<>(pageNo, pageSize, count, list);
	}

	public Page<LogWithBLOBs> findByUserId(Integer pageNo, Integer pageSize, Integer userId) {
		List<LogWithBLOBs> list = logMapper.findByUserId(userId, (pageNo - 1) * pageSize, pageSize, "id desc");
		int count = logMapper.countByUserId(userId);
		return new Page<>(pageNo, pageSize, count, list);
	}

	public LogWithBLOBs save(LogEventEnum event, Integer userId, String target, Integer targetId, String before,
			String after, Topic topic) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("topic", topic);
		String desc = freemarkerUtil.format(logEventConfig.getTemplate().get(event.getName()), params);
		LogWithBLOBs log = new LogWithBLOBs();
		log.setEvent(event.getEvent());
		log.setEventDescription(desc);
		log.setUserId(userId);
		log.setTarget(target);
		log.setTargetId(targetId);
		log.setBeforeContent(before);
		log.setAfterContent(after);
		log.setInTime(new Date());
		save(log);
		return log;
	}

	public void save(LogWithBLOBs log) {
		logMapper.insertSelective(log);
	}

}
