package com.xiangshangban.organization;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.xiangshangban.organization.filter.ServletFilter;


@SpringBootApplication
@EnableTransactionManagement
public class OrganizationApplication {
	public static Logger logger = Logger.getLogger(OrganizationApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(OrganizationApplication.class, args);
		logger.info("启动成功");
	}
	
	@Bean
    public FilterRegistrationBean filterRegistrationBean() {
 	   FilterRegistrationBean registrationBean = new FilterRegistrationBean();
 	   ServletFilter weChatFilter = new ServletFilter();
 	   registrationBean.setFilter(weChatFilter);
 	   List<String> urlPatterns = new ArrayList<String>();
 	   urlPatterns.add("/*");
 	   registrationBean.setUrlPatterns(urlPatterns);
 	   return registrationBean;
 	}
	
	
	//显示声明CommonsMultipartResolver为mutipartResolver
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(1);
        /*resolver.setMaxUploadSize(50*1024*1024);//上传文件大小 50M 50*1024*1024
*/        return resolver;
    }  
}
