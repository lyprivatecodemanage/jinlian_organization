package com.xiangshangban.organization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.dao.DeviceDao;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	private DeviceDao deviceDao;
	@Override
	public int selectDeviceCountByCompanyId(String companyId) {

		return deviceDao.selectDeviceCountByCompanyId(companyId);
	}

}
