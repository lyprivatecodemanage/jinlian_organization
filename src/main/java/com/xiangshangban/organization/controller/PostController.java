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
		public Map<String, Object> insertPost(@RequestBody String post,HttpServletRequest request,HttpServletResponse response){
			System.out.println(post);
			String companyId="977ACD3022C24B99AC9586CC50A8F786";
			Post posttemp=JSON.parseObject(post,Post.class);
			posttemp.setCompanyId(companyId);
			String departmentId = posttemp.getDepartmentId();
			String postName = posttemp.getPostName();
			
			Map<String, Object> map=new HashMap<String, Object>();
			if(!departmentId.equals("") || !postName.equals("") || !companyId.equals("")){
				map.put("message", "添加成功");
				postService.insertPost(posttemp);
			}else{
				map.put("message", "添加失败");
			}
			return map;
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
		 * 查询所有岗位信息
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/selectByAllPostInfo", produces = "application/json;charset=UTF-8", method=RequestMethod.GET)
		public Map<String, Object> selectByAllPostInfo(HttpServletRequest request,HttpServletResponse response){	
			Map<String, Object> map=new HashMap<String, Object>();
			String companyId="977ACD3022C24B99AC9586CC50A8F786";
			List<Post> list=postService.selectByAllPostInfo(companyId);
			if(list.size()!=0){
				map.put("list", list);
				return map;
			}else{
				map.put("message", "没有查到信息");
				return map;
			}
			
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
