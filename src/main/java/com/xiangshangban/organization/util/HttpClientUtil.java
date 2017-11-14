package com.xiangshangban.organization.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
public class HttpClientUtil {
	/**
	* httpclient 模拟post 发送json请求  (ContentType=application/json)
	* @param sendurl
	* @param data
	* @return
	*/
	public static String sendRequet(String sendurl, Object data) {
	return sendRequet(sendurl, data, ContentType.APPLICATION_JSON, new HashMap<String,String>());
	}
	/**
	* httpclient 模拟post 发送json请求
	* @param sendurl
	* @param data
	* @param contentType 
	* @param headers 头信息，map集合
	* @return
	*/
	public static String sendRequet(String sendurl, Object data, ContentType contentType, Map<String,String> headers) {
		HttpPost post = new HttpPost(sendurl);
		StringEntity myEntity = new StringEntity(JSON.toJSONString(data,false), contentType);// 构造请求数据
		myEntity.setContentEncoding("utf-8");
		post.setEntity(myEntity);// 设置请求体
		
		Iterator iter = headers.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    post.addHeader((String)entry.getKey(), (String)entry.getValue());
		}
		
		String responseContent = null; // 响应内容
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
		    response = client.execute(post);
		    if (response.getStatusLine().getStatusCode() == 200) {
		        HttpEntity entity = response.getEntity();
		        responseContent = EntityUtils.toString(entity, "UTF-8");
		    }
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (response != null)
		            response.close();
		
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (client != null)
		                client.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
		return responseContent;
	}
	public static void sendExportRequet(HttpServletRequest req, HttpServletResponse resp, String sendurl, Object data, ContentType contentType, Map<String,String> headers) {
		HttpPost post = new HttpPost(sendurl);
		StringEntity myEntity = new StringEntity(JSON.toJSONString(data,false), contentType);// 构造请求数据
		myEntity.setContentEncoding("utf-8");
		post.setEntity(myEntity);// 设置请求体
		
		Iterator iter = headers.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    post.addHeader((String)entry.getKey(), (String)entry.getValue());
		}
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
		    response = client.execute(post);
		    resp.setContentType("octets/stream"); 
			String agent = req.getHeader("USER-AGENT");
		    String excelName = response.getHeaders("excelName")[0].getValue();
		    if(agent!=null && agent.indexOf("MSIE")==-1&&agent.indexOf("rv:11")==-1 && 
					agent.indexOf("Edge")==-1 && agent.indexOf("Apache-HttpClient")==-1){//非IE
				excelName = new String(excelName.getBytes("UTF-8"), "ISO-8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+excelName);
			}else{
				response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(excelName,"UTF-8"));  	
			}
		    if (response.getStatusLine().getStatusCode() == 200) {
		        HttpEntity entity = response.getEntity();
		        InputStream is = entity.getContent();  
		        byte[] buffer = new byte[128];
		        int count = 0;
		        while ((count = is.read(buffer)) > 0) {
		            resp.getOutputStream().write(buffer, 0, count);
		        }
		        is.close();
		    }
		    
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
			
		    try {
		        if (response != null)
		            response.close();
		
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (client != null)
		                client.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
	}
	/**
	 * 根据模块名获取模块根路径url
	 * @param modeCode
	 * @return
	 */
	public static String getModeUrl(String modeCode){
		try {
			return PropertiesUtils.pathUrl(modeCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 获取包含的模块名称
	 * @return
	 */
	public static String[] getIncludeMode(){
		try {
			return PropertiesUtils.pathUrl("mode-include").split(",");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 网络文件转换为流
	 * @param urlpath
	 * @return
	 * @throws IOException 
	 */
	public static InputStream getInputStreamByUrl(String urlpath) throws IOException {
		URL url = new URL(urlpath);
		URLConnection conn = url.openConnection();
		conn.connect();
		return conn.getInputStream();
	}
}
