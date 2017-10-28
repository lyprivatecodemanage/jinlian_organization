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
  
    Department selectByDepartment(String departmentId ,String companyId);
  
    int updateByDepartment(Department department);
        
    Department findByDepartmentNumber(String departmentNumbe);
    
    List<Department> findDepartmentTree(@Param("departmentParentId") String departmentParentId,@Param("companyId") String companyId);
    
    List<Department> findDepartmentempTree (String departmentParentId);
    
    List<Department> findByMoreDepartment(Map<String,String> map);
    
    List<Department> findByAllDepartment(String companyId);
    
           
}