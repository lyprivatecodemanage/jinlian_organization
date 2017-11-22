package com.xiangshangban.organization.dao;

import org.apache.ibatis.annotations.Mapper;

import com.xiangshangban.organization.bean.OSSFile;

@Mapper
public interface OSSFileDao {

	public void addOSSFile(OSSFile oSSFile);
}
