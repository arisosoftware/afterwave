package com.afterwave.module.comment.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afterwave.module.comment.pojo.Comment;
import com.afterwave.module.comment.pojo.CommentWithBLOBs;
@SuppressWarnings("rawtypes")
public interface CommentMapper {
	int countAllForAdmin();

	int countByTopicId(Integer topicId);

	int countByUserId(Integer userId);

	int deleteByPrimaryKey(Integer id);

	void deleteByTopicId(Integer topicId);

	void deleteByUserId(Integer userId);

	List<Map> findAllForAdmin(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize,
			@Param("orderBy") String orderBy);


	List<Map> findByTopicId(Integer topicId);

	List<Map> findByUserId(@Param("userId") Integer userId, @Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, @Param("orderBy") String orderBy);

	List<CommentWithBLOBs> findChildByCommentId(@Param("commentId") Integer commentId);

	List<CommentWithBLOBs> findCommentByTopicId(Integer topicId);

	int insert(CommentWithBLOBs record);

	int insertSelective(CommentWithBLOBs record);

	CommentWithBLOBs selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Comment record);

	int updateByPrimaryKeySelective(CommentWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(CommentWithBLOBs record);

}