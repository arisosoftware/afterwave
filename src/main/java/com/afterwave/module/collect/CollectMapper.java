package com.afterwave.module.collect;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/*
 * @Author Arisosoftwaredeveloper
 */
@SuppressWarnings("rawtypes")
public interface CollectMapper {
	int countByTopicId(Integer topicId);

	int countByUserId(Integer userId);

	int deleteByPrimaryKey(Integer id);

	void deleteByTopicId(Integer topicId);

	void deleteByUserId(Integer userId);


	List<Map> findByUserId(@Param("userId") Integer userId, @Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, @Param("orderBy") String orderBy);

	Collect findByUserIdAndTopicId(@Param("userId") Integer userId, @Param("topicId") Integer topicId);

	int insert(Collect record);

	int insertSelective(Collect record);

	Collect selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Collect record);

	int updateByPrimaryKeySelective(Collect record);
}