package com.xiangshangban.organization.service;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.DepartmentTree;

public interface DepartmentService {
	
		int deleteByDepartment(String departmentId);
	
		int insertDepartment(Department department);
	  
		Department selectByDepartment(String departmentId ,String companyId);
	  
	    int updateByDepartment(Department department);	   	  
	    
	    List<DepartmentTree> getDepartmentTree(String departmentParentId,String companyId,DepartmentTree departmentTree);
	    
	    List<DepartmentTree> getDepartmentTreeAll(String companyId);
	    	    	    
	    List<Department> findByMoreDepartment(Map<String,String> map);
	    
	    List<Department> findByAllDepartment(String companyId);

		List<DepartmentTree> getDepartmentempTree(String departmentParentId, String companyId,DepartmentTree departmentTree);

		List<DepartmentTree> getDepartmentempTreeAll(String companyId);
		Department findByDepartmentNumber(String departmentNumbe);
		/**
		 * 分页+模糊查询部门信息
		 * @param map 必须传入companyId，pageRecordNum，fromPageNum，但是companyName，departmentName，employeeName可为空
		 * @return 返回分页查询结果
		 */
	    List<Department> findByAllFenyeDepartment(Map<String,String> map);
	    /**
		 * 分页+模糊查询部门信息的总数目
		 * @param map 必须传入companyId，但companyName，departmentName，employeeName可为空
		 * @return 返回分页查询结果
		 */
	    Integer findDepartmentPageAllLength(Map<String, String> params);
	    
	    Department findByDepartmentById(String companyId, String deptId);
	    /**
	     * 查询所有子部门
	     * @param departmentParentId
	     * @param companyId
	     * @return
	     */
	    public List<Department> getDepartmentChild(String departmentParentId,String companyId);
	    /**
	     * 查询重名
	     * @param companyId
	     * @param departmentName
	     * @param departmentId
	     * @return
	     */
		int getDepartmentByName(String companyId, String departmentName, String departmentId);

		Integer selectDepartmentCountByCompanyId(String companyId);
		
		Department selectDepatmentByDepartmentNameAndCompanyId(String companyId,String departmentName);
}
