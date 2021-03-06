package com.xiangshangban.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Department;


@Mapper
public interface DepartmentDao {
   
    int deleteByDepartment(String departmentId);

    int insertDepartment(Department department);
  
    Department selectByDepartment(@Param("departmentId")String departmentId ,@Param("companyId")String companyId);
  
    int updateByDepartment(Department department);
        
    Department findByDepartmentNumber(String departmentNumbe);
    
    List<Department> findDepartmentTree(@Param("departmentParentId") String departmentParentId,@Param("companyId") String companyId);
    
    List<Department> findDepartmentempTree (String departmentParentId);
    
    List<Department> findByMoreDepartment(Map<String,String> map);
    //分页查询部门信息
    List<Department> findByAllFenyeDepartment(Map<String,String> map);
    
    List<Department> findByAllDepartment(String companyId);

	Department findByDepartmentById(@Param("companyId")String companyId, @Param("deptId")String deptId);

	Integer findDepartmentPageAllLength(Map<String, String> map);

	int getDepartmentByName(@Param("companyId")String companyId, 
			@Param("departmentName")String departmentName, @Param("departmentId")String departmentId);
	
	Integer selectDepartmentCountByCompanyId(@Param("companyId")String companyId);
	
	Department selectDepatmentByDepartmentNameAndCompanyId(@Param("companyId")String companyId,@Param("departmentName")String departmentName);
	/**
	 * 根据公司id查询公司部门及部门下的岗位
	 * @param companyId
	 * @return
	 */
	List<Department> selectDepartmentAndPostByCompanyId(String companyId);
}