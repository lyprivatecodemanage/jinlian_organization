package com.xiangshangban.organization.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.Transferjob;
import com.xiangshangban.organization.dao.TransferjobDao;
import com.xiangshangban.organization.util.FormatUtil;
@Service
public class TransferjobServiceImpl implements TransferjobService {
	@Autowired
	TransferjobDao transferjobDao;
	
	
	@Override
	public String deleteByTransferjob(String transferJobId) {
		String i ="0";
		try {
			transferjobDao.deleteByTransferjob(transferJobId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String insertTransferjob(Transferjob transferjob) {
		String i ="0";
		try {
			transferjob.setTransferJobId(FormatUtil.createUuid());	
			transferjob.setOperatingTime(transferjob.getOperatingTime());					
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			transferjob.setOperatingTime(simpleDateFormat.format(new Date()));
			transferjobDao.insertTransferjob(transferjob);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Transferjob selectByTransferjob(String transferJobId,String companyId) {
		return transferjobDao.selectByTransferjob(transferJobId, companyId);
	}

	@Override
	public String updateByTransferjob(Transferjob transferjob) {
		String i ="0";
		try {
			transferjobDao.updateByTransferjob(transferjob);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Transferjob selectByTransferjobpost(String employeeId,String companyId) {
		return transferjobDao.selectByTransferjobpost(employeeId, companyId);
	}

	@Override
	public String updateByTrandepartmentId(String departmentId) {
		String i ="0";
		try {
			transferjobDao.updateByTrandepartmentId(departmentId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String updateBytransferendtime(String employeeId,String transferEndTime) {
		String i ="0";
		try {
			transferjobDao.updateBytransferendtime(employeeId, transferEndTime);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Transferjob findByempinfo(Map<String, String> map) {
		return transferjobDao.findByempinfo(map);
	}

	@Override
	public Transferjob findByempNullinfo(Map<String, String> map) {
		return transferjobDao.findByempNullinfo(map);
	}

}
