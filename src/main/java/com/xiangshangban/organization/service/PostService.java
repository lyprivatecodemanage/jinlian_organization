package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;


import com.xiangshangban.organization.bean.Post;

public interface PostService {
	int deleteByPost(String postId);
	   
	String insertPost(Post post);
 
    Post selectByPost(String postId,String companyId);

    String updateByPost(Post post);
    
    List<Post> selectByPostName(String employeeId,String departmentId);  
    
    List<Post> selectByAllPostInfo(String companyId);
    //查询所有岗位信息
    List<Post>findByMorePostIfon(Map<String,String> map);
    //分页查询岗位信息
    List<Post>selectByAllFenyePost(Map<String,String> map);
    
    List<Post> selectByPostemp(String employeeId,String departmentId);

	Integer findPostPageAllLength(Map<String, String> params);
	/**
	 * 查询副岗位
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	List<Post> selectVicePositionByEmployeeId(String companyId,String employeeId);
	/**
	 * 查询该部门下是否存在同名岗位
	 * @param companyId
	 * @param departmentId
	 * @param postName
	 * @param postId 
	 * @return
	 */
	int getDepPostNumByName(String companyId, String departmentId, String postName, String postId);
}
