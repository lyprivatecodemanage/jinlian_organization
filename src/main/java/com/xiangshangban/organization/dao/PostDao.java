package com.xiangshangban.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Post;

@Mapper
public interface PostDao {
    
    int deleteByPost(@Param("postId")String postId);
   
    int insertPost(Post post);
 
    Post selectByPost(@Param("postId")String postId,@Param("companyId")String companyId);

    int updateByPost(Post post);
    
    List<Post> selectByAllPostInfo(String companyId);   
    
    List<Post> selectByPostName(@Param("employeeId")String employeeId,@Param("departmentId") String departmentId);
    //查询所有员工的岗位信息
    List<Post> selectByPostemp(@Param("employeeId")String employeeId,@Param("departmentId") String departmentId);  
    //查询所有岗位信息
    List<Post>findByMorePostIfon(Map<String,String> map);
    //分页查询岗位信息
    List<Post>selectByAllFenyePost(Map<String,String> map);
    
    List<Post> findBydepartmentPost (@Param("departmentId") String departmentId,@Param("companyId")String companyId);

	Integer findPostPageAllLength(Map<String, String> params);
	
	List<Post> selectVicePositionByEmployeeId(@Param("companyId")String companyId,@Param("employeeId")String employeeId);

	int getDepPostNumByName(@Param("companyId")String companyId, @Param("departmentId")String departmentId, 
			@Param("postName")String postName, @Param("postId")String postId);
}