package com.xiangshangban.organization.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.UserCompanyDefault;
import com.xiangshangban.organization.bean.Uusers;
import com.xiangshangban.organization.dao.CheckPersonDao;
import com.xiangshangban.organization.dao.EmployeeDao;
import com.xiangshangban.organization.dao.UserCompanyDefaultDao;
import com.xiangshangban.organization.dao.UusersDao;

@Service
public class CheckPersonServiceImpl implements CheckPersonService {
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	UusersDao usersDao;
	@Autowired
	CheckPersonDao checkPersonDao;
	@Autowired
	UserCompanyDefaultDao userCompanyDefaultDao;
	@Override
	public Integer updateApplyStatus(String companyId, String userId, String status) {
		if("1".equals(status)){//同意
			Uusers user = usersDao.selectById(userId);
			if(user!=null && StringUtils.isNotEmpty(user.getPhone())){
				UserCompanyDefault usercompany =userCompanyDefaultDao.selectByUserIdAndCompanyId(userId, companyId);
				if(usercompany!=null && StringUtils.isNotEmpty(usercompany.getCompanyId())){//已存在
					if("0".equals(usercompany.getIsActive())){//未激活，则激活
						usercompany.setIsActive("1");
						userCompanyDefaultDao.updateSelective(usercompany);
					}
					checkPersonDao.updateByPrimaryKeySelective(companyId, userId, status);
				}else{
					Employee employee =new Employee();
					employee.setEmployeeId(userId);
					employee.setCompanyId(companyId);
					employee.setEmployeeName(user.getUsername());
					user.setPhone(user.getPhone());
					//将用户信息加入到员工表
					employeeDao.insertEmployee(employee );
					checkPersonDao.updateByPrimaryKeySelective(companyId, userId, status);
					//添加公司与人员的绑定关系
					usercompany = new UserCompanyDefault();
					usercompany.setCompanyId(companyId);
					usercompany.setUserId(userId);
					usercompany.setCurrentOption("0");//是否默认打开
					usercompany.setIsActive("1");
					userCompanyDefaultDao.insertSelective(usercompany);
				}
				
			}else{
				return 0;
			}
			
		}else{//驳回
			Uusers user = usersDao.selectById(userId);
			if(user!=null && StringUtils.isNotEmpty(user.getPhone())){
				UserCompanyDefault usercompany =userCompanyDefaultDao.selectByUserIdAndCompanyId(userId, companyId);
				if(usercompany!=null && StringUtils.isNotEmpty(usercompany.getCompanyId())){//已存在
					if("0".equals(usercompany.getIsActive())){//未激活，则激活
						usercompany.setIsActive("1");
						userCompanyDefaultDao.updateSelective(usercompany);
					}
					checkPersonDao.updateByPrimaryKeySelective(companyId, userId, status);
				}else{
					Employee employee =new Employee();
					employee.setEmployeeId(userId);
					employee.setCompanyId(companyId);
					employee.setEmployeeName(user.getUsername());
					user.setPhone(user.getPhone());
					//将用户信息加入到员工表
					employeeDao.insertEmployee(employee );
					checkPersonDao.updateByPrimaryKeySelective(companyId, userId, status);
					
				}
				
			}else{
				return 0;
			}
		}
		return 1;
	}
   
}
