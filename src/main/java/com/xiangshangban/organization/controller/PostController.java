package com.xiangshangban.organization.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.service.ConnectEmpPostService;
import com.xiangshangban.organization.service.PostService;

@RestController
@RequestMapping("/PostController")
public class PostController {
		@Autowired
		PostService postService;
		@Autowired
		ConnectEmpPostService connectEmpPostService;
		/**
		 * 添加岗位
		 * @param post
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/insertPost", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public String insertPost(@RequestBody String post,HttpServletRequest request,HttpServletResponse response){
			System.out.println(post);
			Post posttemp=JSON.parseObject(post,Post.class);			
			String i=postService.insertPost(posttemp);		
			return "{\"message\":\""+i+"\"}";
		}
		
		/**
		 * 修改岗位
		 * @param post
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/updateByPost", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public String updateByPost(@RequestBody String post,HttpServletRequest request,HttpServletResponse response){
			System.out.println(post);
			Post posttemp=JSON.parseObject(post,Post.class);			
			String i=postService.updateByPost(posttemp);		
			return "{\"message\":\""+i+"\"}";
		}
		
		/**
		 * 删除岗位
		 * @param postId
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/deleteByPost", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public Map<String, Object> deleteByPost(@RequestBody String postId,HttpServletRequest request,HttpServletResponse response){
			System.out.println(postId);					
			Map<String, Object> map=new HashMap<String, Object>();
			JSONObject obj = JSON.parseObject(postId);
			postId=obj.getString("postId");
			if(postId.equals("")){				
				map.put("message","删除失败");							
			}else{
				postService.deleteByPost(postId);
				map.put("message", "删除成功");		
			}	
			return map;
		}
		/**
		 * 查询一个部门下的所有岗位分类以及分类下的岗位
		 * @param departmentId
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/findBydepartmentPost", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public String findBydepartmentPost(@RequestBody String departmentId,HttpServletRequest request,HttpServletResponse response){
			String companyId="977ACD3022C24B99AC9586CC50A8F786";
			List<Post> list=postService.findBydepartmentPost(departmentId,companyId);				
			return JSON.toJSONString(list);
		}
		/**
		 * 查询所有岗位信息
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/selectByAllPostInfo", produces = "application/json;charset=UTF-8", method=RequestMethod.GET)
		public Map<String, Object> selectByAllPostInfo(HttpServletRequest request,HttpServletResponse response){	
			Map<String, Object> postlisttemp=new HashMap<String, Object>();
			String companyId="977ACD3022C24B99AC9586CC50A8F786";
			List<Post> list=postService.selectByAllPostInfo(companyId);			
			 int s=0;
			for (int i = 0; i < list.size(); i++) {
				 s=s+1;
	                String temps =String.valueOf(s);
				Post post = list.get(i);			
				String postId = post.getPostId();												
				/*List<ConnectEmpPost> Postempnumber = connectEmpPostService.findByConnectpostemp(postId);
				int countNumber = Postempnumber.size();	
				post.setCountNumber(countNumber);		*/		
				postlisttemp.put("post"+temps, post);				 											
			}
			
			return postlisttemp;
		}
		
		/** 
		 * 根据岗位名称，所属部门查询岗位信息
		 * @param jsonString
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/findByMorePostIfon", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public String findByMorePostIfon(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){			
			Map<String,String> params = new HashMap<String, String>();
			JSONObject obj = JSON.parseObject(jsonString);
			String companyId="977ACD3022C24B99AC9586CC50A8F786";
			params.put("postName", companyId);
			params.put("postName", obj.getString("postName"));
			params.put("departmentName", obj.getString("departmentName"));
			List<Post> list=postService.findByMorePostIfon(params);				
			return JSON.toJSONString(list);
		}
		
}
