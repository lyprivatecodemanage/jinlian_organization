package com.xiangshangban.organization.service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xiangshangban.organization.bean.OSSFile;
import com.xiangshangban.organization.dao.OSSFileDao;
import com.xiangshangban.organization.util.OSSFileUtil;
import com.xiangshangban.organization.util.PropertiesUtils;
@Service
public class OSSFileServiceImpl implements OSSFileService {

	@Autowired
	OSSFileDao oSSFileDao;
	
	@Override
	public OSSFile autoAddOSSFile(String customerId, String directory, String type,InputStream input){
		try {
			OSSFile oSSFile = new OSSFile();
			String accessId = PropertiesUtils.ossProperty("accessKey");
			String accessKey = PropertiesUtils.ossProperty("securityKey");
			OSSFileUtil client  = new OSSFileUtil(accessId,accessKey);
			String key = client.autoUpload(customerId,type, directory, getKey(), input);
			oSSFile.setKey(key);
			oSSFile.setName(key);
			oSSFile.setCustomerId(customerId);
			oSSFile.setStatus("0");
			oSSFile.setPath(OSSFileUtil.getFilePath(customerId, directory, key));
			oSSFileDao.addOSSFile(oSSFile);
			return oSSFile;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	@Override
	public String getPathByKey(String customerId, String directory, String key) {
		return OSSFileUtil.getFilePath(customerId, directory, key);
	}
	/**
	 * 产生文件KEY
	 * @return
	 */
	private String getKey() {	
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	@Override
	public OSSFile addOSSFile(String customerId, String directory, MultipartFile file) {
		try {
			OSSFile oSSFile = new OSSFile();
			//从配置文件中获取登录OSS的凭证
			String accessId = PropertiesUtils.ossProperty("accessKey");
			String accessKey = PropertiesUtils.ossProperty("securityKey");
			OSSFileUtil client  = new OSSFileUtil(accessId,accessKey );
			//String customerId = "C001";//公司编号
			String userId = "u001";//用户编号ID
			String key = client.upload(customerId, directory, getKey(), file);//上传到OSS
			//设置文件相关信息
			oSSFile.setKey(key);
			oSSFile.setName(file.getOriginalFilename());
			oSSFile.setCustomerId(customerId);
			oSSFile.setUploadUser(userId);
			oSSFile.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			oSSFile.setStatus("0");
			oSSFile.setPath(OSSFileUtil.getFilePath(customerId, directory, key));
			oSSFileDao.addOSSFile(oSSFile);//数据库中存储关联关系
			return oSSFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 	
	}

	@Override
	public String deviceFileUpload(String customerId, String directory, String SN, String edtion, String type,String fileMd5, byte[] content) {
		try {
			String accessId = PropertiesUtils.ossProperty("accessKey");
			String accessKey = PropertiesUtils.ossProperty("securityKey");
			OSSFileUtil client  = new OSSFileUtil(accessId,accessKey);
			String md5 = client.deviceFileUpload(customerId, directory, SN, edtion, type,fileMd5, content);
			return md5;
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public OSSFile autoAddOSSFileByLength(String customerId,  String directory,String type, int contentLength,
			InputStream input) {
		try {
			OSSFile oSSFile = new OSSFile();
			String accessId = PropertiesUtils.ossProperty("accessKey");
			String accessKey = PropertiesUtils.ossProperty("securityKey");
			OSSFileUtil client  = new OSSFileUtil(accessId,accessKey);
			String key = client.autoUploadByLength(customerId, directory,type, getKey(), contentLength, input);
			oSSFile.setKey(key);
			oSSFile.setName(key);
			oSSFile.setCustomerId(customerId);
			oSSFile.setStatus("0");
			oSSFile.setPath(OSSFileUtil.getFilePath(customerId, directory, key));
			//oSSFileDao.addOSSFile(oSSFile);
			return oSSFile;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
}
