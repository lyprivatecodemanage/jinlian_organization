package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;

import com.xiangshangban.organization.bean.Post;

public interface PostService {
	String deleteByPost(String postId);
	   
	String insertPost(Post post);
 
    Post selectByPost(String postId,String companyId);

    String updateByPost(Post post);
    
    List<Post> selectByPostName(String employeeId,String departmentId);  
    
    List<Post> selectByAllPostInfo(String companyId);
    
    List<Post>findByMorePostIfon(Map<String,String> map);
   
    
}
