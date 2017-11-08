package com.xiangshangban.organization.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.OSSFile;


@Mapper
public interface OSSFileDao {

	void insertOssfile(OSSFile oSSFile);
	
	List<OSSFile> selectByAllossfilet(String customerId);
	
	OSSFile selectBysingleossfilet(@Param("key") String key,@Param("customerId") String customerId);
	
	void deleteByossfile(@Param("key") String key,@Param("customerId") String customerId);
}
