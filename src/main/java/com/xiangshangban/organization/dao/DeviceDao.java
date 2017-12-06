package com.xiangshangban.organization.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceDao {
	int selectDeviceCountByCompanyId(@Param("companyId")String companyId);
}
