package com.afterwave.module.user.mapper;

import com.afterwave.module.user.pojo.Oauth2User;

public interface Oauth2UserMapper {
	int deleteByPrimaryKey(Integer id);


	Oauth2User findByOauthUserIdAndType(String oauthUserId, String type);

	int insert(Oauth2User record);

	int insertSelective(Oauth2User record);

	Oauth2User selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Oauth2User record);

	int updateByPrimaryKeySelective(Oauth2User record);
}