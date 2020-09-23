package com.afterwave.module.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afterwave.module.user.pojo.User;

public interface UserMapper {
	int count();

	int deleteByPrimaryKey(Integer id);


	List<User> findAll(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize,
			@Param("orderBy") String orderBy);

	User findUser(@Param("token") String token, @Param("username") String username, @Param("email") String email);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(User record);

	int updateByPrimaryKeySelective(User record);
}