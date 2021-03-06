package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arjuna.ats.arjuna.common.recoveryPropertyManager;
import com.xiangshangban.organization.bean.CheckPerson;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.bean.Uroles;
import com.xiangshangban.organization.bean.UserCompanyDefault;
import com.xiangshangban.organization.bean.Uusers;
import com.xiangshangban.organization.bean.UusersRoles;
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
	@Autowired
	EmployeeService employeeService;
	
	
	
	@Override
	public ReturnData updateApplyStatus(String companyId, String userId, String status) {
		ReturnData returnData = new ReturnData();
		if("1".equals(status)){//同意
			Uusers user = usersDao.selectById(userId);
			if(user!=null && StringUtils.isNotEmpty(user.getPhone())){
				UserCompanyDefault usercompany =userCompanyDefaultDao.selectByUserIdAndCompanyId(userId, companyId ,"0");
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
					//user.setPhone(user.getPhone());
					employee.setLoginName(user.getPhone());
					//将用户信息加入到员工表
					employeeDao.insertEmployee(employee );
					checkPersonDao.updateByPrimaryKeySelective(companyId, userId, status);
					//添加公司与人员的绑定关系
					usercompany = new UserCompanyDefault();
					usercompany.setCompanyId(companyId);
					usercompany.setUserId(userId);
					//查询已激活并且为默认的公司
					UserCompanyDefault companyDefalt = userCompanyDefaultDao.getActiveDefault(user.getUserid(),"0");
					if(companyDefalt==null || StringUtils.isEmpty(companyDefalt.getCompanyId())){
						//抽取排序中已激活但非默认的第一个公司作为默认公司
						usercompany.setCurrentOption("1");
					}else{
						usercompany.setCurrentOption("2");
					}
					usercompany.setIsActive("1");
					//web端
					usercompany.setType("0");
					userCompanyDefaultDao.insertSelective(usercompany);
					//app端
					usercompany.setType("1");
					userCompanyDefaultDao.insertSelective(usercompany);
					employeeService.addClasses(companyId, userId);
					employeeService.updateDeviceEmp(employee);
				}
				List<UusersRoles> userRoleList = usersDao.selectRoleByUserIdAndCompanyId(companyId, userId);
				if(userRoleList!=null && userRoleList.size()>0){
					usersDao.updateUserRoleByCompanyId(companyId, userId, Uroles.user_role,Uroles.user_role);
				}else{
					usersDao.insertUserRoleByCompanyId(companyId, userId, Uroles.user_role);
				}
				usersDao.updateStatus(userId, "1");
				if("0".equals(usercompany.getInfoStatus())){
					returnData.setMessage("审核已同意，请继续完善人员信息");
					returnData.setReturnCode("4108");
				}else{
					returnData.setMessage("审核已同意");
					returnData.setReturnCode("4107");
				}
			}else{
				returnData.setMessage("找不到待审核人员的信息");
				returnData.setReturnCode("4111");
			}
			
		}else{//驳回
			Uusers user = usersDao.selectById(userId);
			if(user!=null && StringUtils.isNotEmpty(user.getPhone())){
				UserCompanyDefault usercompany =userCompanyDefaultDao.selectByUserIdAndCompanyId(userId, companyId,"0");
				if(usercompany!=null && StringUtils.isNotEmpty(usercompany.getCompanyId())){//已存在人员信息，不管是否已激活，都驳回失败
					returnData.setMessage("审核驳回失败，原因：人员信息已存在");
					returnData.setReturnCode("4110");
				}else{
					checkPersonDao.updateByPrimaryKeySelective(companyId, userId, status);
					returnData.setMessage("审核已驳回");
					returnData.setReturnCode("4109");
				}
			}else{
				returnData.setMessage("找不到待审核人员的信息");
				returnData.setReturnCode("4111");
			}
		}
		return returnData;
	}
	@Override
	public List<CheckPerson> getcheckListByPage(Map<String, String> params) {
		return checkPersonDao.getcheckListByPage(params);
	}
	@Override
	public int getcheckListByPageAllLength(Map<String, String> params) {
		return checkPersonDao.getcheckListByPageAllLength(params);
	}
   
}
