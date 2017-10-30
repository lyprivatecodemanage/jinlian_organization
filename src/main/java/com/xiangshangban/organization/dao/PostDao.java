package com.xiangshangban.organization.dao;




import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Post;

@Mapper
public interface PostDao {
    
    int deleteByPost(String postId);
   
    int insertPost(Post post);
 
    Post selectByPost(String postId,String companyId);

    int updateByPost(Post post);
    
    List<Post> selectByAllPostInfo(String companyId);
    
    List<Post> selectByPostName(@Param("employeeId")String employeeId,@Param("departmentId") String departmentId);
    
    
    List<Post>findByMorePostIfon(Map<String,String> map);
    
    List<Post> findBydepartmentPost (@Param("departmentId") String departmentId,@Param("companyId")String companyId);
}