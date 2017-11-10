package com.xiangshangban.organization.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
		 * @author 张慧
		 * 查询所有岗位信息
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/selectByAllPostInfo", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public ReturnData selectByAllPostInfo(HttpServletRequest request,HttpServletResponse response){	
			ReturnData returnData = new ReturnData();
			//String companyId="977ACD3022C24B99AC9586CC50A8F786";
			//获取请求头信息			
			String companyId = request.getHeader("companyId");
			if(!companyId.equals("")){
				List<Post> list=postService.selectByAllPostInfo(companyId);							
					returnData.setData(list);
					returnData.setMessage("数据请求成功");
					returnData.setReturnCode("3000");			
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");
			}			
			return returnData;
			
		}
		/**
		 * @author 张慧
		 * 分页查询岗位信息
		 * @param pageNum
		 * @param pageRecordNum
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/selectByAllFenyePost", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public ReturnData selectByAllFenyePost(String pageNum, String pageRecordNum,HttpServletRequest request,HttpServletResponse response){	
			ReturnData returnData = new ReturnData();
			Map<String, String> params = new HashMap<String, String>();
			//String companyId="977ACD3022C24B99AC9586CC50A8F786";
			String companyId = request.getHeader("companyId");
			String pageNumPattern = "\\d{1,}";
			boolean pageNumFlag = Pattern.matches(pageNumPattern, pageNum);
			boolean pageRecordNumFlag = Pattern.matches(pageNumPattern, pageRecordNum);
			if(!pageNumFlag||!pageRecordNumFlag){
				returnData.setMessage("参数格式不正确");
				returnData.setReturnCode("3007");			
				return returnData;
			}
			List<Post> postlist = postService.selectByAllPostInfo(companyId);
			List<Post> list =new ArrayList<>();
			if(!companyId.equals("")){			
				if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
				int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
					String strNum = String.valueOf(number);
					params.put("pageRecordNum", pageRecordNum);
					params.put("fromPageNum", strNum);
					params.put("companyId", companyId);
					list=postService.selectByAllFenyePost(params);
					int totalPages = postlist.size();
					double  pageCountnum =(double)totalPages/Integer.parseInt(pageRecordNum);	
					int pagecountnum=(int) Math.ceil(pageCountnum);
					returnData.setTotalPages(totalPages);
					returnData.setPagecountNum(pagecountnum);
					returnData.setData(list);
					returnData.setMessage("数据请求成功");
					returnData.setReturnCode("3000");		
			        return returnData;
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");		
				return returnData;	
			}
		}
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");		
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
			if(!companyId.equals("")){
				List<Post> list=postService.findByMorePostIfon(params);											
					returnData.setData(list);
					returnData.setMessage("数据请求成功");
					returnData.setReturnCode("3000");				
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");
			}			
			return returnData;			
		}
		
}
