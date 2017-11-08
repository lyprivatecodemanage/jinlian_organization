package com.xiangshangban.organization.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.OSSFile;

public interface OSSFileService {
	void insertOssfile(OSSFile oSSFile);
	
    List<OSSFile> selectByAllossfilet(String customerId);
	
    OSSFile selectBysingleossfilet(@Param("key") String key,@Param("customerId") String customerId);
    
	void deleteByossfile(@Param("key") String key,@Param("customerId") String customerId);
}
