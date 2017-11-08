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
import com.xiangshangban.organization.bean.ReturnData;
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
		public ReturnData insertPost(@RequestBody String post,HttpServletRequest request,HttpServletResponse response){
			//String companyId="977ACD3022C24B99AC9586CC50A8F786";
			ReturnData returnData = new ReturnData();
			//获取请求头信息			
			String companyId = request.getHeader("companyId");
			Post posttemp=JSON.parseObject(post,Post.class);
			posttemp.setCompanyId(companyId);
			String departmentId = posttemp.getDepartmentId();
			String postName = posttemp.getPostName();			
			if(!departmentId.equals("") || !postName.equals("") || !companyId.equals("")){				
				postService.insertPost(posttemp);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");
			}
			return returnData;
		}
		
		/**
		 * 修改岗位
		 * @param post
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/updateByPost", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public ReturnData updateByPost(@RequestBody String post,HttpServletRequest request,HttpServletResponse response){
			ReturnData returnData = new ReturnData();
			Post posttemp=JSON.parseObject(post,Post.class);
			//String companyId="977ACD3022C24B99AC9586CC50A8F786";
			//获取请求头信息			
			String companyId = request.getHeader("companyId");
			posttemp.setCompanyId(companyId);
			String departmentId = posttemp.getDepartmentId();
			String postName = posttemp.getPostName();						
			if(!departmentId.equals("") || !postName.equals("") || !companyId.equals("")){
				postService.updateByPost(posttemp);	
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");	
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");
			}
			return returnData;
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
			Map<String, Object> map=new HashMap<String, Object>();
			ReturnData returnData = new ReturnData();
			JSONObject obj = JSON.parseObject(postId);
			postId=obj.getString("postId");
			if(postId.equals("")){				
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");				
			}else{
				postService.deleteByPost(postId);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");		
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
		public ReturnData selectByAllPostInfo(HttpServletRequest request,HttpServletResponse response){	
			ReturnData returnData = new ReturnData();
			//String companyId="977ACD3022C24B99AC9586CC50A8F786";
			//获取请求头信息			
			String companyId = request.getHeader("companyId");
			List<Post> list=postService.selectByAllPostInfo(companyId);
			if(list.size()!=0){				
				returnData.setData(list);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");			
			}
			return returnData;
			
		}
		
		/** 
		 * 根据岗位名称，所属部门查询岗位信息
		 * @param jsonString
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/findByMorePostIfon", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public ReturnData findByMorePostIfon(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){			
			Map<String,String> params = new HashMap<String, String>();
			JSONObject obj = JSON.parseObject(jsonString);
			ReturnData returnData = new ReturnData();
			//String companyId="977ACD3022C24B99AC9586CC50A8F786";
			//获取请求头信息			
			String companyId = request.getHeader("companyId");			
			params.put("companyId", companyId);
			params.put("postName", obj.getString("postName"));
			params.put("departmentName", obj.getString("departmentName"));
			List<Post> list=postService.findByMorePostIfon(params);				
			if(list.size() !=0){				
				returnData.setData(list);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");				
			}
			return returnData;			
		}
		
}
