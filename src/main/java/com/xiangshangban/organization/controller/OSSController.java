package com.xiangshangban.organization.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.OSSFile;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.OSSFileService;


@RestController
@RequestMapping(value = "/oss/")
public class OSSController {
	@Autowired
	OSSFileService oSSFileService;
	
	
	/**
	 * 删除文件
	 * @param key
	 * @param customerId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByossfile", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData deleteByossfile(@RequestParam("jsonString") String jsonString,HttpServletRequest request,HttpServletResponse response){				
		JSONObject obj = JSON.parseObject(jsonString);
		ReturnData returnData = new ReturnData();		
		String key = obj.getString("key");
		String customerId = obj.getString("customerId");
		if(!key.equals("") || !customerId.equals("")){				
			oSSFileService.deleteByossfile(key, customerId);			
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");									
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
		}	
		return returnData;
	}	
	
	
	/**
	 * 查询单条文件数据
	 * @param key
	 * @param customerId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/selectBysingleossfilet", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData selectBysingleossfilet(@RequestParam("jsonString") String jsonString,HttpServletRequest request,HttpServletResponse response){	
		ReturnData returnData = new ReturnData();
		JSONObject obj = JSON.parseObject(jsonString);
		String key = obj.getString("key");
		String customerId = obj.getString("customerId");
		OSSFile oSSFile=oSSFileService.selectBysingleossfilet(key,customerId);
		if(!key.equals("") || !customerId.equals("")){	
			returnData.setData(oSSFile);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}
		return returnData;
	}
	
	/**
	 * 查询所有有效的文件
	 * @param customerId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/selectByAllossfilet", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData selectByAllossfilet(@RequestParam("customerId") String customerId,HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		JSONObject obj = JSON.parseObject(customerId);
		customerId=obj.getString("customerId");
		List<OSSFile> oSSFilelist=oSSFileService.selectByAllossfilet(customerId);
		if(oSSFilelist.size()!=0){			
			returnData.setData(oSSFilelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}
		return returnData;
		
	}
		
	/**
	 * 把上传文件路径保存到数据库
	 * @param oSSFile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insertOssfile", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData insertOssfile(@RequestParam("oSSFile") String oSSFile,HttpServletRequest request,HttpServletResponse response){
		OSSFile ossfile=JSON.parseObject(oSSFile,OSSFile.class);
		ReturnData returnData = new ReturnData();
		String key = ossfile.getKey();
		String name = ossfile.getName();
		String customerId = ossfile.getCustomerId();
		String path = ossfile.getPath();
		String uploadUser = ossfile.getUploadUser();
		String status = ossfile.getStatus();
		String uploadTime = ossfile.getUploadTime();
		OSSFile ossFile = new OSSFile();
		ossFile.setKey(key);
		ossFile.setName(name);
		ossFile.setStatus(status);
		ossFile.setUploadUser(uploadUser);
		ossFile.setCustomerId(customerId);
		ossFile.setUploadTime(uploadTime);
		ossFile.setPath(path);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ossFile.setUploadTime(simpleDateFormat.format(new Date()));
		if(!key.equals("") || !name.equals("") || !customerId.equals("") || !path.equals("") || !uploadUser.equals("") || !status.equals("") || !uploadTime.equals("")){			
			oSSFileService.insertOssfile(ossFile);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
		}	
		return returnData;
	}
	
	
	
} 

