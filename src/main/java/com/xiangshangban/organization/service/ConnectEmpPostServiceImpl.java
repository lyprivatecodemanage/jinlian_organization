package com.xiangshangban.organization.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.dao.ConnectEmpPostDao;

@Service
public class ConnectEmpPostServiceImpl implements ConnectEmpPostService {
    @Autowired
    ConnectEmpPostDao connectEmpPostDao;
        
    
	@Override
	public String saveConnect(ConnectEmpPost connect) {
		String i="0";
		try {			
			connectEmpPostDao.saveConnect(connect);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String deleteConnect(String employeeId,String postId) {			
		String i="0";
		try {			
			connectEmpPostDao.deleteConnect(employeeId, postId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String updateConnect(ConnectEmpPost connect) {
		String i="0";
		try {			
			connectEmpPostDao.updateConnect(connect);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	@Override
	public String updateConnectpostStaus(ConnectEmpPost connect) {
		String i="0";
		try {			
			connectEmpPostDao.updateConnectpostStaus(connect);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public ConnectEmpPost findByConnectpostemp(String employeeId,String postId) {
		// TODO Auto-generated method stub
		return connectEmpPostDao.findByConnectpostemp(postId, postId);
	}


	@Override
	public String updateConnectDelehipostStaus(String employeeId) {
		String i="0";
		try {			
			connectEmpPostDao.updateConnectDelehipostStaus(employeeId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String updatetpostGradespostStaus(ConnectEmpPost connect) {
		String i="0";
		try {			
			connectEmpPostDao.updatetpostGradespostStaus(connect);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;		
	}

	@Override
	public ConnectEmpPost findByConnect(String employeeId, String departmentId,String postGrades) {
		// TODO Auto-generated method stub
		return connectEmpPostDao.findByConnect(employeeId, departmentId, postGrades);
	}




}
