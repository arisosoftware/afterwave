package com.afterwave.module.security.mapper;

import java.util.List;

import com.afterwave.module.security.pojo.Role;

public interface RoleMapper {
	int deleteByPrimaryKey(Integer id);


	List<Role> findAll();

	int insert(Role record);

	int insertSelective(Role record);

	Role selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Role record);

	int updateByPrimaryKeySelective(Role record);
}