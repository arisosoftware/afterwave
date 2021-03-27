package com.afterwave.module.topic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afterwave.module.tag.pojo.Tag;
import com.afterwave.module.tag.service.TagService;
import com.afterwave.module.topic.mapper.TopicTagMapper;
import com.afterwave.module.topic.pojo.TopicTag;

/**
 * 
 */
@Service
@Transactional
public class TopicTagService {

	@Autowired
	private TagService tagService;
	@Autowired
	private TopicTagMapper topicTagMapper;

	public void deleteByTopicId(Integer topicId) {

		List<TopicTag> topicTags = topicTagMapper.findByTopicId(topicId);
		topicTags.forEach(topicTag -> {
			Tag tag = tagService.findById(topicTag.getTagId());
			tag.setTopicCount(tag.getTopicCount() - 1);
			tagService.update(tag);
		});

		topicTagMapper.deleteByTopicId(topicId);
	}

	public List<TopicTag> save(List<Tag> tags, Integer topicId) {
		List<TopicTag> topicTags = new ArrayList<>();
		tags.forEach(_tag -> {
			TopicTag topicTag = new TopicTag();
			topicTag.setTagId(_tag.getId());
			topicTag.setTopicId(topicId);
			topicTagMapper.insertSelective(topicTag);
			topicTags.add(topicTag);
		});
		return topicTags;
	}

}
