package com.xiangshangban.organization.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.UserCompanyDefault;

@Mapper
public interface UserCompanyDefaultDao {
	
	List<UserCompanyDefault> selectByUserId(String userId);

	int deleteByPrimaryKey(UserCompanyDefault key);

	int insert(UserCompanyDefault record);

	int insertSelective(UserCompanyDefault record);

	UserCompanyDefault selectByUserCompanyDefault(String userid);

	UserCompanyDefault selectByUserIdAndCompanyId(@Param("userId")String userId, @Param("companyId")String companyId);
}
