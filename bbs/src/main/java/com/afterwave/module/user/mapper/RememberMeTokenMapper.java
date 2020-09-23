package com.afterwave.module.user.mapper;

import java.util.List;

import com.afterwave.module.user.pojo.RememberMeToken;

public interface RememberMeTokenMapper {
	int deleteByPrimaryKey(Integer id);

	void deleteByUsername(String username);

	List<RememberMeToken> findAllByUsername(String username);


	RememberMeToken findBySeries(String series);

	int insert(RememberMeToken record);

	int insertSelective(RememberMeToken record);

	RememberMeToken selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(RememberMeToken record);

	int updateByPrimaryKeySelective(RememberMeToken record);
}