package com.afterwave.module.attachment.mapper;

import com.afterwave.module.attachment.pojo.Attachment;

public interface AttachmentMapper {
	int deleteByPrimaryKey(Integer id);


	Attachment findByMd5(String md5);

	int insert(Attachment record);

	int insertSelective(Attachment record);

	Attachment selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Attachment record);

	int updateByPrimaryKeySelective(Attachment record);
}