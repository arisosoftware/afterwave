package com.afterwave.module.code.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.afterwave.module.code.pojo.Code;

public interface CodeMapper {
	int deleteByPrimaryKey(Integer id);


	List<Code> findByEmailAndCodeAndType(@Param("email") String email, @Param("code") String code,
			@Param("type") String type);

	int insert(Code record);

	int insertSelective(Code record);

	Code selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Code record);

	int updateByPrimaryKeySelective(Code record);
}