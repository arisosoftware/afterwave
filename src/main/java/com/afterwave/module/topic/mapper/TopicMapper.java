package com.afterwave.module.topic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afterwave.module.topic.pojo.Topic;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
@SuppressWarnings("rawtypes")
public interface TopicMapper {
	int countTopic(@Param("userId") Integer userId, @Param("good") Boolean good, @Param("top") Boolean top,
			@Param("commentCount") Integer commentCount, @Param("startTime") String startTime,
			@Param("endTime") String endTime);

	int countTopicsByTagId(Integer tagId);

	int deleteByPrimaryKey(Integer id);

	void deleteByUserId(Integer userId);

	List<TopicWithBLOBs> findAll();

	Topic findByTitle(String title);


	List<Map> findTopic(@Param("userId") Integer userId, @Param("good") Boolean good, @Param("top") Boolean top,
			@Param("commentCount") Integer commentCount, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize,
			@Param("orderBy") String orderBy);

	
	List<Map> findTopicsByTagId(@Param("tagId") Integer tagId, @Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, @Param("orderBy") String orderBy);

	int insert(TopicWithBLOBs record);

	int insertSelective(TopicWithBLOBs record);

	TopicWithBLOBs selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Topic record);

	int updateByPrimaryKeySelective(TopicWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(TopicWithBLOBs record);
}