package com.xiangshangban.organization.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.Duty;
import com.xiangshangban.organization.dao.DutyDao;
import com.xiangshangban.organization.util.FormatUtil;

@Service
public class DutyServiceImpl implements DutyService {
	@Autowired	
	DutyDao dutyDao;
	
	
	
	@Override
	public String deleteByDuty(String dutyId) {
		String i ="0";
		try {
			dutyDao.deleteByDuty(dutyId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String insertDuty(Duty duty) {
		String i ="0";
		try {
			duty.setDutyId(FormatUtil.createUuid());
			dutyDao.insertDuty(duty);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Duty selectByDuty(String dutyId) {
		// TODO Auto-generated method stub
		return dutyDao.selectByDuty(dutyId);
	}

	@Override
	public String updateByDuty(Duty duty) {
		String i ="0";
		try {
			dutyDao.updateByDuty(duty);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<Duty> findByAllDuty(String postId) {
		// TODO Auto-generated method stub
		return dutyDao.findByAllDuty(postId);
	}

}
