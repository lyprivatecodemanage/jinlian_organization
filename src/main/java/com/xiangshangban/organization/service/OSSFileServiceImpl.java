package com.xiangshangban.organization.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiangshangban.organization.bean.OSSFile;
import com.xiangshangban.organization.dao.OSSFileDao;


@Service
public class OSSFileServiceImpl implements OSSFileService {

	@Autowired
	OSSFileDao oSSFileDao;

	@Override
	public void insertOssfile(OSSFile oSSFile){		
		oSSFileDao.insertOssfile(oSSFile);
	}

	@Override
	public List<OSSFile> selectByAllossfilet(String customerId) {
		// TODO Auto-generated method stub
		return oSSFileDao.selectByAllossfilet(customerId);
	}

	@Override
	public void deleteByossfile(String key, String customerId) {
		oSSFileDao.deleteByossfile(key, customerId);
		
	}

	@Override
	public OSSFile selectBysingleossfilet(String key, String customerId) {
		// TODO Auto-generated method stub
		return oSSFileDao.selectBysingleossfilet(key, customerId);
	}
	
	
}
