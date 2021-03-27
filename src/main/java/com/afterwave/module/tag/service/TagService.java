package com.afterwave.module.tag.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afterwave.core.bean.Page;
import com.afterwave.module.tag.mapper.TagMapper;
import com.afterwave.module.tag.pojo.Tag;
import com.afterwave.module.topic.mapper.TopicTagMapper;

/**
 * 
 */
@Service
@Transactional
public class TagService {

	@Autowired
	private TagMapper tagMapper;
	@Autowired
	private TopicTagMapper topicTagMapper;


	public void async() {
		List<Tag> tags = tagMapper.findAll(null, null, null);

		topicTagMapper.deleteInValidAssociate();
		tags.forEach(tag -> {
			int count = topicTagMapper.countByTagId(tag.getId());
			tag.setTopicCount(count);
			this.update(tag);
		});
	}

	public void deleteById(Integer id) {
		tagMapper.deleteByPrimaryKey(id);
	}

	public Tag findById(Integer id) {
		return tagMapper.selectByPrimaryKey(id);
	}

	public Tag findByName(String name) {
		return tagMapper.findByName(name);
	}

	public List<Tag> findByNameLike(String name, Integer pageNo, Integer pageSize) {
		return tagMapper.findByNameLike("%" + name + "%", 0, 7);
	}


	public List<Tag> findByTopicId(Integer topicId) {
		return tagMapper.findByTopicId(topicId);
	}

	public Page<Tag> page(Integer pageNo, Integer pageSize) {
		List<Tag> list = tagMapper.findAll((pageNo - 1) * pageSize, pageSize, "topic_count desc");
		int count = tagMapper.count();
		return new Page<>(pageNo, pageSize, count, list);
	}

	public void save(List<Tag> tags) {
		for (Tag tag : tags) {
			this.save(tag);
		}
	}

	public List<Tag> save(String[] tags) {
		List<Tag> tagList = new ArrayList<>();
		for (String t : tags) {
			Tag tag = this.findByName(t);
			if (tag == null) {
				tag = new Tag();
				tag.setInTime(new Date());
				tag.setName(t);
				tag.setTopicCount(1);
				this.save(tag);
			} else {
				tag.setTopicCount(tag.getTopicCount() + 1);
				this.update(tag);
			}
			tagList.add(tag);
		}
		return tagList;
	}

	public void save(Tag tag) {
		tagMapper.insertSelective(tag);
	}

	public void update(Tag tag) {
		tagMapper.updateByPrimaryKeySelective(tag);
	}

	 

	public void indexedAll() {

		
	}
}
