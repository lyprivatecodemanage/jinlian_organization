package com.xiangshangban.organization.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.ConnectEmpPostService;
import com.xiangshangban.organization.service.EmployeeService;
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
			ReturnData returnData = new ReturnData();
			//获取请求头信息			
			String companyId = request.getHeader("companyId");
			JSONObject obj = JSON.parseObject(post);
			Post posttemp=JSON.parseObject(post,Post.class);
			posttemp.setCompanyId(companyId);
			String departmentId = posttemp.getDepartmentId();
			String postName = posttemp.getPostName();			
			if(StringUtils.isNotEmpty(departmentId) && StringUtils.isNotEmpty(postName) && StringUtils.isNotEmpty(companyId)){				
				postService.insertPost(posttemp);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
			}else{
				returnData.setMessage("必传参数为空");
				returnData.setReturnCode("3006");
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
			if(StringUtils.isNotEmpty(departmentId) && StringUtils.isNotEmpty(postName) && StringUtils.isNotEmpty(companyId)){
				postService.updateByPost(posttemp);	
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");	
			}else{
				returnData.setMessage("必传参数为空");
				returnData.setReturnCode("3006");
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
		public ReturnData deleteByPost(@RequestBody String listPostString,HttpServletRequest request,HttpServletResponse response){				
			ReturnData returnData = new ReturnData();
			//获取请求头信息
			String companyId = request.getHeader("companyId");
			JSONObject obj = JSON.parseObject(listPostString);
			JSONArray listPost = obj.getJSONArray("listPost");
			if(listPost.size()==0){
				returnData.setMessage("删除岗位错误：未选择要删除的岗位");
				returnData.setReturnCode("4112");
				return returnData;
			}
			//检查是否可以删除
			for(int i=0;i<listPost.size();i++){
				String departmentid = listPost.get(i).toString();
				JSONObject objs = JSON.parseObject(departmentid);
				String postId=objs.getString("postId");	
				List<ConnectEmpPost> connectEmpPostlist = connectEmpPostService.findEmpByPostId(companyId, postId);
				if(connectEmpPostlist.size()>0){
					returnData.setMessage("删除岗位错误：岗位下有人员");
					returnData.setReturnCode("4106");
					return returnData;
				}				
			}
			for(int i=0;i<listPost.size();i++){
				String departmentid = listPost.get(i).toString();
				JSONObject objs = JSON.parseObject(departmentid);
				String postId=objs.getString("postId");	
				postService.deleteByPost(postId);
							
			}
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
			return returnData;
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
			if(StringUtils.isNotEmpty(companyId)){
				List<Post> list=postService.selectByAllPostInfo(companyId);							
					returnData.setData(list);
					returnData.setMessage("数据请求成功");
					returnData.setReturnCode("3000");			
			}else{
				returnData.setMessage("必传参数为空");
				returnData.setReturnCode("3006");
			}			
			return returnData;
			
		}
		/**
		 * 分页+模糊查询岗位信息（模糊字段：岗位名称、所属部门名称）
		 * @author 张慧
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/selectByAllFenyePost", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public ReturnData selectByAllFenyePost(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){	
			ReturnData returnData = new ReturnData();
			Map<String, String> params = new HashMap<String, String>();
			JSONObject obj = JSON.parseObject(jsonString);			
			String pageNum = obj.getString("pageNum");
			String pageRecordNum = obj.getString("pageRecordNum");
			String companyId = request.getHeader("companyId");
			if(StringUtils.isNotEmpty(pageNum) && StringUtils.isNotEmpty(pageRecordNum)){
				String pageNumPattern = "\\d{1,}";
				boolean pageNumFlag = Pattern.matches(pageNumPattern, pageNum);
				boolean pageRecordNumFlag = Pattern.matches(pageNumPattern, pageRecordNum);
				if(!pageNumFlag||!pageRecordNumFlag){
					returnData.setMessage("参数格式不正确");
					returnData.setReturnCode("3007");			
					return returnData;
				}
			}else{//默认设置
				pageNum = "1";
				pageRecordNum="10";
			}
			
			String strNum = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum)+"";
			params.put("pageRecordNum", pageRecordNum);
			params.put("fromPageNum", strNum);
			params.put("companyId", companyId);
			params.put("departmentId", obj.getString("departmentId"));
			params.put("postName", obj.getString("postName"));//岗位名称
			params.put("departmentName", obj.getString("departmentName"));//所属部门名称
			
			List<Post> list =postService.selectByAllFenyePost(params);
			int totalPages = postService.findPostPageAllLength(params);//数据总条数
			//总页数
			int pageCountnum =totalPages%Integer.parseInt(pageRecordNum)==0?
					(totalPages/Integer.parseInt(pageRecordNum)):(totalPages/Integer.parseInt(pageRecordNum)+1);	
			returnData.setTotalPages(totalPages);
			returnData.setPagecountNum(pageCountnum);
			returnData.setData(list);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
			return returnData;							
		}		
		
		/** 
		 * 根据岗位Id，查询岗位信息
		 * @param jsonString
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value="/findByPostId", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
		public ReturnData findByMorePostIfon(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){			
			ReturnData returnData = new ReturnData();
			String companyId = request.getHeader("companyId");
			JSONObject obj = JSON.parseObject(jsonString);
			String postId = obj.getString("postId");
			if(StringUtils.isEmpty(postId)){
				returnData.setMessage("必传参数为空");
				returnData.setReturnCode("3006");
				return returnData;
			}
			
			returnData.setData(postService.selectByPost(postId, companyId));
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
			return returnData;			
		}
}
