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

	UserCompanyDefault selectByUserIdAndCompanyId(@Param("userId")String userId, @Param("companyId")String companyId,@Param("type")String type);

	int updateSelective(UserCompanyDefault usercompany);
	
	int deleteUserFromCompany(@Param("companyId")String companyId,@Param("employeeId")String employeeId);

	int updateActive(@Param("companyId")String companyId,@Param("employeeId")String employeeId);
	/**
	 * 查询已激活并且为默认的公司
	 * @param userId 用户ID
	 * @return
	 */
	UserCompanyDefault getActiveDefault(@Param("userId")String userId,@Param("type")String type);
	/**
	 * 查询已激活的备选公司中的第一个公司
	 * @param userId 用户ID
	 * @return
	 */
	UserCompanyDefault getActiveNoDefaultFirst(@Param("userId")String userId,@Param("type")String type);
	/**
	 * 设置当前默认打开的公司
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	int updateCurrentCompany(@Param("companyId")String companyId,@Param("employeeId")String employeeId);
}
