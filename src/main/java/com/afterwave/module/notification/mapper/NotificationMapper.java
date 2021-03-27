package com.afterwave.module.notification.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.afterwave.module.notification.pojo.Notification;
@SuppressWarnings("rawtypes")
public interface NotificationMapper {
	int countByTargetUserId(@Param("targetUserId") Integer targetUserId, @Param("isRead") Boolean isRead);

	int deleteByPrimaryKey(Integer id);

	void deleteNotification(@Param("targetUserId") Integer targetUserId, @Param("userId") Integer userId,
			@Param("topicId") Integer topicId);


	List<Map> findByTargetUserId(@Param("targetUserId") Integer targetUserId, @Param("isRead") Boolean isRead,
			@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("orderBy") String orderBy);

	int insert(Notification record);

	int insertSelective(Notification record);

	Notification selectByPrimaryKey(Integer id);

	void updateByIsRead(Integer targetUserId);

	int updateByPrimaryKey(Notification record);

	int updateByPrimaryKeySelective(Notification record);

	int updateByPrimaryKeyWithBLOBs(Notification record);

}