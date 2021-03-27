package com.afterwave.module.topic.mapper;

import java.util.List;

import com.afterwave.module.topic.pojo.TopicTag;

public interface TopicTagMapper {
	int countByTagId(Integer tagId);

	int deleteByPrimaryKey(Long id);

	void deleteByTopicId(Integer topicId);

	void deleteInValidAssociate();


	List<TopicTag> findByTopicId(Integer topicId);

	int insert(TopicTag record);

	int insertSelective(TopicTag record);

	TopicTag selectByPrimaryKey(Long id);

	int updateByPrimaryKey(TopicTag record);

	int updateByPrimaryKeySelective(TopicTag record);
}