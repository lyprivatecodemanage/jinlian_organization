package com.xiangshangban.organization.service;



import java.util.List;
import java.util.Map;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.DepartmentTree;

public interface DepartmentService {
	
		String deleteByDepartment(String departmentId);
	
		String insertDepartment(Department department);
	  
		Department selectByDepartment(String departmentId ,String companyId);
	  
	    String updateByDepartment(Department department);	   	  
	    
	    List<DepartmentTree> getDepartmentTree(String departmentParentId,String companyId,DepartmentTree departmentTree);
	    
	    List<DepartmentTree> getDepartmentTreeAll(String companyId);
	    	    	    
	    List<Department> findByMoreDepartment(Map<String,String> map);
	    
	    List<Department> findByAllDepartment(String companyId);

		List<DepartmentTree> getDepartmentempTree(String departmentParentId, String companyId,DepartmentTree departmentTree);

		List<DepartmentTree> getDepartmentempTreeAll(String companyId);
		
		//分页查询部门信息
	    List<Department> findByAllFenyeDepartment(Map<String,String> map);
}
